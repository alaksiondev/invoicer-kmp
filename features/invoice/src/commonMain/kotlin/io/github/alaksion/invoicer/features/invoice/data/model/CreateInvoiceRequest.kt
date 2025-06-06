package io.github.alaksion.invoicer.features.invoice.data.model

import io.github.alaksion.invoicer.features.invoice.domain.model.CreateInvoiceModel
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateInvoiceRequest(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val beneficiaryId: String,
    val intermediaryId: String?,
    val activities: List<CreateInvoiceActivityRequest> = listOf()
)

@Serializable
internal data class CreateInvoiceActivityRequest(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)

internal fun CreateInvoiceModel.toDataModel(): CreateInvoiceRequest = CreateInvoiceRequest(
    externalId = this.externalId,
    senderCompanyName = this.senderCompanyName,
    senderCompanyAddress = this.senderCompanyAddress,
    recipientCompanyName = this.recipientCompanyName,
    recipientCompanyAddress = this.recipientCompanyAddress,
    issueDate = this.issueDate,
    dueDate = this.dueDate,
    beneficiaryId = this.beneficiaryId,
    intermediaryId = this.intermediaryId,
    activities = this.activities.map {
        CreateInvoiceActivityRequest(
            description = it.description,
            unitPrice = it.unitPrice,
            quantity = it.quantity
        )
    }
)
