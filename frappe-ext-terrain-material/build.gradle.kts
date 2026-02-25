import gay.sylv.frappe.extension
import gay.sylv.frappe.module

plugins {
	id("frappe.mod")
}

base.archivesName = "frappe-ext-terrain-material"

dependencies {
	module("base")
	extension("quad-view", api = true)
}
