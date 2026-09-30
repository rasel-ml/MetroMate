package app.mrm.metromate

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import app.mrm.metromate.di.appModule
import app.mrm.metromate.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MRTApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (isDebug) {
            Napier.base(DebugAntilog())
        }

        startKoin {
            androidContext(this@MRTApp)
            modules(appModule, platformModule)
        }
    }
}
