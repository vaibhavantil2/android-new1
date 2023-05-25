package com.hedvig.app.util.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.apolloStore
import slimber.log.d
import slimber.log.e

class NetworkCacheManager(
  private val apolloClient: ApolloClient,
) {
  fun clearCache() {
    val didClearAllRecords = apolloClient.apolloStore.clearAll()
    if (didClearAllRecords) {
      d { "Did clear entire apolloStore cache" }
    } else {
      e { "Failed to clear apolloStore cache" }
    }
  }
}