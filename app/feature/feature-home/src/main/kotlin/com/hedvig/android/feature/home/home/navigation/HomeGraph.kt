package com.hedvig.android.feature.home.home.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import com.hedvig.android.core.designsystem.material3.motion.MotionDefaults
import com.hedvig.android.feature.home.home.ui.HomeDestination
import com.hedvig.android.feature.home.home.ui.HomeViewModel
import com.hedvig.android.navigation.core.AppDestination
import com.hedvig.android.navigation.core.HedvigDeepLinkContainer
import com.hedvig.android.navigation.core.TopLevelGraph
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.kiwi.navigationcompose.typed.navigation
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeGraph(
  nestedGraphs: NavGraphBuilder.() -> Unit,
  hedvigDeepLinkContainer: HedvigDeepLinkContainer,
  onStartChat: (NavBackStackEntry) -> Unit,
  onStartClaim: (NavBackStackEntry) -> Unit,
  navigateToClaimDetails: (NavBackStackEntry, claimId: String) -> Unit,
  navigateToPayinScreen: () -> Unit,
  navigateToMissingInfo: (NavBackStackEntry, String) -> Unit,
  navigateToHelpCenter: (NavBackStackEntry) -> Unit,
  openAppSettings: () -> Unit,
  openUrl: (String) -> Unit,
) {
  navigation<TopLevelGraph.HOME>(
    startDestination = createRoutePattern<AppDestination.TopLevelDestination.Home>(),
    deepLinks = listOf(
      navDeepLink { uriPattern = hedvigDeepLinkContainer.home },
    ),
  ) {
    composable<AppDestination.TopLevelDestination.Home>(
      enterTransition = { MotionDefaults.fadeThroughEnter },
      exitTransition = { MotionDefaults.fadeThroughExit },
    ) { backStackEntry ->
      val viewModel: HomeViewModel = koinViewModel()
      HomeDestination(
        viewModel = viewModel,
        onStartChat = { onStartChat(backStackEntry) },
        onClaimDetailCardClicked = { claimId: String ->
          navigateToClaimDetails(backStackEntry, claimId)
        },
        navigateToConnectPayment = navigateToPayinScreen,
        onStartClaim = { onStartClaim(backStackEntry) },
        navigateToMissingInfo = { contractId -> navigateToMissingInfo(backStackEntry, contractId) },
        navigateToHelpCenter = { navigateToHelpCenter(backStackEntry) },
        openUrl = openUrl,
        openAppSettings = openAppSettings,
      )
    }
    nestedGraphs()
  }
}
