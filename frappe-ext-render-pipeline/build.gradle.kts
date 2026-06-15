import gay.sylv.frappe.extension
import gay.sylv.frappe.module

plugins {
	id("frappe.mod")
}

base.archivesName = "frappe-ext-render-pipeline"

dependencies {
	module("base")
	extension("quad-view")

	include(libs.antlr)
	include(libs.glsl.transformer)
	implementation(libs.glsl.transformer)
}
