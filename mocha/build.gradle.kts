import gay.sylv.frappe.extension
import gay.sylv.frappe.module

plugins {
	id("frappe.child")
}

base.archivesName = "mocha"

dependencies {
	module("base")
	extension("material")
	extension("terrain-material")
	extension("quad-view")
	extension("render-pipeline")
}

// FIXME: awful hack, turn this into a task
afterEvaluate {
	val frpFolder = projectDir.resolve("src").resolve("testmodClient").resolve("resources").resolve("assets").resolve("mocha-testmod").resolve("shaders").resolve("frp")
	println(projectDir)

	for (proj in rootProject.allprojects) {
		if (!proj.name.startsWith("frappe-ext-")) {
			continue
		}

		val shaderFormatsFile = proj.projectDir.resolve("src").resolve("client").resolve("resources").resolve("shader-formats")
		if (!shaderFormatsFile.exists() || !shaderFormatsFile.isDirectory) {
			println(shaderFormatsFile)
			continue
		}

		for (file in shaderFormatsFile.listFiles()) {
			if (file.extension != "vsh" && file.extension != "fsh") {
				continue
			}

			file.copyTo(frpFolder.resolve(file.name.replace("frp-", "")), true)
		}
	}
}
