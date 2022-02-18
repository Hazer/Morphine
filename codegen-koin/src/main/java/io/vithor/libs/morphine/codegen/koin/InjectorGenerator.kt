package io.vithor.libs.morphine.codegen.koin

import com.squareup.kotlinpoet.asTypeName
import io.vithor.libs.morphine.Injector
import io.vithor.libs.morphine.RetrofitInjector
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types


abstract class InjectorGenerator : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private lateinit var elementUtils: Elements
    private lateinit var typeUtils: Types
    private lateinit var messager: Messager

    private lateinit var imports: MutableMap<String, MutableList<ModuleBuilder>>

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        messager = processingEnv.messager

        imports = mutableMapOf()


    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            Injector::class.java.name,
            RetrofitInjector::class.java.name,
            Inject::class.java.name,
            Named::class.java.name
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {

        roundEnvironment?.getElementsAnnotatedWith(Inject::class.java)?.forEach {
            when (it.kind) {
                ElementKind.CLASS -> {
                    val className = it.simpleName.toString()
                    val typeEl = it as TypeElement

                    generateForClass(typeEl, it, className)
                }
                ElementKind.CONSTRUCTOR -> {
                    val typeEl = (it.enclosingElement as TypeElement)
                    val className = typeEl.simpleName.toString()
                    generateForClass(typeEl, typeEl, className)
                    // generateForConstructor(it)
                }
            }
        }

        roundEnvironment?.getElementsAnnotatedWith(Injector::class.java)
            ?.forEach {
                val className = it.simpleName.toString()
                val typeEl = it as TypeElement

                generateForClass(typeEl, it, className)
            }

        roundEnvironment?.getElementsAnnotatedWith(RetrofitInjector::class.java)
            ?.forEach {
                val className = it.simpleName.toString()
                val typeEl = it as TypeElement

                // typeEl.enclosingElement

                val superClassTypeName = "RetrofitResource"

                val pack = processingEnv.elementUtils.getPackageOf(it).toString()

                val importOnce: Boolean =
                    typeEl.getAnnotation(RetrofitInjector::class.java)?.importOnce ?: false

                val singleton = typeEl.getAnnotation(Singleton::class.java) != null

                generateModuleOfAPI(superClassTypeName, singleton, className, pack)
            }

        if (roundEnvironment!!.processingOver()) {
            // TODO: Find a way to generate AllInjectors instance in the root package.
            generateAllModules()
        }

        return true
    }

    private fun generateForClass(
        typeEl: TypeElement,
        element: Element,
        className: String
    ) {
        // typeEl.enclosingElement

        // Gets superclass without generics info
        val superClassTypeName =
            typeEl.superclass.asTypeName().toString()
                .substringBefore('<') // Remove generics info
                .substringAfterLast('.') // Remove package info

        // Gets constructor with less arguments
        val constructorElm = element.enclosedElements
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .map { it as ExecutableElement }
            .minByOrNull { it.parameters.size }

        val pack = processingEnv.elementUtils.getPackageOf(element).toString()

        val importOnce: Boolean = typeEl.getAnnotation(Injector::class.java)?.importOnce ?: false

        val singleton = typeEl.getAnnotation(Singleton::class.java) != null

        generateModuleOfClass(
            superClassTypeName,
            singleton,
            className,
            pack,
            constructorElm
        )
    }

    // TODO: Migrate to KotlinPoet
    private fun generateAllModules() {
        val mainPackageName = processingEnv.options.get("MAIN_PACKAGE")

        val commonPackage = mainPackageName
            ?: throw IllegalStateException("Module package must not be empty, generation failed")

        val injector = allModulesTemplate(
            commonPackage,
            imports,
            ::generateModuleOfClassModules
        )

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val path = commonPackage.replace('.', '/')

        val pathFile = File(kaptKotlinGeneratedDir, path)
        pathFile.mkdirs()
//        val finalPath = if (pathFile.exists()) pathFile else File(kaptKotlinGeneratedDir)
        val finalPath = pathFile

        val file =
            File(finalPath, "${commonPackage.replace('.', '_')}_AllModules.kt")

        file.writeText(injector)
    }

    private fun generateModuleOfClassModules(key: String) {
        val imports = imports[key] ?: return

        val first = imports.first()

        val groupName = first.groupName

        val commonPackage = imports.map { it.packageName }.fold("") { acc, next ->
            if (acc.isBlank()) return@fold next

            if (next.isBlank()) throw IllegalStateException("Module package must not be empty, generation failed")

            return@fold acc.takeWhileIndexed { index, char ->
                next.getOrNull(index) == char
            }
        }.removeSuffix(".")

        val packageName = commonPackage//first.packageName

        val groupQualifiedName = key

        val injector = groupModuleTemplate(
            packageName,
            groupName,
            imports
        )

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val path = groupQualifiedName.substringBeforeLast('.')
            .replace('.', '/')
            .replace('_', '/')

        val pathFile = File(kaptKotlinGeneratedDir, path)
        pathFile.mkdirs()
//        val finalPath = if (pathFile.exists()) pathFile else File(kaptKotlinGeneratedDir)
        val finalPath = pathFile

        val file = File(finalPath, "${groupQualifiedName.replace('.', '_')}.kt")
        file.writeText(injector)
    }


    private fun generateModuleOfClass(
        superClassTypeName: String,
        singleton: Boolean,
        originalClassName: String,
        pack: String,
        constructorElm: ExecutableElement?
    ) {
        val newClassName = "${originalClassName}_Module"

        val generatedClass = KotlinKoinModuleBuilder(
            singleton,
            superClassTypeName,
            newClassName,
            pack,
            originalClassName,
            constructorElm,
            processingEnv
        )

        val actual = imports.getOrPut(generatedClass.groupQualifiedName) {
            mutableListOf()
        }

        if (actual.none { it.qualifiedName == generatedClass.qualifiedName }) {
            actual.add(generatedClass)
        }

        imports[generatedClass.groupQualifiedName] = actual

        val fileContent = generatedClass.getContent()

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val path = pack.replace('.', '/').replace('_', '/')

        val pathFile = File(kaptKotlinGeneratedDir, path)
        pathFile.mkdirs()
//        val finalPath = if (pathFile.exists()) pathFile else File(kaptKotlinGeneratedDir)
        val finalPath = pathFile

        val file = File(finalPath, "${pack.replace('.', '_')}_$newClassName.kt")

        file.writeText(fileContent)
    }


    private fun generateModuleOfAPI(
        superClassTypeName: String,
        singleton: Boolean,
        originalClassName: String,
        pack: String
    ) {
        val newClassName = "${originalClassName}_Module"

        val generatedClass = RetrofitModuleBuilder(
            singleton,
            superClassTypeName,
            newClassName,
            pack,
            originalClassName
        )

        val actual = imports.getOrPut(generatedClass.groupQualifiedName) {
            mutableListOf()
        }

        if (actual.none { it.qualifiedName == generatedClass.qualifiedName }) {
            actual.add(generatedClass)
        }

        imports[generatedClass.groupQualifiedName] = actual

        val fileContent = generatedClass.getContent()

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]

        val path = pack.replace('.', '/').replace('_', '/')

        val pathFile = File(kaptKotlinGeneratedDir, path)
        pathFile.mkdirs()
//        val finalPath = if (pathFile.exists()) pathFile else File(kaptKotlinGeneratedDir)
        val finalPath = pathFile

        val file = File(finalPath, "${pack.replace('.', '_')}_$newClassName.kt")

        file.writeText(fileContent)
    }
}