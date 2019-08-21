apply(plugin="com.github.dcendents.android-maven")
apply(plugin="com.jfrog.bintray")

val pub_artifactId
        get() = project.ext.get("PUBLISH_ARTIFACT_ID")
val siteUrl
    get() = "https://github.com/Hazer/Morphine"
val gitUrl
    get() = "https://github.com/Hazer/Morphine.git"
val pkgOpts
    get() = project.ext.get("packagingMode")
val libsDesc
    get() = project.ext.get("libsDesc")


tasks.withType<Upload> {
this.
    install {
        repositories.mavenInstaller {
            // This generates POM.xml with proper parameters
            pom {
                project {
                    packaging pkgOpts
                            name pub_artifactId
                            description libsDesc
                            url siteUrl

                            licenses {
                                license {
                                    name 'The Apache Software License, Version 2.0'
                                    url 'https://github.com/Hazer/Morphine/blob/master/LICENSE'
                                }
                            }
                    developers {
                        developer {
                            id 'hazer'
                            name 'Vithorio Polten'
                        }
                    }
                    scm {
                        connection "scm:git:git://github.com/Hazer/Morphine.git"
                        developerConnection "scm:git:ssh://github.com:Hazer/Morphine.git"
                        url "https://github.com/Hazer/Morphine"
                    }
                }
            }
        }
    }
}


if (project.getPlugins().hasPlugin('com.android.application') ||
        project.getPlugins().hasPlugin('com.android.library')) {
    task androidJavadocs(type: Javadoc) {
        failOnError false
        source = android.sourceSets.main.java.sourceFiles
        classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
    }

    task androidJavadocsJar(type: Jar, dependsOn: androidJavadocs) {
        archiveClassifier.set('javadoc')
        from androidJavadocs.destinationDir
    }

    task androidSourcesJar(type: Jar) {
        from android.sourceSets.main.java.srcDirs
        archiveClassifier.set('sources')
    }
} else {
    task sourcesJar(type: Jar, dependsOn:classes) {
        archiveClassifier.set('sources')
        from sourceSets.main.allSource
    }

    task javadocJar(type: Jar, dependsOn:javadoc) {
        archiveClassifier.set('javadoc')
        from javadoc.destinationDir
    }
}

if (JavaVersion.current().isJava8Compatible()) {
    allprojects {
        tasks.withType(Javadoc) {
            options.addStringOption('Xdoclint:none', '-quiet')
        }
    }
}

artifacts {
    if (project.getPlugins().hasPlugin('com.android.application') ||
            project.getPlugins().hasPlugin('com.android.library')) {
        archives androidSourcesJar
        archives androidJavadocsJar
    } else {
        archives sourcesJar
        archives javadocJar
    }
}

Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())

bintray {
    user = System.getenv('BINTRAY_USER')
    key = System.getenv('BINTRAY_KEY')
    configurations = ['archives']
    pkg {
        repo = 'maven'
        name = pub_artifactId
        websiteUrl = siteUrl
        vcsUrl = gitUrl
        licenses = ["Apache-2.0"]
        publish = true
        publicDownloadNumbers = true
    }
}