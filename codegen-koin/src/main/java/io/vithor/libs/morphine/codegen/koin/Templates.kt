@file:Suppress("NOTHING_TO_INLINE")

package io.vithor.libs.morphine.codegen.koin

import io.vithor.libs.morphine.Tagged
import javax.inject.Named

const val INJECTION_METHOD = "get"
// TODO: Migrate to KotlinPoet
const val INJECTION_METHOD_INVOKED = "$INJECTION_METHOD()"

// TODO: Migrate to KotlinPoet
inline fun simpleProviderModule(
    isViewModel: Boolean,
    isSingleton: Boolean,
    className: String,
    packageName: String,
    originClassName: String,
    constructorArgs: String
): String {
    return """
        package $packageName

        import org.koin.core.qualifier.named
        import org.koin.dsl.module
        ${if (isViewModel) "import org.koin.androidx.viewmodel.dsl.viewModel" else ""}
        
        import $packageName.$originClassName

        val $className = module {
            ${if (isViewModel) "viewModel" else provision(isSingleton)}<$originClassName>() { $originClassName($constructorArgs) }
        }

    """.trimIndent()
}

inline fun provision(isSingleton: Boolean) =
    if (isSingleton) "single" else "factory"

inline fun retrofitFactoryModule(
    className: String,
    packageName: String,
    originClassName: String
): String {
    return """
        package $packageName

        import org.koin.core.qualifier.named
        import org.koin.dsl.module
        import retrofit2.Retrofit

        import $packageName.$originClassName

        val $className = module {
            factory<$originClassName> { retrofit: Retrofit ->
                retrofit.create($originClassName::class.java)
            }
        }

    """.trimIndent()
}

// TODO: Migrate to KotlinPoet
inline fun injectionMethodTagged(tagged: Tagged) = """$INJECTION_METHOD(named("${tagged.tag}"))"""

// TODO: Migrate to KotlinPoet
inline fun injectionMethodNamed(named: Named) = """$INJECTION_METHOD(named("${named.value}"))"""

// TODO: Migrate to KotlinPoet
inline fun groupModuleTemplate(
    packageName: String,
    groupName: String,
    imports: MutableList<ModuleBuilder>
): String {
    return """
                package $packageName

                import org.koin.dsl.module

                val $groupName = listOf(
                    ${imports.joinToString(",\n                        ") { it.qualifiedName }}
                )

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

                import org.koin.core.module.Module
                import org.koin.dsl.module

                val allModules = listOf<Module>() + 
                    ${imports.keys.onEach(generateModule).joinToString(" +\n                    ")}

            """.trimIndent()
}