import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "venediktts"
val versionFile = File("src/main/resources/version.txt")
val gradlePropsFile = File("gradle.properties")

application {
    project.setProperty("mainClassName", "catanDiceBot.bot.MainKt")
}

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.elbekD:kt-telegram-bot:2.1.8")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.0")
}

tasks.register("writeVersion") {
    group = "build"
    doFirst {
        println("Writing version to ${versionFile.path}")
        versionFile.writeText(version.toString())
    }
}

tasks.register("incrementVersion") {
    group = "build"
    doFirst {
        val versionString = version.toString()
        val buildNumberStart = versionString.lastIndexOf('.') + 1
        val buildNumber = versionString.substring(buildNumberStart)
            .toInt()
            .plus(1)
        val versionExceptBuildNumber = versionString.substring(0, buildNumberStart)
        val newVersionString = versionExceptBuildNumber + buildNumber
        println("Incrementing version at ${gradlePropsFile.path}")
        gradlePropsFile.writeText(
            gradlePropsFile.readText()
                .replace("version=$versionString", "version=$newVersionString")
        )
    }
}

tasks.register("release") {
    group = "build"
    dependsOn("incrementVersion", "shadowJar")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(ShadowJar::class) {
    mustRunAfter("incrementVersion")
    archiveFileName.set("catan-dice-bot-${archiveVersion.get()}.${archiveExtension.get()}")
}

tasks.withType<KotlinCompile> {
    dependsOn("writeVersion")
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}