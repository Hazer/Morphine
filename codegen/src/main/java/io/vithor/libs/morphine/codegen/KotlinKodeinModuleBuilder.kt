package io.vithor.kodein.sulfate.codegen

import com.squareup.kotlinpoet.asTypeName
import io.vithor.kodein.sulfate.Tagged
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement

// TODO: Migrate to KotlinPoet
class KotlinKodeinModuleBuilder(
    override val erased: Boolean,
    override val importOnce: Boolean,
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

            val tagged = it.getAnnotation(Tagged::class.java)

            return@joinToString if (tagged != null)
                "/* $description */ ${injectionMethodTagged(tagged)}"
            else
                "/* $description */ $INJECTION_METHOD_INVOKED"
        }
    }

    override fun getContent(): String = contentTemplate
}

