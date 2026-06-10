package gay.sylv.frappe

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.frappe(module: String): ProjectDependency {
	return dependencies.project(":frappe-$module", configuration = "default")
}

fun DependencyHandlerScope.module(module: String, include: Boolean = false, api: Boolean = true, prefix: Boolean = true): Dependency? {
	val dependencyPrefixer = if (prefix) { this::frappe } else {
		module -> dependencies.project(module, configuration = "default")
	}
	val dependencyPrefixerMixinConfig = if (prefix) {
		module -> dependencies.project(":frappe-$module")
	} else {
		module: String -> dependencies.project(module)
	}

	if (include) {
		add("include", dependencyPrefixer(module))
	}

	add("mixinConfigImplementation", dependencyPrefixerMixinConfig(module)) {
		capabilities {
			requireFeature("mixin-config")
		}
	}

	return if (!api) {
		// Ensure dependents don't get unwanted TAWs or extension classes
		add("implementation", dependencyPrefixer(module))
	} else {
		add("api", dependencyPrefixer(module))
	}
}

fun DependencyHandlerScope.extension(extension: String, include: Boolean = false, api: Boolean = true): Dependency? {
	return module("ext-$extension", include = include, api = api)
}
