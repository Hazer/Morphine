package io.vithor.kodein.sulfate.codegen

class RetrofitModuleBuilder(override val erased: Boolean,
                                override val importOnce: Boolean,
                                override val superClassTypeName: String,
                                override val className: String,
                                override val packageName: String,
                                override val originClassName: String
) : ModuleBuilder {
    override val qualifiedName: String = "$packageName.$className"

    override val groupName = "${superClassTypeName}_Injectors"

    override val groupQualifiedName = "$packageName.${superClassTypeName}_Injectors"

    private val contentTemplate = retrofitFactoryModule(
        erased,
        className,
        packageName,
        originClassName
    )

    override fun getContent(): String = contentTemplate
}