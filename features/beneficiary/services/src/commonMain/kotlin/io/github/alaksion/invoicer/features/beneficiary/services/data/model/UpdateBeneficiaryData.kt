package io.github.alaksion.invoicer.features.beneficiary.services.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateBeneficiaryData(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)
