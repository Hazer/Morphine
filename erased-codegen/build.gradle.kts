apply(plugin="java-library")
apply(plugin="kotlin")
apply(plugin="kotlin-kapt")

group = "io.vithor.libs"
version = Versions.Libs.morphine
val archivesBaseName = "morphine-erased-codegen"


dependencies {
    kapt(project(Modules.Morphine.annotations))
    compileOnly(project(Modules.Morphine.annotations))

    api(project(Modules.Morphine.core))

    implementation(Dependencies.kotlin())
    implementation(Dependencies.kodeinErased())

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
    set("libsDesc", "Morphine's Kodein Erased Codegen compiler")
}

apply(from="../gradle/android-publish.gradle")