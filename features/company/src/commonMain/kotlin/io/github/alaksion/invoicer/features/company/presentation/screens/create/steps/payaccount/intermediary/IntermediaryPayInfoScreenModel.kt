package io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.intermediary

import cafe.adriel.voyager.core.model.ScreenModel
import io.github.alaksion.invoicer.features.company.presentation.model.CreateCompanyForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class IntermediaryPayInfoScreenModel(
    private val form: CreateCompanyForm
) : ScreenModel {

    private val _state = MutableStateFlow(IntermediaryPayInfoState())
    val state = _state.asStateFlow()

    fun resumeState() {
        form.intermediaryPayAccount?.let {
            _state.update {
                it.copy(
                    iban = form.primaryPayAccount.iban,
                    swift = form.primaryPayAccount.swift,
                    bankName = form.primaryPayAccount.bankName,
                    bankAddress = form.primaryPayAccount.bankAddress,
                )
            }
        }
    }

    fun updateSwift(value: String) {
        _state.update {
            it.copy(swift = value)
        }
    }

    fun updateIban(value: String) {
        _state.update {
            it.copy(iban = value)
        }
    }

    fun updateAddress(value: String) {
        _state.update {
            it.copy(bankAddress = value)
        }
    }

    fun updateBankName(value: String) {
        _state.update {
            it.copy(
                bankName = value
            )
        }
    }

    fun submit() {
        form.intermediaryPayAccount = CreateCompanyForm.IntermediaryAccountInfo(
            iban = _state.value.iban,
            swift = state.value.swift,
            bankName = state.value.bankName,
            bankAddress = state.value.bankAddress,
        )
    }
}