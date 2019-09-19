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
//    override val kodein = Kodein.lazy {
//        import(androidXModule(this@App))
//
//        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein) }
//
//        bind<Xablau>(tag = "OTO") with provider { Xablau("Nomeado") }
//
//        import(allModules)
//    }

    val appModule = module {
        factory<Xablau>(named("OTO")) { Xablau("Nomeado") }
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