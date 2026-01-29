import gay.sylv.conduit.extension
import gay.sylv.conduit.module

plugins {
	id("conduit.child")
}

base.archivesName = "condium"

dependencies {
	module("base")
	extension("custom-chunk-layer")
	extension("fabric-renderer")
}
