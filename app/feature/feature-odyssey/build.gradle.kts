@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id("hedvig.android.library")
  id("hedvig.android.library.compose")
  id("hedvig.android.ktlint")
  id("kotlin-parcelize")
  alias(libs.plugins.serialization)
  alias(libs.plugins.squareSortDependencies)
}

dependencies {
  implementation(projects.apolloCore)
  implementation(projects.apolloOctopusPublic)
  implementation(projects.audioPlayer)
  implementation(projects.coreCommonPublic)
  implementation(projects.coreCommonAndroidPublic)
  implementation(projects.coreDatastorePublic)
  implementation(projects.coreDesignSystem)
  implementation(projects.coreResources)
  implementation(projects.coreUi)
  implementation(projects.coreUiData)
  implementation(projects.dataClaimFlow)
  implementation(projects.dataClaimTriaging)
  implementation(projects.navigationCore)
  implementation(projects.navigationComposeTyped)

  testImplementation(projects.coreCommonTest)

  implementation(libs.accompanist.permissions)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material3.windowSizeClass)
  implementation(libs.androidx.compose.materialIconsExtended)
  implementation(libs.androidx.compose.uiCore)
  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.androidx.navigation.common)
  implementation(libs.androidx.navigation.runtime)
  implementation(libs.androidx.other.activityCompose)
  implementation(libs.arrow.core)
  implementation(libs.coil.coil)
  implementation(libs.coil.compose)
  implementation(libs.koin.android)
  implementation(libs.koin.compose)
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.slimber)
  testImplementation(libs.assertK)
  testImplementation(libs.coroutines.test)
  testImplementation(libs.junit)
  testImplementation(libs.turbine)
}

android {
  namespace = "com.hedvig.android.feature.odyssey"
}
