package io.vithor.morphine.sample.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Xablau @Inject constructor(val aow: String = "Works")

class MainViewModel @Inject constructor(val testArg: Xablau, @Named("OTO") val namedArg: Xablau) : ViewModel() {

    init {
        Log.d("InjectorTest", "Injected ${testArg.aow}")
    }
}