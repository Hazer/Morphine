package io.vithor.libs.morphine.codegen

interface ModuleBuilder {
    val erased: Boolean
    val importOnce: Boolean
    val superClassTypeName: String
    val className: String
    val packageName: String
    val originClassName: String

    val qualifiedName: String
    val groupName: String
    val groupQualifiedName: String

    fun getContent(): String
}