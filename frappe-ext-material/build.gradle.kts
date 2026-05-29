import gay.sylv.frappe.module
import gay.sylv.frappe.extension

plugins {
	id("frappe.mod")
}

base.archivesName = "frappe-ext-material"

dependencies {
	module("base")
	extension("render-pipeline", api = true)
}
