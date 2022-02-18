[![](https://jitpack.io/v/hallisonoliveira/morphine.svg)](https://jitpack.io/#hallisonoliveira/morphine)

# Morphine
Kotlin Kodein Module's generator

## Why?
We where Dagger users, for a couple of reasons, we decided to migrate to [Kodein](https://github.com/Kodein-Framework/Kodein-DI) as our DI, but manually migrating and creating all injection binds for a big project was a total no-go. To make our lifes easier we created Morphine, it does code generation of classes and constructors annotated with `javax.inject` annotations, its a Kodein companion for a Dagger-like experience.

## Configuring Morphine in Gradle for each Gradle Module do:

```gradle
dependencies {
    implementation("io.vithor.libs:morphine-api:$morphineVersion")
    // There are 2 version of the codegen, one for each Kodein counterpart
    kapt("io.vithor.libs:morphine-generic-codegen:$morphineVersion")
    // or
    kapt("io.vithor.libs:morphine-erased-codegen:$morphineVersion")
}

kapt {
    arguments {
        arg("MAIN_PACKAGE", "your.module.package.sample")
    }
}
```

## Acessing generated modules in code:
```kotlin
import your.module.package.sample.allModules  // Your sample module package defined earlier 
                                              // in each module's "MAIN_PACKAGE"
import org.kodein.di.DI

val sampleModule = DI.Module("My Sample Module") {
    import(allModules)
}
```
