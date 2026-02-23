import gay.sylv.frappe.extension
import gay.sylv.frappe.module

plugins {
	id("frappe.child")
}

base.archivesName = "cerise"

dependencies {
	module("base")
	extension("terrain-material")
	extension("quad-view")
}
