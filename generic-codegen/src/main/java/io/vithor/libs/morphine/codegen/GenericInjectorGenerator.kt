package io.vithor.kodein.sulfate.codegen

import com.google.auto.service.AutoService
import io.vithor.libs.morphine.codegen.InjectorGenerator
import javax.annotation.processing.Processor
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(InjectorGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class GenericInjectorGenerator : InjectorGenerator(isKodeinErased = false)