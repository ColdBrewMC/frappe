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
