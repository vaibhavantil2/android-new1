plugins {
  id("hedvig.android.ktlint")
  id("hedvig.android.library")
  id("hedvig.android.library.compose")
  alias(libs.plugins.apollo)
  alias(libs.plugins.serialization)
  alias(libs.plugins.squareSortDependencies)
}

dependencies {
  apolloMetadata(projects.apolloOctopusPublic)

  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.arrow.core)
  implementation(libs.apollo.normalizedCache)
  implementation(libs.apollo.runtime)
  implementation(libs.compose.richtext)
  implementation(libs.compose.richtextUi)
  implementation(libs.coil.coil)
  implementation(libs.coil.compose)
  implementation(libs.coroutines.android)
  implementation(libs.coroutines.core)
  implementation(libs.koin.compose)
  implementation(libs.koin.core)
  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlinx.serialization.core)
  implementation(projects.apolloCore)
  implementation(projects.apolloOctopusPublic)
  implementation(projects.coreBuildConstants)
  implementation(projects.coreDesignSystem)
  implementation(projects.coreCommonPublic)
  implementation(projects.coreIcons)
  implementation(projects.coreResources)
  implementation(projects.coreUi)
  implementation(projects.dataContractPublic)
  implementation(projects.featureFlagsPublic)
  implementation(projects.moleculeAndroid)
  implementation(projects.moleculePublic)
  implementation(projects.navigationComposeTyped)
  implementation(projects.navigationCore)
  implementation(projects.uiEmergency)
}

apollo {
  service("octopus") {
    packageName.set("octopus")
    generateDataBuilders.set(true)
  }
}

