import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import java.net.HttpURLConnection
import java.net.URL

plugins {
    kotlin("jvm") version "1.8.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.palantir.git-version") version "0.12.3"
    id("dev.s7a.gradle.minecraft.server") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jmailen.kotlinter") version "3.8.0"
}

val gitVersion: Closure<String> by extra

val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

dependencies {
    shadowImplementation(kotlin("stdlib"))
    compileOnly("org.spigotmc:spigot-api:$pluginVersion-R0.1-SNAPSHOT")
}

configure<BukkitPluginDescription> {
    main = "@group@.Main"
    version = pluginVersion
    apiVersion = "1." + pluginVersion.split(".")[1]
    author = "@author@"
    website = "@website@"
    /*
    コマンド追加用
    commands {
        register("test") {
            description = "This is a test command!"
            aliases = listOf("t")
            permission = "testplugin.test"
            usage = "Just run the command!"
        }
    }
    */
}

tasks.withType<ShadowJar> {
    configurations = listOf(shadowImplementation)
    archiveClassifier.set("")
    relocate("kotlin", "@group@.libs.kotlin")
    relocate("org.intellij.lang.annotations", "@group@.libs.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "@group@.libs.org.jetbrains.annotations")
}

tasks.named("build") {
    dependsOn("shadowJar")
    // プラグインを特定のパスへ自動コピー
    val copyFilePath = "M:/TwitterServer/plugins/" // コピー先のフォルダーパス
    val copyFile = File(copyFilePath)
    if (copyFile.exists() && copyFile.isDirectory) {
        doFirst {
            copy {
                from(buildDir.resolve("libs/${project.name}.jar"))
                into(copyFile)
            }
        }
        doLast { // AutomaticCreatingPluginUpdate連携
            // APIリクエストを行う
            val port = 25585
            val ip = "192.168.0.21"
            val apiUrl = "http://$ip:$port/plugin?name=${project.name}"
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connect()

                // レスポンスコードを確認
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    println("API Response: $response")
                } else {
                    println("Failed to get response: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error during API request: ${e.message}")
            } finally {
                connection.disconnect()
            }
        }
    }
}

task<SetupTask>("setup")
