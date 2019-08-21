apply(plugin="java-library")
apply(plugin="kotlin")
apply(plugin="kotlin-kapt")

group = "io.vithor.libs"
version = Versions.Libs.morphine
val archivesBaseName = "morphine-abs-codegen"

repositories {
    google()
    mavenCentral()
    jcenter()
    maven(url="https://kotlin.bintray.com/kotlinx")
}

dependencies {
    kapt(project(Modules.Morphine.annotations))
    compileOnly(project(Modules.Morphine.annotations))

    implementation("com.squareup:kotlinpoet:1.2.0")

    implementation(Dependencies.kotlin())
    kapt(Dependencies.kotlin())
    implementation(Dependencies.kodeinCore())
    kapt(Dependencies.kodeinCore())

    // configuration generator for service providers
    implementation("com.google.auto.service:auto-service:1.0-rc5")
    kapt("com.google.auto.service:auto-service:1.0-rc5")
}

configure<JavaPluginConvention> {
    sourceSets {
        getByName("main") {
            java {
                srcDir("${buildDir.absolutePath}/tmp/kapt/main/kotlinGenerated/")
            }
        }
    }

    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

ext {
    set("packagingMode", "jar")
    set("PUBLISH_ARTIFACT_ID", archivesBaseName)
    set("libsDesc", "Morphine's Abstract Codegen compiler")
}

apply(from="../gradle/android-publish.gradle")