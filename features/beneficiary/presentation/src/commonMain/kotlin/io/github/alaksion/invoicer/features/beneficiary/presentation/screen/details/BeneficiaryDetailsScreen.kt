package io.github.alaksion.invoicer.features.beneficiary.presentation.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Subject
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import invoicer.features.beneficiary.presentation.generated.resources.Res
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_bank_address_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_bank_name_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_created_at_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_delete_cancel_cta
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_delete_cta
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_delete_description
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_delete_title
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_iban_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_name_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_swift_label
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_title
import invoicer.features.beneficiary.presentation.generated.resources.beneficiary_details_updated_at_label
import io.github.alaksion.invoicer.features.beneficiary.presentation.screen.details.components.BeneficiaryDetailsField
import io.github.alaksion.invoicer.features.beneficiary.presentation.screen.update.UpdateBeneficiaryScreen
import io.github.alaksion.invoicer.foundation.designSystem.components.LoadingState
import io.github.alaksion.invoicer.foundation.designSystem.components.ScreenTitle
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.BackButton
import io.github.alaksion.invoicer.foundation.designSystem.components.dialog.DefaultInvoicerDialog
import io.github.alaksion.invoicer.foundation.designSystem.components.feedback.Feedback
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.SpacerSize
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.VerticalSpacer
import io.github.alaksion.invoicer.foundation.designSystem.tokens.Spacing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

internal data class BeneficiaryDetailsScreen(
    private val id: String
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<BeneficiaryDetailsScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val snackbarHostState = remember { SnackbarHostState() }
        var showDialog by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(screenModel) {
            screenModel.events.collectLatest {
                when (it) {
                    is BeneficiaryDetailsEvent.DeleteError -> scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message
                        )
                    }

                    BeneficiaryDetailsEvent.DeleteSuccess -> navigator?.pop()
                }
            }
        }

        LaunchedEffect(Unit) { screenModel.initState(id) }

        StateContent(
            state = state,
            onBack = { navigator?.pop() },
            snackbarHost = snackbarHostState,
            onRetry = { screenModel.initState(id) },
            showDeleteDialog = showDialog,
            onConfirmDelete = {
                showDialog = false
                screenModel.deleteBeneficiary(id)
            },
            onDismissDelete = { showDialog = false },
            onRequestDelete = { showDialog = true },
            onRequestEdit = { navigator?.push(UpdateBeneficiaryScreen(id)) }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StateContent(
        state: BeneficiaryDetailsState,
        onBack: () -> Unit,
        onRetry: () -> Unit,
        snackbarHost: SnackbarHostState,
        showDeleteDialog: Boolean,
        onRequestDelete: () -> Unit,
        onConfirmDelete: () -> Unit,
        onDismissDelete: () -> Unit,
        onRequestEdit: () -> Unit,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHost)
            },
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        BackButton(onBackClick = onBack)
                    },
                    actions = {
                        if (state.mode == BeneficiaryDetailsMode.Content) {
                            IconButton(
                                onClick = onRequestEdit
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                )
                            }

                            IconButton(
                                onClick = onRequestDelete
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(Spacing.medium)
            ) {
                ScreenTitle(
                    title = stringResource(Res.string.beneficiary_details_title),
                    subTitle = null
                )
                VerticalSpacer(SpacerSize.XLarge3)

                when (val mode = state.mode) {
                    BeneficiaryDetailsMode.Loading -> LoadingState(
                        Modifier.fillMaxSize()
                    )

                    BeneficiaryDetailsMode.Content -> {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                        ) {
                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_name_label),
                                value = state.name,
                                icon = Icons.Default.Business,
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_bank_name_label),
                                value = state.bankName,
                                icon = Icons.Default.AccountBalance
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_bank_address_label),
                                value = state.bankAddress,
                                icon = Icons.Default.LocationOn
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_swift_label),
                                value = state.swift,
                                icon = Icons.AutoMirrored.Default.Subject
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_iban_label),
                                value = state.iban,
                                icon = Icons.AutoMirrored.Default.Subject
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_created_at_label),
                                value = state.createdAt,
                                icon = Icons.Default.CalendarMonth
                            )

                            BeneficiaryDetailsField(
                                label = stringResource(Res.string.beneficiary_details_updated_at_label),
                                value = state.updatedAt,
                                icon = Icons.Default.CalendarMonth
                            )
                        }

                        if (showDeleteDialog) {
                            DefaultInvoicerDialog(
                                onDismiss = onDismissDelete,
                                title = stringResource(Res.string.beneficiary_details_delete_title),
                                description = stringResource(Res.string.beneficiary_details_delete_description),
                                confirmButtonText = stringResource(Res.string.beneficiary_details_delete_cta),
                                cancelButtonText = stringResource(Res.string.beneficiary_details_delete_cancel_cta),
                                confirmButtonClick = onConfirmDelete,
                                icon = Icons.Rounded.WarningAmber
                            )
                        }
                    }

                    is BeneficiaryDetailsMode.Error -> {
                        Feedback(
                            modifier = Modifier.fillMaxSize(),
                            title = stringResource(mode.type.titleResource),
                            description = mode.type.descriptionResource?.let { stringResource(it) },
                            icon = mode.type.icon,
                            primaryActionText = stringResource(mode.type.ctaResource),
                            onPrimaryAction = {
                                when (mode.type) {
                                    BeneficiaryErrorType.NotFound -> onBack()
                                    BeneficiaryErrorType.Generic -> onRetry()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
