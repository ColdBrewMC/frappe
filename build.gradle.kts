import gay.sylv.frappe.extension
import gay.sylv.frappe.module

plugins {
	id("frappe.mod")
}

val mod_id: String by project

base.archivesName = mod_id

dependencies {
	module("base", include = true)
	extension("quad-view", include = true)
	extension("terrain-material", include = true)
	module("mocha", include = true, api = false, prefix = false)
}
