package io.vithor.morphine.sample.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Named

class Xablau @Inject constructor() {
    val aow = "Works"
}

class MainViewModel @Inject constructor(@Named("TAG") val testArg: Xablau) : ViewModel() {

    init {
        Log.d("InjectorTest", "Injected ${testArg.aow}")
    }
}