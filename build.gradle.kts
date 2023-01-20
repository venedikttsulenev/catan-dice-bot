import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

application {
    project.setProperty("mainClassName", "catanDiceBot.bot.MainKt")
}

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("com.github.johnrengelman.shadow") version "6.0.0"
}


group = "venediktts"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.elbekD:kt-telegram-bot:2.1.8")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(ShadowJar::class) {
    archiveFileName.set("catan-dice-bot-${archiveVersion.get()}.${archiveExtension.get()}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}