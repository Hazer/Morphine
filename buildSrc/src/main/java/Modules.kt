@file:Suppress("MemberVisibilityCanBePrivate", "unused")

object Modules {
    const val sample = ":sample"

    object Morphine {
        const val annotations = ":annotations"
        const val core = ":codegen"
        const val erased = ":erased-codegen"
        const val generic = ":generic-codegen"
    }

    const val annotations = Morphine.annotations
    const val core = Morphine.core
    const val erased = Morphine.erased
    const val generic = Morphine.generic
}
