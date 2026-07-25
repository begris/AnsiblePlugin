plugins {
    java
}

repositories{
    mavenCentral()
}

dependencies {
    implementation(libs.jakson)
}


java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.register<JavaExec>("generateDocs") {
    dependsOn(tasks.jar)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("ExtractDoc")
    args = listOf(pluginSourceFile.asFile.absolutePath,
        "build/ansible-plugins/plugins.json", "build/ansible-plugins/docs")
}

tasks.named("generateDocs") {
    dependsOn(tasks.named("fetchAnsiblePlugins"))
    onlyIf { tasks.named("build").get().state.failure == null }
}

tasks.named("jar") {
    finalizedBy(tasks.named("generateDocs"))
}

fun isToolAvailable(tool: String): Boolean {
    return try {
        ProcessBuilder("which", tool)
            .redirectErrorStream(true)
            .start()
            .waitFor() == 0
    } catch (e: Exception) {
        false
    }
}

fun String.shellQuote(): String {
    val metaChars = " \t\n\"'`\\$&;|<>(){}[]*?!~#"
    val sb = StringBuilder()
    for (c in this) {
        if (c in metaChars) sb.append('\\')
        sb.append(c)
    }
    return sb.toString()
}

val pluginSourceFile = layout.settingsDirectory.file("ansible-plugins/plugins-source.json")
val pluginSourceDocumentationFolder = layout.settingsDirectory.file("ansible-plugins/plugins")
tasks.register<Exec>("fetchAnsiblePlugins") {
    group = "ansible"
    description = "Fetches Ansible plugin docs via ansible-doc/jq, if both tools are available"
    val outputFile = pluginSourceFile
    val outputDocumentationFolder = pluginSourceDocumentationFolder


    doFirst {
        mkdir(outputDocumentationFolder)
        mkdir("ansible-plugins/plugins")
    }

    commandLine(
        "bash", "-c",
        """
        shopt -s nullglob;
        echo "${outputDocumentationFolder.asFile.absolutePath.shellQuote()}   ----    ${outputFile.asFile.absolutePath}";
        ansible-doc -lj | jq 'keys[] | .' | xargs -P 16 -I {} sh -c 'echo "Retrieving plugin {}"; ansible-doc -j {} > ${outputDocumentationFolder.asFile.absolutePath.shellQuote()}/{}.json'
        jq -n '[inputs] | add' ${outputDocumentationFolder.asFile.absolutePath.shellQuote()}/*.json > "${outputFile.asFile.absolutePath}"
        """.trimIndent()
    )
    outputs.file(outputFile)

    onlyIf {
        if (outputFile.asFile.exists()) {
            logger.lifecycle("Skipping fetchAnsiblePlugins — ${outputFile.asFile.name} already exists. Run 'cleanAnsiblePlugins' to force a refresh.")
            return@onlyIf false
        }
        val available = isToolAvailable("ansible-doc") && isToolAvailable("jq")
        if (!available) {
            logger.lifecycle("Skipping fetchAnsiblePlugins — ansible-doc and/or jq not found on PATH.")
        }
        available
    }
}

tasks.register<Delete>("cleanAnsiblePlugins") {
    group = "ansible"
    description = "Deletes the generated plugins-source.json so the next build re-fetches it"
    delete(pluginSourceFile, pluginSourceDocumentationFolder)
}

/* Manual changes:
1- Remove import_playbook from modules list and it's file.
2- Change type of 'mode' field in copy module to raw.
3- Remove "free-form" from module fields in thier files.
 */
