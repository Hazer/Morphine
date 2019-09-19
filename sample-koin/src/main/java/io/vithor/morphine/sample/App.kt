package io.vithor.morphine.sample

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import io.vithor.morphine.sample.gen.allModules
import io.vithor.morphine.sample.ui.main.Xablau
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class App : Application() {

    val appModule = module {
        factory(named("OTO")) { Xablau("Nomeado") }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(allModules + appModule)
        }
    }
}