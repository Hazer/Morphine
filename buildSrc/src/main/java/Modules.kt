@file:Suppress("MemberVisibilityCanBePrivate", "unused")

object Modules {
    const val sampleKodein = ":sample"
    const val sampleKoin = ":sample-koin"

    const val annotations = ":annotations"
    const val coreKodein = ":codegen"
    const val core = coreKodein
    const val coreKoin = ":codegen-koin"
    const val erased = ":erased-codegen"
    const val generic = ":generic-codegen"
}
