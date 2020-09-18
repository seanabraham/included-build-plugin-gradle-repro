package com.example

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class BuildPlugin : Plugin<Project> {
    override fun apply(project: Project) {
      println("Hello, world! Applying to ${project.name}")
    }
}
