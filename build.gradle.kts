import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

var SFTLIBRARY = "";
var SFTDEVELOPMENT = "";

var MAINCLASSNAME = "com.dleyva.app.Main"

if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows()) {
    SFTLIBRARY = ".";
    SFTDEVELOPMENT = ".";
}
else {
    SFTLIBRARY = System.getenv("HOME")+"/SFTlibrary";
    SFTLIBRARY = System.getenv("HOME")+"/SFTdevelopment";
}

plugins {
    java
    jacoco
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

repositories {
    jcenter()
}

dependencies {
    implementation(files(SFTDEVELOPMENT+"/library/CoreServices.jar"))
    implementation(files(SFTDEVELOPMENT+"/library/LogServices.jar"))
    implementation(files(SFTDEVELOPMENT+"/library/AppObjects.jar"))
    
    implementation("com.google.guava:guava:27.1-android")
    implementation("org.slf4j:slf4j-api:1.7.21");
    implementation("com.jcraft:jsch:0.1.55");
    implementation("org.freemarker:freemarker:2.3.30");
    implementation("com.google.code.gson:gson:2.8.6");
    implementation("org.codehaus.janino:janino:3.1.2");
    implementation("org.codehaus.janino:commons-compiler:3.1.2");
    implementation("info.picocli:picocli:4.3.2");
    implementation("org.codehaus.groovy:groovy-all:2.4.7");
    implementation("org.slf4j:slf4j-api:1.7.21");
    implementation("com.fasterxml.jackson.core:jackson-core:2.8.10");
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.8.10");
    implementation("com.fasterxml.jackson.core:jackson-databind:2.8.10");
    implementation("com.google.code.gson:gson:2.8.5");
    implementation("io.netty:netty-all:4.1.50.Final");

    logback(
        "core",
        "classic",
        "access"
    ).forEach { implementation(files(it)) }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
}

description = "Sample Application"
group = "sft"

application {
    mainClassName = MAINCLASSNAME
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("${project.name}")
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Implementation-Title"] = "${project.description}"
        attributes["Implementation-Version"] = archiveVersion.get()
        attributes["Main-Class"] = MAINCLASSNAME
    }
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))    
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}

fun logback(
    vararg modules: String
): List<String> = modules.map { module ->
    SFTLIBRARY+"/library/logback-$module-1.3.0-SNAPSHPOT.jar"
}
