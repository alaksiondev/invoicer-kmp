package io.github.alaksion.invoicer.features.company.presentation.model

import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatform.getKoin

class CreateCompanyForm {
    var companyName: String = ""
    var companyDocument: String = ""

    var addressLine1: String = ""
    var addressLine2: String = ""
    var city: String = ""
    var state: String = ""
    var postalCode: String = ""
    var countryCode: String = ""

    var primaryPayAccount = PayAccountInfo(
        iban = "",
        swift = "",
        bankName = "",
        bankAddress = "",
        shouldContinueToIntermediary = false
    )

    var intermediaryPayAccount: IntermediaryAccountInfo? = null

    data class PayAccountInfo(
        val iban: String,
        val swift: String,
        val bankName: String,
        val bankAddress: String,
        val shouldContinueToIntermediary: Boolean
    )

    data class IntermediaryAccountInfo(
        val iban: String,
        val swift: String,
        val bankName: String,
        val bankAddress: String,
    )
}

internal class CreateCompanyFormManager {

    fun closeScope() {
        val scope = getKoin().getScopeOrNull(ScopeName)
        scope?.close()
    }

    fun getForm(): CreateCompanyForm {
        val currentScope = getKoin().getScopeOrNull(ScopeName)

        if (currentScope == null) {
            val newScope = getKoin().createScope(ScopeName, ScopeQualifier)
            newScope.declare(CreateCompanyForm())
            return newScope.get<CreateCompanyForm>()
        }

        return currentScope.get<CreateCompanyForm>()
    }

    companion object {
        const val ScopeName = "CreateCompanyFlowScope"
        val ScopeQualifier = named("CreateCompanyFlowScope")
    }
}