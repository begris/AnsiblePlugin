import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
//    jacoco
//    id("org.sonarqube") version "3.0"
    id("org.jetbrains.intellij.platform")
//    id("org.jetbrains.intellij.platform.migration")
}

group = "ir.msdehghan"
version = "1.0.0-SNAPSHOT"

dependencies {
    intellijPlatform {
//        intellijIdeaCommunity("2024.3.1")
        intellijIdeaUltimate("2024.3")
        bundledPlugin("org.jetbrains.plugins.yaml")
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation("junit:junit:4.13.2")
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_20
//    targetCompatibility = JavaVersion.VERSION_20
//}

intellijPlatform {
    version = "2024.3"
    projectName = rootProject.name
//    setPlugins(listOf("yaml"))
    // Instrument if we are not in CI. It will make coverage wrong in tests
    instrumentCode = System.getenv("CI") == null
    pluginConfiguration {
    }
    pluginVerification {
        ides {
            ide(IntelliJPlatformType.IntellijIdeaUltimate, version = "2024.3")
            recommended()
            //project("/home/bjoern.beier/wrk/config/idea-plugin-test-amsible-project/infrastructure")
        }
    }
    //updateSinceUntilBuild = false
}

//sonarqube {
//    properties {
//        properties["sonar.projectKey"] = "MSDehghan_AnsiblePlugin"
//        properties["sonar.organization"] = "msdehghan-github"
//        properties["sonar.host.url"] = "https://sonarcloud.io"
//    }
//}
//
//tasks{
//    sonarqube.get().dependsOn(jacocoTestReport)
//    jacocoTestReport {
//        dependsOn(test)
//        reports {
//            xml.required = true
////            xml.isEnabled = true
//        }
//    }
//    buildSearchableOptions {
//        enabled = false
//    }
//}
