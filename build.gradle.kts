import gay.sylv.conduit.extension
import gay.sylv.conduit.module

plugins {
	id("conduit.mod")
}

val mod_id: String by project

base.archivesName = mod_id

dependencies {
	module("base", include = true)
	extension("fabric-renderer", api = true)
	extension("custom-chunk-layer")
}
