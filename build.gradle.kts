// Top-level build file where you can add configuration options common to all sub-projects/modules.
//apply plugin: "com.github.ben-manes.versions"

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kodein-framework/Kodein-DI/")
        maven(url = "https://dl.bintray.com/hazer/maven")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Libs.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.Libs.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${Versions.Libs.kotlin}")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        // classpath "com.github.ben-manes:gradle-versions-plugin:${Versions.versionsChecker}"
        // classpath 'com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.6'

        // classpath "org.jlleitschuh.gradle:ktlint-gradle:7.4.0"

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kodein-framework/Kodein-DI/")
        maven(url = "https://dl.bintray.com/hazer/maven")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
    }
}

tasks.getByName<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
