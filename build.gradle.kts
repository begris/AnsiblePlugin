import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    jacoco
    id("org.jetbrains.intellij.platform") version "2.18.1"
    alias(libs.plugins.sonarqube)
}

group = "ir.msdehghan"
version = "1.2.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    testImplementation(libs.junit)
    intellijPlatform {
        intellijIdea("2025.3.5")

        bundledPlugins("org.jetbrains.plugins.yaml", "com.intellij.modules.json")
        testFramework(TestFrameworkType.Platform)
    }
    compileOnly(project(":plugin-doc-extractor"))
}

val docsProject = project(":plugin-doc-extractor")

tasks.register<Sync>("syncPluginDocs") {
    dependsOn(":plugin-doc-extractor:generateDocs")
    from(docsProject.layout.buildDirectory.dir("ansible-plugins/docs"))
    into(layout.buildDirectory.dir("resources/main/docs"))
}

tasks.register<Copy>("copyPluginsMetadata") {
    dependsOn(":plugin-doc-extractor:generateDocs")
    from(docsProject.layout.buildDirectory.dir("ansible-plugins/plugins.json"))
    into(layout.buildDirectory.dir("resources/main"))
}

tasks.named("processResources") {
    dependsOn(tasks.named("syncPluginDocs"), tasks.named("copyPluginsMetadata"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "243"
        }

        intellijPlatform {
            buildSearchableOptions = false
            pluginVerification {
                ides {
                    recommended()
                }
            }
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "MSDehghan_AnsiblePlugin")
        property("sonar.organization", "msdehghan")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks {
    runIde {
        argumentProviders += CommandLineArgumentProvider {
            listOf("/home/bbeier/wrk/config/infrastructure")
        }
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
        }
    }
    sonar.get().dependsOn(jacocoTestReport)
}
