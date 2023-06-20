// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.hilt) apply false
    id("com.github.ben-manes.versions") version "0.47.0"
    id("nl.littlerobots.version-catalog-update") version "0.8.0"
}
true // Needed to make the Suppress annotation work for the plugins block