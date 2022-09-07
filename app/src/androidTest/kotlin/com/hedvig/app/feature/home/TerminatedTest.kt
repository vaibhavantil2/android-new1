package com.hedvig.app.feature.home

import com.hedvig.android.apollo.graphql.HomeQuery
import com.hedvig.android.apollo.graphql.LoggedInQuery
import com.hedvig.app.R
import com.hedvig.app.feature.home.screens.HomeTabScreen
import com.hedvig.app.feature.home.screens.HonestyPledgeSheetScreen
import com.hedvig.app.feature.loggedin.ui.LoggedInActivity
import com.hedvig.app.testdata.feature.home.HOME_DATA_TERMINATED
import com.hedvig.app.testdata.feature.referrals.LOGGED_IN_DATA
import com.hedvig.app.util.ApolloCacheClearRule
import com.hedvig.app.util.ApolloMockServerRule
import com.hedvig.app.util.LazyActivityScenarioRule
import com.hedvig.app.util.apolloResponse
import com.hedvig.app.util.context
import com.hedvig.app.util.hasText
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

class TerminatedTest : TestCase() {
  @get:Rule
  val activityRule = LazyActivityScenarioRule(LoggedInActivity::class.java)

  @get:Rule
  val mockServerRule = ApolloMockServerRule(
    LoggedInQuery.OPERATION_DOCUMENT to apolloResponse {
      success(LOGGED_IN_DATA)
    },
    HomeQuery.OPERATION_DOCUMENT to apolloResponse {
      success(HOME_DATA_TERMINATED)
    },
  )

  @get:Rule
  val apolloCacheClearRule = ApolloCacheClearRule()

  @Test
  fun shouldShowMessageWhenUserHasAllContractsInTerminatedState() = run {
    activityRule.launch(LoggedInActivity.newInstance(context()))

    onScreen<HomeTabScreen> {
      recycler {
        childAt<HomeTabScreen.BigTextItem>(0) {
          text { hasText(hedvig.resources.R.string.home_tab_terminated_welcome_title, "Test") }
        }
        childAt<HomeTabScreen.BodyTextItem>(1) {
          text { hasText(hedvig.resources.R.string.home_tab_terminated_body) }
        }
        childAt<HomeTabScreen.StartClaimItem>(2) {
          button { click() }
        }
      }
    }

    onScreen<HonestyPledgeSheetScreen> {
      claim { isVisible() }
    }
  }
}