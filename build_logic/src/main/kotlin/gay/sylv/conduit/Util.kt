package gay.sylv.conduit

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.conduit(module: String): ProjectDependency {
	return dependencies.project(":conduit-$module", configuration = "default")
}

fun DependencyHandlerScope.module(module: String, include: Boolean = false, api: Boolean = true): Dependency? {
	if (include) {
		add("include", conduit(module))
	}

	return if (!api) {
		// Ensure dependents don't get unwanted TAWs or extension classes
		add("implementation", conduit(module))
	} else {
		add("api", conduit(module))
	}
}

fun DependencyHandlerScope.extension(extension: String, include: Boolean = false, api: Boolean = false): Dependency? {
	return module("ext-$extension", include, api)
}
