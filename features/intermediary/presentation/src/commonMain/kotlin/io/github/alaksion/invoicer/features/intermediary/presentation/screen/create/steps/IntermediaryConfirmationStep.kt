package io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Ballot
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.MapsHomeWork
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import invoicer.features.intermediary.presentation.generated.resources.Res
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_bank_address
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_bank_name
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_iban
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_name
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_subtitle
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_swift
import invoicer.features.intermediary.presentation.generated.resources.confirm_intermediary_title
import invoicer.features.intermediary.presentation.generated.resources.create_intermediary_submit_cta
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.CreateIntermediaryEvents
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.CreateIntermediaryScreenModel
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.CreateIntermediaryState
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.create.components.IntermediaryFieldCard
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.feedback.IntermediaryFeedbackScreen
import io.github.alaksion.invoicer.features.intermediary.presentation.screen.feedback.IntermediaryFeedbackType
import io.github.alaksion.invoicer.foundation.designSystem.components.ScreenTitle
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.BackButton
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.PrimaryButton
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.SpacerSize
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.VerticalSpacer
import io.github.alaksion.invoicer.foundation.designSystem.tokens.Spacing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

internal class IntermediaryConfirmationStep : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<CreateIntermediaryScreenModel>()
        val state by screenModel.state.collectAsState()
        val scope = rememberCoroutineScope(

        )
        val snackbarHostState = remember {
            SnackbarHostState()
        }

        LaunchedEffect(screenModel) {
            screenModel.events.collectLatest {
                when (it) {
                    is CreateIntermediaryEvents.Error -> scope.launch {
                        snackbarHostState.showSnackbar(message = it.message)
                    }

                    CreateIntermediaryEvents.Success -> navigator.push(
                        IntermediaryFeedbackScreen(
                            type = IntermediaryFeedbackType.CreateSuccess
                        )
                    )
                }
            }

        }

        StateContent(
            onBack = navigator::pop,
            onContinue = screenModel::submit,
            snackBarHostState = snackbarHostState,
            state = state,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StateContent(
        state: CreateIntermediaryState,
        snackBarHostState: SnackbarHostState,
        onContinue: () -> Unit,
        onBack: () -> Unit,
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            modifier = Modifier.imePadding(),
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            },
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        BackButton(onBackClick = onBack)
                    }
                )
            },
            bottomBar = {
                PrimaryButton(
                    label = stringResource(Res.string.create_intermediary_submit_cta),
                    onClick = {
                        onContinue()
                    },
                    isEnabled = state.isSubmitting.not(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium),
                    isLoading = state.isSubmitting
                )
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium)
                    .padding(scaffoldPadding)
            ) {
                ScreenTitle(
                    title = stringResource(Res.string.confirm_intermediary_title),
                    subTitle = stringResource(Res.string.confirm_intermediary_subtitle)
                )
                VerticalSpacer(SpacerSize.XLarge3)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    VerticalSpacer(SpacerSize.Medium)
                    IntermediaryFieldCard(
                        title = stringResource(Res.string.confirm_intermediary_name),
                        value = state.name,
                        icon = Icons.Outlined.Business
                    )
                    IntermediaryFieldCard(
                        title = stringResource(Res.string.confirm_intermediary_swift),
                        value = state.swift,
                        icon = Icons.Outlined.Ballot
                    )
                    IntermediaryFieldCard(
                        title = stringResource(Res.string.confirm_intermediary_iban),
                        value = state.iban,
                        icon = Icons.Outlined.Ballot
                    )
                    IntermediaryFieldCard(
                        title = stringResource(Res.string.confirm_intermediary_bank_name),
                        value = state.bankName,
                        icon = Icons.Outlined.AccountBalance
                    )
                    IntermediaryFieldCard(
                        title = stringResource(Res.string.confirm_intermediary_bank_address),
                        value = state.bankAddress,
                        icon = Icons.Outlined.MapsHomeWork
                    )
                }
            }
        }
    }
}
