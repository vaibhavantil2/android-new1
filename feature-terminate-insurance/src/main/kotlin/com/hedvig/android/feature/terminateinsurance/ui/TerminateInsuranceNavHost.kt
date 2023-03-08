package com.hedvig.android.feature.terminateinsurance.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.hedvig.android.core.designsystem.material3.motion.MotionDefaults
import com.hedvig.android.feature.terminateinsurance.InsuranceId
import com.hedvig.android.feature.terminateinsurance.navigation.Destinations
import com.hedvig.android.feature.terminateinsurance.navigation.terminateInsuranceGraph
import com.kiwi.navigationcompose.typed.createRoutePattern

@Composable
internal fun TerminateInsuranceNavHost(
  windowSizeClass: WindowSizeClass,
  navController: NavHostController,
  insuranceId: InsuranceId,
  navigateUp: () -> Boolean,
  finishTerminationFlow: () -> Unit,
) {
  val density = LocalDensity.current
  AnimatedNavHost(
    navController = navController,
    enterTransition = { MotionDefaults.sharedXAxisEnter(density) },
    exitTransition = { MotionDefaults.sharedXAxisExit(density) },
    popEnterTransition = { MotionDefaults.sharedXAxisPopEnter(density) },
    popExitTransition = { MotionDefaults.sharedXAxisPopExit(density) },
    startDestination = createRoutePattern<Destinations.TerminateInsurance>(),
  ) {
    terminateInsuranceGraph(
      windowSizeClass = windowSizeClass,
      navController = navController,
      insuranceId = insuranceId,
      navigateUp = navigateUp,
      finishTerminationFlow = finishTerminationFlow,
    )
  }
}