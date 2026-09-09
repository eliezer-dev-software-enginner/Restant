import java.util.Properties

plugins {
    id("java")
    id("application")

    id("org.openjfx.javafxplugin") version "0.1.0"
    id("com.gradleup.shadow") version "8.3.5"
}

val props = Properties()
file("gradle.properties").inputStream().use { props.load(it) }

val appPatchNumber = props.getProperty("appPatch", "0").toIntOrNull() ?: 0
val fullVersion = if (appPatchNumber > 0) "${props.getProperty("appVersion")}.$appPatchNumber" else props.getProperty("appVersion")

group = "everest"
version = fullVersion

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

javafx {
    version = "25.0.1"
    modules("javafx.controls", "javafx.graphics")
}

dependencies {

    // Megalodonte framework
    implementation("megalodonte:megalodonte-base:1.0.0-beta")
    implementation("megalodonte:megalodonte-components:1.0.0-beta")
    implementation("megalodonte:megalodonte-reactivity:1.0.0-beta")
    implementation("megalodonte:megalodonte-router:1.0.0-beta")
    implementation("megalodonte:megalodonte-theme:1.0.0-beta")

    // JSON (replaces old Jackson 2.9.3)
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // SQLite (replaces old 3.21.0.1)
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Rich text editor with syntax highlighting
    implementation("org.fxmisc.richtext:richtextfx:0.11.0")

    // Icons (Ikonli)
    implementation("org.kordamp.ikonli:ikonli-core:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-antdesignicons-pack:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-entypo-pack:12.4.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.13.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    val os = System.getProperty("os.name").lowercase()

    val javafxModulesHome = System.getenv("JAVAFX_MODULES_HOME")
        ?: throw GradleException(
            "Environment variable JAVAFX_MODULES_HOME not defined. " +
                    "Set it to the folder containing linux-25.0.1/ and windows-25.0.1/."
        )

    val fxPath = if (os.contains("win")) {
        "$javafxModulesHome/windows-25.0.1/lib"
    } else {
        "$javafxModulesHome/linux-25.0.1/lib"
    }

    val jvmArgsList = mutableListOf(
        "--module-path", fxPath,
        "--add-modules", "javafx.controls,javafx.graphics",
        "--enable-native-access=ALL-UNNAMED"
    )

    if (System.getenv("DEV_MODE") == "true") {
        jvmArgsList.add("-Dprism.verbose=true")
    }

    jvmArgs = jvmArgsList

    environment("DEV_MODE", "true")
}

application {
    mainClass.set(props.getProperty("appMainClass"))
}

tasks.shadowJar {
    dependsOn(tasks.test)
    archiveBaseName.set(props.getProperty("appName"))
    archiveClassifier.set("")
    mergeServiceFiles()

    manifest {
        attributes(
            "Main-Class" to props.getProperty("appMainClass")
        )
    }

    exclude("org/openjfx/**")
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
}

tasks.jar {
    enabled = false
}
