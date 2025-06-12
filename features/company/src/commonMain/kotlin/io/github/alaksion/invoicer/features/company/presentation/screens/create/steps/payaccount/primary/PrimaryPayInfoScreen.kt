package io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.primary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import invoicer.features.company.generated.resources.Res
import invoicer.features.company.generated.resources.create_company_continue
import invoicer.features.company.generated.resources.create_company_pay_info_primary_description
import invoicer.features.company.generated.resources.create_company_pay_info_primary_title
import invoicer.features.company.generated.resources.create_company_pay_info_primary_use_intermediary
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.components.CompanyPayInfoForm
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.components.CompanyPayInfoFormCallbacks
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.intermediary.IntermediaryPayInfoScreen
import io.github.alaksion.invoicer.foundation.designSystem.components.ScreenTitle
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.BackButton
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.PrimaryButton
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.SpacerSize
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.VerticalSpacer
import io.github.alaksion.invoicer.foundation.designSystem.tokens.Spacing
import org.jetbrains.compose.resources.stringResource

internal class PrimaryPayInfoScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<PrimaryPayInfoScreenModel>()
        val state = screenModel.state.collectAsState()
        val navigator = LocalNavigator.current

        val callbacks = remember {
            Callbacks(
                onChangeIban = screenModel::updateIban,
                onChangeSwift = screenModel::updateSwift,
                onChangeBankName = screenModel::updateBankName,
                onChangeBankAddress = screenModel::updateAddress,
                toggleIntermediary = screenModel::toggleIntermediary,
                onBack = { navigator?.pop() },
                onContinue = {
                    screenModel.submit()
                    val destination = if (state.value.shouldGoToIntermediary) {
                        IntermediaryPayInfoScreen()
                    } else {
                        // TODO -> Navigate to confirmation screen
                        null
                    }
                    destination?.let {
                        navigator?.push(it)
                    }
                },
            )
        }

        LaunchedEffect(Unit) { screenModel.resumeState() }

        StateContent(
            state = state.value,
            callbacks = callbacks
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun StateContent(
        state: PrimaryPayInfoState,
        callbacks: Callbacks
    ) {
        Scaffold(
            modifier = Modifier.imePadding(),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        BackButton(onBackClick = callbacks.onBack)
                    }
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.medium)
                ) {
                    PrimaryButton(
                        label = stringResource(Res.string.create_company_continue),
                        onClick = callbacks.onContinue,
                        modifier = Modifier.fillMaxWidth(),
                        isEnabled = state.buttonEnabled
                    )
                }
            }
        ) { scaffoldPadding ->

            val formCallbacks = remember {
                CompanyPayInfoFormCallbacks(
                    onChangeIban = callbacks.onChangeIban,
                    onChangeSwift = callbacks.onChangeSwift,
                    onChangeBankName = callbacks.onChangeBankName,
                    onChangeBankAddress = callbacks.onChangeBankAddress
                )
            }

            Column(
                modifier = Modifier.padding(scaffoldPadding).padding(Spacing.medium).fillMaxSize()
            ) {
                ScreenTitle(
                    title = stringResource(Res.string.create_company_pay_info_primary_title),
                    subTitle = stringResource(Res.string.create_company_pay_info_primary_description)
                )

                VerticalSpacer(SpacerSize.XLarge3)

                CompanyPayInfoForm(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    iban = state.iban,
                    swift = state.swift,
                    bankName = state.bankName,
                    bankAddress = state.bankAddress,
                    callbacks = formCallbacks
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
                    ) {
                        Switch(
                            checked = state.shouldGoToIntermediary,
                            onCheckedChange = callbacks.toggleIntermediary,
                        )

                        Text(
                            text = stringResource(Res.string.create_company_pay_info_primary_use_intermediary)
                        )
                    }
                }
            }
        }
    }

    data class Callbacks(
        val onChangeIban: (String) -> Unit,
        val onChangeSwift: (String) -> Unit,
        val onChangeBankName: (String) -> Unit,
        val onChangeBankAddress: (String) -> Unit,
        val toggleIntermediary: (Boolean) -> Unit,
        val onBack: () -> Unit,
        val onContinue: () -> Unit,
    )

}