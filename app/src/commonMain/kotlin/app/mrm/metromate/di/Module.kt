package app.mrm.metromate.di

import com.russhwolf.settings.Settings
import app.mrm.metromate.database.AppDatabase
import app.mrm.metromate.repository.SettingsRepository
import app.mrm.metromate.repository.TransactionRepository
import app.mrm.metromate.settings.createSettings
import app.mrm.metromate.ui.screens.farecalculator.FareCalculatorViewModel
import app.mrm.metromate.ui.screens.history.HistoryScreenState
import app.mrm.metromate.ui.screens.history.HistoryScreenViewModel
import app.mrm.metromate.ui.screens.home.MainScreenAction
import app.mrm.metromate.ui.screens.home.MainScreenState
import app.mrm.metromate.ui.screens.home.MainScreenViewModel
import app.mrm.metromate.ui.screens.more.MoreScreenViewModel
import app.mrm.metromate.ui.screens.stationmap.StationMapViewModel
import app.mrm.metromate.ui.screens.transactionlist.TransactionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect val platformModule: org.koin.core.module.Module

val appModule =
    module {
        single<Settings> { createSettings() }
        single { SettingsRepository(get()) }
        single { get<AppDatabase>().getCardDao() }
        single { get<AppDatabase>().getScanDao() }
        single { get<AppDatabase>().getTransactionDao() }
        single {
            TransactionRepository(
                cardDao = get(),
                scanDao = get(),
                transactionDao = get(),
            )
        }

        viewModel { parameters ->
            TransactionListViewModel(
                cardIdm = parameters.get(),
                transactionRepository = get(),
                csvFileWriter = get(),
            )
        }

        viewModel {
            HistoryScreenViewModel(
                transactionRepository = get(),
            )
        }

        factory {
            FareCalculatorViewModel()
        }

        factory {
            HistoryScreenState()
        }

        viewModel {
            MoreScreenViewModel(
                settingsRepository = get(),
            )
        }

        factory {
            MainScreenState()
        }

        viewModel {
            MainScreenViewModel(
                transactionRepository = get(),
                initialState = get(),
                settingsRepository = get(),
            ).apply {
                onAction(MainScreenAction.OnInit)
            }
        }

        viewModel {
            StationMapViewModel(
                settingsRepository = get(),
            )
        }
    }
