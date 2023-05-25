package com.hedvig.app.feature.loggedin

import com.hedvig.app.R
import com.hedvig.app.feature.loggedin.ui.LoggedInActivity
import com.hedvig.app.testdata.feature.home.HOME_DATA_TERMINATED
import com.hedvig.app.testdata.feature.loggedin.CONTRACT_STATUS_DATA_ONE_TERMINATED_CONTRACT
import com.hedvig.app.testdata.feature.referrals.LOGGED_IN_DATA
import com.hedvig.app.util.ApolloCacheClearRule
import com.hedvig.app.util.ApolloMockServerRule
import com.hedvig.app.util.LazyActivityScenarioRule
import com.hedvig.app.util.apolloResponse
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import giraffe.ContractStatusQuery
import giraffe.HomeQuery
import giraffe.LoggedInQuery
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test

class TerminatedTest : TestCase() {
  @get:Rule
  val activityRule = LazyActivityScenarioRule(LoggedInActivity::class.java)

  @get:Rule
  val mockServerRule = ApolloMockServerRule(
    ContractStatusQuery.OPERATION_DOCUMENT to apolloResponse {
      success(CONTRACT_STATUS_DATA_ONE_TERMINATED_CONTRACT)
    },
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
  fun shouldOpenWithHomeTabWhenUserIsNotTerminated() = run {
    activityRule.launch()

    onScreen<LoggedInScreen> {
      root { isVisible() }
      bottomTabs { hasSelectedItem(R.id.home) }
    }
  }
}