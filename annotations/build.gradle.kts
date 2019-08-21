apply(plugin="java-library")
apply(plugin="kotlin")

group = "io.vithor.libs"
version = Versions.Libs.morphine
val archivesBaseName = "morphine-api"


dependencies {
    api("javax.inject:javax.inject:1")
    implementation(Dependencies.kotlin())
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

ext {
    set("packagingMode", "jar")
    set("PUBLISH_ARTIFACT_ID", archivesBaseName)
    set("libsDesc", "Annotations used by Morphine to code generate")
}

apply(from="../gradle/android-publish.gradle")