package com.hedvig.android.navigation.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.hedvig.android.core.common.android.tryOpenPlayStore

class ActivityNavigator(
  private val application: Application,
  private val loggedOutActivityClass: Class<*>,
  private val buildConfigApplicationId: String,
  private val navigateToChat: Context.() -> Unit,
  private val navigateToEmbark: Context.(storyName: String, storyTitle: String) -> Unit,
  private val navigateToLoggedInActivity: Context.(clearBackstack: Boolean) -> Unit,
) {
  fun navigateToMarketingActivity() {
    application.startActivity(
      Intent(application, loggedOutActivityClass)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
    )
  }

  fun navigateToChat(context: Context) {
    context.navigateToChat()
  }

  @Suppress("DEPRECATION")
  fun openAppSettings(context: Context) {
    val permissionActivity = Intent(
      Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
      Uri.parse("package:$buildConfigApplicationId"),
    )
    if (context.packageManager.resolveActivity(permissionActivity, 0) != null) {
      context.startActivity(permissionActivity)
      return
    }
    context.startActivity(Intent(Intent(Settings.ACTION_SETTINGS)))
  }

  fun navigateToEmbark(
    context: Context,
    storyName: String,
    storyTitle: String,
  ) {
    context.navigateToEmbark(storyName, storyTitle)
  }

  fun navigateToLoggedInScreen(
    context: Context,
    clearBackstack: Boolean = true,
  ) {
    context.navigateToLoggedInActivity(clearBackstack)
  }

  fun tryOpenPlayStore(context: Context) {
    context.tryOpenPlayStore()
  }
}