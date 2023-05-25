@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("hedvig.android.library")
  id("hedvig.android.ktlint")
  alias(libs.plugins.serialization)
}

dependencies {
  implementation(projects.app.core.common)
  implementation(projects.app.core.commonAndroid)
  implementation(projects.app.core.datastore)
  implementation(projects.app.hanalytics.hanalyticsCore)

  api(libs.hAnalytics)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.coroutines.core)
  implementation(libs.koin.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.okhttp.core)
  implementation(libs.slimber)
}

android {
  namespace = "com.hedvig.android.hanalytics.android"
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=com.hedvig.android.hanalytics.InternalHanalyticsApi"
  }
}