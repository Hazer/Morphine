package io.vithor.libs.morphine.codegen

import com.squareup.kotlinpoet.asTypeName
import io.vithor.libs.morphine.Tagged
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.lang.model.element.ExecutableElement

// TODO: Migrate to KotlinPoet
class KotlinKodeinModuleBuilder(
    override val erased: Boolean,
    override val importOnce: Boolean,
    override val singleton: Boolean,
    override val superClassTypeName: String,
    override val className: String,
    override val packageName: String,
    override val originClassName: String,

    private val constructorElm: ExecutableElement?,
    private val processingEnv: ProcessingEnvironment
) : ModuleBuilder {
    override val qualifiedName: String = "$packageName.$className"

    override val groupName = "${superClassTypeName}_Injectors"

    override val groupQualifiedName = "$packageName.${superClassTypeName}_Injectors"

    private val contentTemplate = simpleProviderModule(
        erased,
        singleton,
        className,
        packageName,
        originClassName,
        genConstructor(constructorElm)
    )

    // TODO: Migrate to KotlinPoet
    private fun genConstructor(constructorElm: ExecutableElement?): String {
        val args = constructorElm?.parameters
        if (args?.isEmpty() != false) return ""

        return args.joinToString(separator = ", ") {
            val variableAsElement = processingEnv.typeUtils.asElement(it.asType())
            val description = "${it.simpleName}: ${variableAsElement.asType().asTypeName()}"

            val named = it.getAnnotation(Named::class.java)

            val injection: String

            injection = if (named != null) {
                injectionMethodNamed(named)
            } else {
                val tagged = it.getAnnotation(Tagged::class.java)

                if (tagged != null) injectionMethodTagged(tagged)
                else INJECTION_METHOD_INVOKED
            }
            return@joinToString "/* $description */ $injection"
        }
    }

    override fun getContent(): String = contentTemplate
}

