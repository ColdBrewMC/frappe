package gay.sylv.frappe

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.frappe(module: String): ProjectDependency {
	return dependencies.project(":frappe-$module", configuration = "default")
}

fun DependencyHandlerScope.module(module: String, include: Boolean = false, api: Boolean = true, prefix: Boolean = true): Dependency? {
	val prefixer = if (prefix) { this::frappe } else {
		module -> dependencies.project(module, configuration = "default")
	}

	if (include) {
		add("include", prefixer(module))
	}

	return if (!api) {
		// Ensure dependents don't get unwanted TAWs or extension classes
		add("implementation", prefixer(module))
	} else {
		add("api", prefixer(module))
	}
}

fun DependencyHandlerScope.extension(extension: String, include: Boolean = false, api: Boolean = false): Dependency? {
	return module("ext-$extension", include, api)
}
