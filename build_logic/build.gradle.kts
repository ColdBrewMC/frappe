plugins {
	`kotlin-dsl`
}

repositories {
	gradlePluginPortal()

	maven {
		url = uri("https://maven.fabricmc.net/")
	}
}

dependencies {
	// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
	implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

	implementation("net.fabricmc:fabric-loom:${libs.plugins.fabric.loom.get().version}")
	implementation("dev.yumi:yumi-gradle-licenser:${libs.plugins.yumi.gradle.licenser.get().version}")

	// FIXME: why in the ever living fuck do dependencies not dependency
	runtimeOnly("net.fabricmc.fabric-api:fabric-api:${libs.fabric.api.asProvider().get().version}")
	compileOnly("net.fabricmc.fabric-api:fabric-renderer-api-v1:${libs.fabric.api.renderer.get().version}")
	runtimeOnly("io.github.douira:glsl-transformer:${libs.glsl.transformer.get().version}")
}
