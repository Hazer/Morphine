@file:Suppress("MemberVisibilityCanBePrivate", "unused", "NOTHING_TO_INLINE")

object Dependencies {
    fun kotlin() =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Libs.kotlin}"

    fun kodeinCore() =
        plus("org.kodein.di:kodein-di", Versions.Libs.kodein, "core-jvm")

    fun kodeinGenerics() =
        plus("org.kodein.di:kodein-di", Versions.Libs.kodein, "generic-jvm")

    fun kodeinErased() =
        plus("org.kodein.di:kodein-di", Versions.Libs.kodein, "erased-jvm")
}

@PublishedApi
internal inline fun plus(base: String, version: String, extra: String?) =
    "${(if (extra == null) base else "$base-$extra")}:$version"