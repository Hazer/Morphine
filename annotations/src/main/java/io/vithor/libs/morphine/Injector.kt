package io.vithor.libs.morphine

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Injector(val importOnce: Boolean = false)