package io.github.alaksion.invoicer.features.company.di

import io.github.alaksion.invoicer.features.company.data.datasource.CompanyRemoteDataSource
import io.github.alaksion.invoicer.features.company.data.datasource.CompanyRemoteDataSourceImpl
import io.github.alaksion.invoicer.features.company.data.repository.CompanyRepositoryImpl
import io.github.alaksion.invoicer.features.company.domain.repository.CompanyRepository
import io.github.alaksion.invoicer.features.company.presentation.model.CreateCompanyFormManager
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.address.CompanyAddressScreenModel
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.info.CompanyInfoScreenModel
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.intermediary.IntermediaryPayInfoScreenModel
import io.github.alaksion.invoicer.features.company.presentation.screens.create.steps.payaccount.primary.PrimaryPayInfoScreenModel
import io.github.alaksion.invoicer.features.company.presentation.screens.select.SelectCompanyScreenModel
import io.github.alaksion.invoicer.foundation.session.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val companyDiModule = module {
    factory<CompanyRemoteDataSource> {
        CompanyRemoteDataSourceImpl(
            dispatcher = Dispatchers.IO,
            httpWrapper = get()
        )
    }

    factory<CompanyRepository> {
        CompanyRepositoryImpl(
            dataSource = get()
        )
    }

    factory<SelectCompanyScreenModel> {
        SelectCompanyScreenModel(
            dispatcher = Dispatchers.Default,
            repository = get(),
            session = Session
        )
    }

    factory {
        CompanyInfoScreenModel(
            form = get<CreateCompanyFormManager>().getForm()
        )
    }

    factory {
        CompanyAddressScreenModel(
            form = get<CreateCompanyFormManager>().getForm()
        )
    }

    factory {
        PrimaryPayInfoScreenModel(
            form = get<CreateCompanyFormManager>().getForm()
        )
    }

    factory {
        IntermediaryPayInfoScreenModel(
            form = get<CreateCompanyFormManager>().getForm()
        )
    }

    single<CreateCompanyFormManager> {
        CreateCompanyFormManager()
    }
}