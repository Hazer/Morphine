package io.vithor.morphine.sample

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import io.vithor.morphine.sample.gen.allModules
import io.vithor.morphine.sample.ui.main.Xablau
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton

class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))

        bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(kodein) }

        bind<Xablau>(tag = "OTO") with provider { Xablau("Nomeado") }

        import(allModules)
    }
}