package io.vithor.morphine.sample

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import io.vithor.morphine.sample.gen.allModules
import io.vithor.morphine.sample.ui.main.Xablau
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

class App : Application(), DIAware {
    override val di = DI.lazy {
        import(androidXModule(this@App))

        bindSingleton <ViewModelProvider.Factory> { KodeinViewModelFactory(di) }

        bindProvider(tag = "OTO") { Xablau("Nomeado") }

        import(allModules)
    }
}