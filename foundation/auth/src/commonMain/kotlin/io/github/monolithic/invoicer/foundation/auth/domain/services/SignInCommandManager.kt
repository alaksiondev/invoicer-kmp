package io.github.monolithic.invoicer.foundation.auth.domain.services

import io.github.monolithic.invoicer.foundation.auth.domain.repository.AuthRepository
import io.github.monolithic.invoicer.foundation.auth.domain.repository.AuthTokenRepository
import io.github.monolithic.invoicer.foundation.session.SessionTokens
import io.github.monolithic.invoicer.foundation.session.SessionUpdater
import io.github.monolithic.invoicer.foundation.watchers.AuthEvent
import io.github.monolithic.invoicer.foundation.watchers.AuthEventBus

interface SignInCommandManager {
    suspend fun resolveCommand(command: SignInCommand)
}

sealed interface SignInCommand {
    data class Google(val googleSessionToken: String) : SignInCommand
    data class Credential(val userName: String, val password: String) : SignInCommand
    data object RefreshSession : SignInCommand
}

internal class SignInCommandManagerResolver(
    private val authRepository: AuthRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val authEventBus: AuthEventBus,
    private val session: SessionUpdater
) : SignInCommandManager {

    override suspend fun resolveCommand(command: SignInCommand) {
        val authToken = when (command) {
            is SignInCommand.Credential -> authRepository.signIn(
                email = command.userName,
                password = command.password
            )

            is SignInCommand.Google -> authRepository.googleSignIn(command.googleSessionToken)

            is SignInCommand.RefreshSession -> {
                authTokenRepository.getAuthTokens()?.let { tokens ->
                    session.updateTokens(
                        SessionTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    )
                }

                authRepository.refreshSession(
                    refreshToken = authTokenRepository.getAuthTokens()?.refreshToken.orEmpty()
                )
            }
        }

        authTokenRepository.storeAuthTokens(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken
        )
        session.updateTokens(
            SessionTokens(
                accessToken = authToken.accessToken,
                refreshToken = authToken.refreshToken
            )
        )

        authEventBus.publishEvent(AuthEvent.SignedIn)
    }
}
