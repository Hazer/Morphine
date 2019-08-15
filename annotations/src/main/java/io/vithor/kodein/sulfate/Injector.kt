package io.vithor.kodein.sulfate

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Injector(val importOnce: Boolean = false)