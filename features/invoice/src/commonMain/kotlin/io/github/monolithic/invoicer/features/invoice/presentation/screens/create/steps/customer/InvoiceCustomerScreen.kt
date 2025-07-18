package io.github.monolithic.invoicer.features.invoice.presentation.screens.create.steps.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import invoicer.features.invoice.generated.resources.Res
import invoicer.features.invoice.generated.resources.invoice_create_continue_cta
import invoicer.features.invoice.generated.resources.invoice_customer_title
import io.github.monolithic.invoicer.features.invoice.presentation.screens.create.steps.configuration.InvoiceConfigurationScreen
import io.github.monolithic.invoicer.features.invoice.presentation.screens.create.steps.customer.components.InvoiceCustomerList
import io.github.monolithic.invoicer.foundation.designSystem.components.LoadingState
import io.github.monolithic.invoicer.foundation.designSystem.components.buttons.BackButton
import io.github.monolithic.invoicer.foundation.designSystem.components.buttons.PrimaryButton
import io.github.monolithic.invoicer.foundation.designSystem.components.feedback.ErrorFeedback
import io.github.monolithic.invoicer.foundation.designSystem.tokens.Spacing
import io.github.monolithic.invoicer.foundation.ui.FlowCollectEffect
import org.jetbrains.compose.resources.stringResource

internal class InvoiceCustomerScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<InvoiceCustomerScreenModel>()
        val navigator = LocalNavigator.current
        val state by screenModel.state.collectAsState()
        val callBacks = remember {
            Callbacks(
                onBack = { navigator?.parent?.pop() },
                onSubmit = screenModel::submit,
                onSelectCustomer = screenModel::selectCustomer,
                onRetry = { screenModel.loadCustomers(force = true) }
            )
        }

        LaunchedEffect(screenModel) {
            screenModel.loadCustomers()
        }

        FlowCollectEffect(
            flow = screenModel.event,
            screenModel
        ) {
            navigator?.push(InvoiceConfigurationScreen())
        }

        StateContent(
            state = state,
            callbacks = callBacks
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StateContent(
        state: InvoiceCustomerState,
        callbacks: Callbacks
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(Res.string.invoice_customer_title)
                        )
                    },
                    navigationIcon = {
                        BackButton(onBackClick = callbacks.onBack)
                    }
                )
            },
            bottomBar = {
                PrimaryButton(
                    label = stringResource(Res.string.invoice_create_continue_cta),
                    onClick = callbacks.onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.medium)
                )
            },
            modifier = Modifier.systemBarsPadding()
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(scaffoldPadding).padding(Spacing.medium)
            ) {
                when (state.mode) {
                    InvoiceCustomerMode.Content -> InvoiceCustomerList(
                        modifier = Modifier.fillMaxSize(),
                        items = state.customers,
                        selectedId = state.selectedCustomerId,
                        onSelect = callbacks.onSelectCustomer,
                    )

                    InvoiceCustomerMode.Loading -> LoadingState(Modifier.fillMaxSize())
                    InvoiceCustomerMode.Error -> ErrorFeedback(
                        modifier = Modifier.fillMaxSize(),
                        onPrimaryAction = callbacks.onRetry
                    )
                }
            }
        }
    }

    data class Callbacks(
        val onBack: () -> Unit,
        val onSubmit: () -> Unit,
        val onSelectCustomer: (String) -> Unit,
        val onRetry: () -> Unit,
    )
}
