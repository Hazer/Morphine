package io.vithor.kodein.sulfate

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class RetrofitInjector(val importOnce: Boolean = false)