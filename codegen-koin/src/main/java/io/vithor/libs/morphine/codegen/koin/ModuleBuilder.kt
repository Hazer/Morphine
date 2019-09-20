package io.vithor.libs.morphine.codegen.koin

interface ModuleBuilder {
    val singleton: Boolean
    val superClassTypeName: String
    val className: String
    val packageName: String
    val originClassName: String

    val qualifiedName: String
    val groupName: String
    val groupQualifiedName: String

    fun getContent(): String
}