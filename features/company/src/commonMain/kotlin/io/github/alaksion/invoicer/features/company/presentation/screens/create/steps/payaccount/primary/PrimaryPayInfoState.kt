package io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.primary

internal data class PrimaryPayInfoState(
    val iban: String = "",
    val swift: String = "",
    val bankName: String = "",
    val bankAddress: String = "",
    val shouldGoToIntermediary: Boolean = true,
) {
    val buttonEnabled: Boolean =
        iban.isNotBlank() &&
                swift.isNotBlank() &&
                bankName.isNotBlank() &&
                bankAddress.isNotBlank()
}