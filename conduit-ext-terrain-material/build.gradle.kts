import gay.sylv.conduit.extension
import gay.sylv.conduit.module

plugins {
	id("conduit.mod")
}

base.archivesName = "conduit-ext-custom-chunk-render-layer"

dependencies {
	module("base")
	extension("quad-view")
}
