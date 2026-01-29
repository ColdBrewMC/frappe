import org.gradle.accessors.dm.LibrariesForLibs

plugins {
	id("dev.yumi.gradle.licenser")
	id("net.fabricmc.fabric-loom")
	`maven-publish`
	checkstyle
}

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.

	mavenCentral()

	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
	}

	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/")
	}
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft(libs.minecraft)
	implementation(libs.fabric.loader)
	implementation(libs.fabric.api)
}

loom {
	runtimeOnlyLog4j = true

	// Split sources is best practice in modern Minecraft versions
	splitEnvironmentSourceSets()

	if (project !== rootProject) {
		accessWidenerPath = file("src/client/resources/${base.archivesName.get()}.classtweaker")
	}

	sourceSets {
		register("testmodClient") {
			compileClasspath += sourceSets["main"].compileClasspath
			runtimeClasspath += sourceSets["main"].runtimeClasspath
			compileClasspath += sourceSets["client"].compileClasspath
			runtimeClasspath += sourceSets["client"].runtimeClasspath
		}

		getByName("test") {
			compileClasspath += sourceSets["testmodClient"].compileClasspath
			runtimeClasspath += sourceSets["testmodClient"].runtimeClasspath
		}
	}

	runs {
		getByName("server") {
			ideConfigGenerated(false)
		}
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	// If this mod is a library, it should generate javadocs.
	// This line generates javadocs for the mod.
	withJavadocJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks {
	javadoc {
		(options as StandardJavadocDocletOptions)
			.tags(
				"apiNote:a:API Note:",
				"implSpec:a:Implementation Requirements:",
				"implNote:a:Implementation Note:"
			)
	}

	withType<JavaCompile> {
		options.release.set(25)
	}

	build {
//		dependsOn(tasks.applyLicenses)
	}
}

if (project !== rootProject) {
	license {
		var file = file("../LHEADER")
		if (!file.exists()) {
			file = file("./LHEADER")
		}
		rule(file)
		exclude("**/*.json")
		exclude("**/*.fsh")
		exclude("**/*.vsh")
		exclude("**/*.gsh")
		exclude("**/*.tsh")
		exclude("**/*.csh")
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = base.archivesName.get()
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
