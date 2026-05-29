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
include(
	"frappe-base",
	"frappe-ext-material",
	"frappe-ext-quad-view",
	"frappe-ext-render-pipeline",
	"frappe-ext-terrain-material",
)

include("mocha")
