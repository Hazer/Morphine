package io.vithor.morphine.sample

import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseViewModel<S, C> : ViewModel()

class NavDrawerSharedViewModel
@Inject constructor() : BaseViewModel<MySharedState, MySharedCommand>()

data class MySharedState(
    val loading: Boolean = false
)

sealed class MySharedCommand