package com.hedvig.app.feature.profile.ui.tab

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.hedvig.android.core.designsystem.material3.motion.MotionDefaults
import com.hedvig.android.navigation.compose.typed.animatedComposable
import com.hedvig.android.navigation.compose.typed.animatedNavigation
import com.hedvig.android.navigation.core.AppDestination
import com.hedvig.android.navigation.core.TopLevelGraph
import com.hedvig.app.feature.profile.ui.eurobonus.EurobonusDestination
import com.hedvig.app.feature.profile.ui.eurobonus.EurobonusViewModel
import com.kiwi.navigationcompose.typed.createRoutePattern
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.profileGraph(
  navController: NavController,
) {
  animatedNavigation<TopLevelGraph.PROFILE>(
    startDestination = createRoutePattern<AppDestination.TopLevelDestination.Profile>(),
  ) {
    animatedComposable<AppDestination.TopLevelDestination.Profile>(
      enterTransition = { MotionDefaults.fadeThroughEnter },
      exitTransition = { MotionDefaults.fadeThroughExit },
    ) {
      val viewModel: ProfileViewModel = koinViewModel()
      ProfileDestination(
        navController = navController,
        viewModel = viewModel,
      )
    }
    animatedComposable<AppDestination.Eurobonus> {
      val euroBonus = this
      val viewModel: EurobonusViewModel = koinViewModel { parametersOf(euroBonus) }
      EurobonusDestination(
        viewModel = viewModel,
        navigateUp = navController::navigateUp,
      )
    }
  }
}
