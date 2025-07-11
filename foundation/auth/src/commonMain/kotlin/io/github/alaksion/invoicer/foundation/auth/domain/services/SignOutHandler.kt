package io.github.alaksion.invoicer.foundation.auth.domain.services

import io.github.alaksion.invoicer.foundation.auth.domain.repository.AuthRepository
import io.github.alaksion.invoicer.foundation.auth.firebase.FirebaseHelper
import io.github.alaksion.invoicer.foundation.session.SessionUpdater
import io.github.alaksion.invoicer.foundation.watchers.AuthEvent
import io.github.alaksion.invoicer.foundation.watchers.AuthEventBus

interface SignOutService {
    suspend fun signOut()
}

internal class SignOutHandler(
    private val authRepository: AuthRepository,
    private val authEventBus: AuthEventBus,
    private val firebaseHelper: FirebaseHelper,
    private val session: SessionUpdater
) : SignOutService {
    override suspend fun signOut() {
        authRepository.signOut()
        session.clearTokens()
        firebaseHelper.signOut()
        authEventBus.publishEvent(AuthEvent.SignedOut)
    }
}
