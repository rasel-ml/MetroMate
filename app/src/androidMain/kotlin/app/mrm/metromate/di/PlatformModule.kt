package app.mrm.metromate.di

import app.mrm.metromate.database.DatabaseProvider
import app.mrm.metromate.utils.CsvFileWriter
import app.mrm.metromate.utils.FileSharer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule =
    module {
        single { DatabaseProvider(androidContext()).getDatabase() }
        single { FileSharer(androidContext()) }
        factory { CsvFileWriter(androidContext()) }
    }
