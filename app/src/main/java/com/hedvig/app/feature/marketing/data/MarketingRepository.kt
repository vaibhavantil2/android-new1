package com.hedvig.app.feature.marketing.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.coroutines.await
import com.hedvig.android.owldroid.graphql.MarketingBackgroundQuery
import com.hedvig.app.util.LocaleManager

class MarketingRepository(
    private val apolloClient: ApolloClient,
    private val localeManager: LocaleManager
) {
    suspend fun marketingBackground() = apolloClient
        .query(MarketingBackgroundQuery(localeManager.defaultLocale().rawValue))
        .execute()
}
