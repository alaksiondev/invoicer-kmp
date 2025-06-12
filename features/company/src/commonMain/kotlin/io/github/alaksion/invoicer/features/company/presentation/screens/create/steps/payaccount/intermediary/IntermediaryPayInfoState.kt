package io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.intermediary

internal data class IntermediaryPayInfoState(
    val iban: String = "",
    val swift: String = "",
    val bankName: String = "",
    val bankAddress: String = "",
) {
    val buttonEnabled: Boolean =
        iban.isNotBlank() &&
                swift.isNotBlank() &&
                bankName.isNotBlank() &&
                bankAddress.isNotBlank()
}