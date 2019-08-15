package io.vithor.libs.morphine

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class RetrofitInjector(val importOnce: Boolean = false)