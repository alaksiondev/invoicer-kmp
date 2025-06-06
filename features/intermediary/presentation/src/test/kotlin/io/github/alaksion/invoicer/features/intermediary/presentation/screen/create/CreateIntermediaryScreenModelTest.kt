package io.github.alaksion.invoicer.features.intermediary.presentation.screen.create

import io.github.alaksion.invoicer.features.intermediary.presentation.fakes.FakeIntermediaryRepository
import io.github.alaksion.invoicer.foundation.watchers.RefreshIntermediaryPublisher
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
class CreateIntermediaryScreenModelTest {

    private lateinit var intermediaryRepository: FakeIntermediaryRepository
    private lateinit var refreshIntermediaryPublisher: RefreshIntermediaryPublisher
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreateIntermediaryScreenModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        refreshIntermediaryPublisher = RefreshIntermediaryPublisher()
        intermediaryRepository = FakeIntermediaryRepository()
        viewModel = CreateIntermediaryScreenModel(
            intermediaryRepository = intermediaryRepository,
            dispatcher = dispatcher,
            refreshIntermediaryPublisher = refreshIntermediaryPublisher
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

        assertEquals(CreateIntermediaryEvents.Success, viewModel.events.first())
    }

    @Test
    fun `should emit error event on submission failure`() = runTest {
        intermediaryRepository.createFails = true

        viewModel.submit()

        assertEquals(
            CreateIntermediaryEvents.Error(""),
            viewModel.events.first()
        )
    }
}
