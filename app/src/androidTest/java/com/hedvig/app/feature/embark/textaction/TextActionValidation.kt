package com.hedvig.app.feature.embark.textaction

import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen
import com.hedvig.android.owldroid.graphql.EmbarkStoryQuery
import com.hedvig.app.feature.embark.screens.EmbarkScreen
import com.hedvig.app.feature.embark.ui.EmbarkActivity
import com.hedvig.app.testdata.feature.embark.STORY_WITH_TEXT_ACTION_EMAIL_VALIDATION
import com.hedvig.app.util.ApolloCacheClearRule
import com.hedvig.app.util.ApolloMockServerRule
import com.hedvig.app.util.apolloResponse
import com.hedvig.app.util.context
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class TextActionValidation : TestCase() {
    @get:Rule
    val activityRule = ActivityTestRule(EmbarkActivity::class.java, false, false)

    @get:Rule
    val mockServerRule = ApolloMockServerRule(
        EmbarkStoryQuery.QUERY_DOCUMENT to apolloResponse {
            success(
                STORY_WITH_TEXT_ACTION_EMAIL_VALIDATION
            )
        }
    )

    @get:Rule
    val apolloCacheClearRule = ApolloCacheClearRule()

    @Test
    fun buttonShouldOnlyBeEnabledWhenValidEmailIsTyped() = run {
        activityRule.launchActivity(EmbarkActivity.newInstance(context(), "Story Name"))

        Screen.onScreen<EmbarkScreen> {
            textActionSubmit { isDisabled() }
            textActionSingleInput {typeText("email")}
            textActionSubmit { isDisabled() }
            textActionSingleInput {clearText()}
            textActionSingleInput {typeText("email@hedvig.com")}
            textActionSubmit { isEnabled() }
        }
    }
}

