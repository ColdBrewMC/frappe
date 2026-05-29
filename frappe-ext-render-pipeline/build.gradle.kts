import gay.sylv.frappe.module

plugins {
	id("frappe.mod")
}

base.archivesName = "frappe-ext-render-pipeline"

dependencies {
	module("base")

	include(libs.glsl.transformer)
	implementation(libs.glsl.transformer)
}
