package io.vithor.libs.morphine.codegen.koin

import com.squareup.kotlinpoet.asTypeName
import io.vithor.libs.morphine.Tagged
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror


// TODO: Migrate to KotlinPoet
class KotlinKoinModuleBuilder(
    override val singleton: Boolean,
    override val superClassTypeName: String,
    override val className: String,
    override val packageName: String,
    override val originClassName: String,
    private val constructorElm: ExecutableElement?,
    private val processingEnv: ProcessingEnvironment
) : ModuleBuilder {
    override val qualifiedName: String = "$packageName.$className"

    override val groupName = "${superClassTypeName}_Modules"

    override val groupQualifiedName = "$packageName.$groupName"

    private val contentTemplate = simpleProviderModule(
        isViewModel(),
        singleton,
        className,
        packageName,
        originClassName,
        genConstructor(constructorElm)
    )

    private fun isViewModel(): Boolean {
        val serializable: TypeMirror =
            processingEnv.elementUtils.getTypeElement("androidx.lifecycle.ViewModel").asType()
        return processingEnv.typeUtils.isAssignable(
            constructorElm!!.enclosingElement.asType(),
            serializable
        )
    }

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

                if (tagged != null) injectionMethodTagged(
                    tagged
                )
                else INJECTION_METHOD_INVOKED
            }
            return@joinToString "/* $description */ $injection"
        }
    }

    override fun getContent(): String = contentTemplate
}

