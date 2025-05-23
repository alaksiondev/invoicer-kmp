package io.github.alaksion.invoicer.features.beneficiary.presentation.screen.create

import io.github.alaksion.invoicer.features.beneficiary.presentation.fakes.FakeBeneficiaryRepository
import io.github.alaksion.invoicer.foundation.watchers.RefreshBeneficiaryPublisher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateBeneficiaryScreenModelTest {

    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var refreshBeneficiaryPublisher: RefreshBeneficiaryPublisher
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreateBeneficiaryScreenModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        refreshBeneficiaryPublisher = RefreshBeneficiaryPublisher()
        beneficiaryRepository = FakeBeneficiaryRepository()
        viewModel = CreateBeneficiaryScreenModel(
            beneficiaryRepository = beneficiaryRepository,
            dispatcher = dispatcher,
            refreshBeneficiaryPublisher = refreshBeneficiaryPublisher
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update name`() {
        viewModel.updateName("John Doe")
        assertEquals("John Doe", viewModel.state.value.name)
    }

    @Test
    fun `should update swift`() {
        viewModel.updateSwift("SWIFT123")
        assertEquals("SWIFT123", viewModel.state.value.swift)
    }

    @Test
    fun `should update iban`() {
        viewModel.updateIban("IBAN123")
        assertEquals("IBAN123", viewModel.state.value.iban)
    }

    @Test
    fun `should update bank address`() {
        viewModel.updateBankAddress("123 Bank St")
        assertEquals("123 Bank St", viewModel.state.value.bankAddress)
    }

    @Test
    fun `should update bank name`() {
        viewModel.updateBankName("Bank Name")
        assertEquals("Bank Name", viewModel.state.value.bankName)
    }

    @Test
    fun `should emit success event on successful submission`() = runTest {
        viewModel.updateName("John Doe")
        viewModel.updateSwift("SWIFT123")
        viewModel.updateIban("IBAN123")
        viewModel.updateBankAddress("123 Bank St")
        viewModel.updateBankName("Bank Name")

        viewModel.submit()

        assertEquals(CreateBeneficiaryEvents.Success, viewModel.events.first())
    }

    @Test
    fun `should emit error event on submission failure`() = runTest {
        beneficiaryRepository.createFails = true

        viewModel.submit()

        assertEquals(
            CreateBeneficiaryEvents.Error(""),
            viewModel.events.first()
        )
    }
}
