pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

includeBuild("build_logic")
include("conduit-base", "conduit-ext-custom-chunk-layer", "conduit-ext-fabric-renderer")

include("cerise")
