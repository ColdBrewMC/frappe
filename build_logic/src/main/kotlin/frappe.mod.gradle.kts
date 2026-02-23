import org.gradle.accessors.dm.LibrariesForLibs

plugins {
	id("frappe.all")
}

val mod_version: String by rootProject
val maven_group: String by rootProject
val mod_id: String by rootProject
val mod_license: String by rootProject

version = mod_version
group = maven_group

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

loom {
	mods {
		register(mod_id) {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
			sourceSet(sourceSets["test"])
		}
	}
}

tasks {
	withType<AbstractArchiveTask> {
		from("LICENSE") {
			rename { "${it}_${mod_id}"}
		}

		from("COPYING") {
			rename { "${it}_${mod_id}"}
		}

		from("COPYING.LESSER") {
			rename { "${it}_${mod_id}"}
		}

		filesMatching("*.kra") {
			exclude()
		}
	}

	val expandProps = mapOf(
		"maven_group" to maven_group,
		"mod_id" to mod_id,
		"mod_version" to mod_version,
		"mod_license" to mod_license
	)

	processResources {
		inputs.property("version", version)

		filesMatching(listOf("fabric.mod.json", "*.mixins.json")) {
			expand(expandProps)
		}

		exclude("*.classtweaker")
	}
}
