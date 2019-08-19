@file:Suppress("NOTHING_TO_INLINE")

package io.vithor.libs.morphine.codegen

import io.vithor.libs.morphine.Tagged
import javax.inject.Named

const val INJECTION_METHOD = "instance"
// TODO: Migrate to KotlinPoet
const val INJECTION_METHOD_INVOKED = "$INJECTION_METHOD()"

// TODO: Migrate to KotlinPoet
inline fun simpleProviderModule(
    erased: Boolean = true,
    isSingleton: Boolean,
    className: String,
    packageName: String,
    originClassName: String,
    constructorArgs: String
): String {
    val kodeinType = if (erased) "erased" else "generic"
    return """
        package $packageName

        import org.kodein.di.Kodein
        import org.kodein.di.$kodeinType.bind
        import org.kodein.di.$kodeinType.instance
        import org.kodein.di.$kodeinType.${provision(isSingleton)}

        import $packageName.$originClassName

        val $className = Kodein.Module(prefix = "$packageName", name = "$originClassName") {
            bind<$originClassName>() with ${provision(isSingleton)} { $originClassName($constructorArgs) }
        }

    """.trimIndent()
}

inline fun provision(isSingleton: Boolean) =
    if (isSingleton) "singleton" else "provider"

inline fun factoryProvision(isMultiton: Boolean) =
    if (isMultiton) "multiton" else "factory"

inline fun retrofitFactoryModule(
    erased: Boolean = true,
    isMultiton: Boolean,
    className: String,
    packageName: String,
    originClassName: String
): String {
    val kodeinType = if (erased) "erased" else "generic"
    return """
        package $packageName

        import org.kodein.di.Kodein
        import org.kodein.di.$kodeinType.bind
        import org.kodein.di.$kodeinType.${factoryProvision(isMultiton)}
        import retrofit2.Retrofit

        import $packageName.$originClassName

        val $className = Kodein.Module(prefix = "$packageName", name = "$originClassName") {
            bind<$originClassName>() with ${factoryProvision(isMultiton)} { retrofit: Retrofit ->
                retrofit.create($originClassName::class.java)
            }
        }

    """.trimIndent()
}

// TODO: Migrate to KotlinPoet
inline fun injectionMethodTagged(tagged: Tagged) = """$INJECTION_METHOD(tag = "${tagged.tag}")"""

// TODO: Migrate to KotlinPoet
inline fun injectionMethodNamed(named: Named) = """$INJECTION_METHOD(tag = "${named.value}")"""

// TODO: Migrate to KotlinPoet
inline fun importMethod(module: ModuleBuilder): String = if (module.importOnce) "importOnce" else "import"

// TODO: Migrate to KotlinPoet
inline fun groupModuleTemplate(
    packageName: String,
    groupName: String,
    imports: MutableList<ModuleBuilder>
): String {
    return """
                package $packageName

                import org.kodein.di.Kodein

                val $groupName = Kodein.Module(prefix = "$packageName", name = "$groupName") {
                    ${imports.joinToString("\n                ") { "${importMethod(it)}(${it.qualifiedName})" }}
                }

            """.trimIndent()
}

// TODO: Migrate to KotlinPoet
inline fun allModulesTemplate(
    commonPackage: String,
    imports: MutableMap<String, MutableList<ModuleBuilder>>,
    generateModule: (key: String) -> Unit
): String {
    return """
                package $commonPackage

                import org.kodein.di.Kodein

                val allModules = Kodein.Module(prefix = "$commonPackage", name = "allModules") {
                    ${StringBuilder().apply {
        for (key in imports.keys) {
            generateModule(key)
            append("import(")
            append(key)
            appendln(")")
            append("                ")
        }
    }}
                }

            """.trimIndent()
}