package io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import invoicer.features.company.generated.resources.Res
import invoicer.features.company.generated.resources.create_company_address_city_label
import invoicer.features.company.generated.resources.create_company_address_description
import invoicer.features.company.generated.resources.create_company_address_line_1_label
import invoicer.features.company.generated.resources.create_company_address_line_2_help_text
import invoicer.features.company.generated.resources.create_company_address_line_2_label
import invoicer.features.company.generated.resources.create_company_address_postal_code_label
import invoicer.features.company.generated.resources.create_company_address_state_label
import invoicer.features.company.generated.resources.create_company_address_title
import invoicer.features.company.generated.resources.create_company_continue
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.primary.PrimaryPayInfoScreen
import io.github.alaksion.invoicer.foundation.designSystem.components.InputField
import io.github.alaksion.invoicer.foundation.designSystem.components.ScreenTitle
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.BackButton
import io.github.alaksion.invoicer.foundation.designSystem.components.buttons.PrimaryButton
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.SpacerSize
import io.github.alaksion.invoicer.foundation.designSystem.components.spacer.VerticalSpacer
import io.github.alaksion.invoicer.foundation.designSystem.tokens.Spacing
import org.jetbrains.compose.resources.stringResource


internal class CompanyAddressStep : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<CompanyAddressScreenModel>()
        val navigator = LocalNavigator.current

        val state = screenModel.state.collectAsState()
        val callbacks = remember {
            Callbacks(
                onAddressLine1Change = screenModel::setAddressLine1,
                onAddressLine2Change = screenModel::setAddressLine2,
                onCityChange = screenModel::setCity,
                onStateChange = screenModel::setState,
                onPostalCodeChange = screenModel::setPostalCode,
                onNextClick = { navigator?.push(PrimaryPayInfoScreen()) },
                onBackClick = { navigator?.pop() })
        }

        LaunchedEffect(Unit) { screenModel.resumeState() }

        StateContent(
            state = state.value, callbacks = callbacks
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StateContent(
        state: CompanyAddressState,
        callbacks: Callbacks
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        BackButton(onBackClick = callbacks.onBackClick)
                    }
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.medium)
                ) {
                    PrimaryButton(
                        label = stringResource(Res.string.create_company_continue),
                        isEnabled = state.isButtonEnabled,
                        onClick = callbacks.onNextClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(Spacing.medium)
                    .verticalScroll(scrollState)
            ) {
                ScreenTitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.create_company_address_title),
                    subTitle = stringResource(Res.string.create_company_address_description)
                )
                VerticalSpacer(SpacerSize.XLarge3)

                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    InputField(
                        value = state.addressLine1,
                        onValueChange = callbacks.onAddressLine1Change,
                        label = {
                            Text(
                                text = stringResource(Res.string.create_company_address_line_1_label)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    InputField(
                        value = state.addressLine2,
                        onValueChange = callbacks.onAddressLine2Change,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(Res.string.create_company_address_line_2_label)
                            )
                        },
                        supportingText = {
                            Text(
                                text = stringResource(Res.string.create_company_address_line_2_help_text)
                            )
                        }
                    )
                    InputField(
                        value = state.city,
                        onValueChange = callbacks.onCityChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(Res.string.create_company_address_city_label)
                            )
                        },
                    )
                    InputField(
                        value = state.state,
                        onValueChange = callbacks.onStateChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(Res.string.create_company_address_state_label)
                            )
                        },
                    )
                    InputField(
                        value = state.postalCode,
                        onValueChange = callbacks.onPostalCodeChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(
                                    Res.string.create_company_address_postal_code_label
                                )
                            )
                        },
                    )
                }
            }
        }
    }

    data class Callbacks(
        val onAddressLine1Change: (String) -> Unit,
        val onAddressLine2Change: (String) -> Unit,
        val onCityChange: (String) -> Unit,
        val onStateChange: (String) -> Unit,
        val onPostalCodeChange: (String) -> Unit,
        val onNextClick: () -> Unit,
        val onBackClick: () -> Unit
    )
}