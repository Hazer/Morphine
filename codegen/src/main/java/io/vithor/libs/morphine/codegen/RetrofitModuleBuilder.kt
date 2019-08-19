package io.vithor.libs.morphine.codegen

class RetrofitModuleBuilder(
    override val erased: Boolean,
    override val importOnce: Boolean,
    override val singleton: Boolean,
    override val superClassTypeName: String,
    override val className: String,
    override val packageName: String,
    override val originClassName: String
) : ModuleBuilder {
    override val qualifiedName: String = "$packageName.$className"

    override val groupName = "${superClassTypeName}_Modules"

    override val groupQualifiedName = "$packageName.${superClassTypeName}_Modules"

    private val contentTemplate = retrofitFactoryModule(
        erased,
        singleton,
        className,
        packageName,
        originClassName
    )

    override fun getContent(): String = contentTemplate
}