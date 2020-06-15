package com.hedvig.app.feature.referrals.tab

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen
import com.apollographql.apollo.api.toJson
import com.hedvig.android.owldroid.fragment.MonetaryAmountFragment
import com.hedvig.android.owldroid.fragment.ReferralFragment
import com.hedvig.android.owldroid.graphql.FeaturesQuery
import com.hedvig.android.owldroid.graphql.ReferralsQuery
import com.hedvig.android.owldroid.type.Feature
import com.hedvig.app.ApolloClientWrapper
import com.hedvig.app.R
import com.hedvig.app.feature.loggedin.ui.LoggedInActivity
import com.hedvig.app.feature.loggedin.ui.LoggedInTabs
import com.hedvig.app.feature.referrals.ReferralScreen
import com.hedvig.app.util.apollo.format
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.javamoney.moneta.Money
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.inject
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class ReferralTabOneRefereeTest : KoinTest {
    private val apolloClientWrapper: ApolloClientWrapper by inject()

    @get:Rule
    val activityRule = ActivityTestRule(LoggedInActivity::class.java, false, false)

    @Before
    fun setup() {
        apolloClientWrapper
            .apolloClient
            .clearNormalizedCache()
    }

    @Test
    fun shouldShowActiveStateWhenUserHasOneReferee() {
        MockWebServer().use { webServer ->
            webServer.dispatcher = object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    val body = request.body.peek().readUtf8()
                    if (body.contains("Features")) {
                        return MockResponse().setBody(FEATURES_DATA.toJson())
                    }

                    if (body.contains("Referrals")) {
                        return MockResponse().setBody(REFERRALS_DATA.toJson())
                    }

                    return MockResponse()
                }
            }

            webServer.start(8080)

            val intent = LoggedInActivity.newInstance(
                ApplicationProvider.getApplicationContext(),
                initialTab = LoggedInTabs.REFERRALS
            )

            activityRule.launchActivity(intent)

            Screen.onScreen<ReferralScreen> {
                share { isVisible() }
                recycler {
                    hasSize(4)
                    firstChild<ReferralScreen.HeaderItem> {
                        discountPerMonthPlaceholder { isGone() }
                        newPricePlaceholder { isGone() }
                        discountPerMonth { isVisible() }
                        newPrice {
                            isVisible()
                            hasText("")
                        }
                        discountPerMonthLabel { isVisible() }
                        newPriceLabel { isVisible() }
                        emptyHeadline { isGone() }
                        emptyBody { isGone() }
                    }
                    childAt<ReferralScreen.CodeItem>(1) {
                        placeholder { isGone() }
                        code {
                            isVisible()
                            hasText("TEST123")
                        }
                    }
                    childAt<ReferralScreen.InvitesHeaderItem>(2) {
                        isVisible()
                    }
                    childAt<ReferralScreen.ReferralItem>(3) {
                        iconPlaceholder { isGone() }
                        textPlaceholder { isGone() }
                        name {
                            isVisible()
                            hasText("Example")
                        }
                        referee { isVisible() }
                        icon {
                            isVisible()
                            hasDrawable(R.drawable.ic_basketball)
                        }
                        status {
                            hasText(
                                Money.of(-10, "SEK")
                                    .format(ApplicationProvider.getApplicationContext())
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val FEATURES_DATA = FeaturesQuery.Data(
            member = FeaturesQuery.Member(
                features = listOf(
                    Feature.KEYGEAR
                )
            )
        )

        private val REFERRALS_DATA = ReferralsQuery.Data(
            referralInformation = ReferralsQuery.ReferralInformation(
                campaign = ReferralsQuery.Campaign(
                    code = "TEST123",
                    incentive = ReferralsQuery.Incentive(
                        asMonthlyCostDeduction = ReferralsQuery.AsMonthlyCostDeduction(
                            amount = ReferralsQuery.Amount(
                                fragments = ReferralsQuery.Amount.Fragments(
                                    MonetaryAmountFragment(
                                        amount = "10.00",
                                        currency = "SEK"
                                    )
                                )
                            )
                        )
                    )
                ),
                referredBy = ReferralsQuery.ReferredBy(
                    fragments = ReferralsQuery.ReferredBy.Fragments(
                        ReferralFragment(
                            asActiveReferral = ReferralFragment.AsActiveReferral(
                                name = "Example",
                                discount = ReferralFragment.Discount(
                                    fragments = ReferralFragment.Discount.Fragments(
                                        MonetaryAmountFragment(
                                            amount = "10.00",
                                            currency = "SEK"
                                        )
                                    )
                                )
                            ),
                            asInProgressReferral = null,
                            asTerminatedReferral = null
                        )
                    )
                ),
                invitations = emptyList()
            )
        )
    }
}
