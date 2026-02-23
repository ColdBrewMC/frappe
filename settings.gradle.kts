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
include("frappe-base", "frappe-ext-terrain-material", "frappe-ext-quad-view")

include("cerise")
