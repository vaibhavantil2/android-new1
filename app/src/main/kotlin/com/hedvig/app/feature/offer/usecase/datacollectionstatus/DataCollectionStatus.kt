package com.hedvig.app.feature.offer.usecase.datacollectionstatus

import com.hedvig.android.apollo.graphql.DataCollectionStatusSubscription

data class DataCollectionStatus(
  val insuranceCompany: String?,
  val subscriptionStatus: DataCollectionSubscriptionStatus,
) {
  companion object {
    fun fromDto(dto: DataCollectionStatusSubscription.Data): DataCollectionStatus {
      return DataCollectionStatus(
        dto.dataCollectionStatusV2.insuranceCompany,
        DataCollectionSubscriptionStatus.fromDto(dto.dataCollectionStatusV2.status),
      )
    }
  }

  enum class DataCollectionSubscriptionStatus {
    IN_PROGRESS,
    COMPLETE,
    FAILED,
    ;

    companion object {
      fun fromDto(
        dto: com.hedvig.android.apollo.graphql.type.DataCollectionStatus,
      ): DataCollectionSubscriptionStatus {
        return when (dto) {
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.COLLECTING,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.RUNNING,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.LOGIN,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.UNKNOWN__,
          -> IN_PROGRESS

          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.COMPLETED_PARTIAL,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.COMPLETED,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.COMPLETED_EMPTY,
          -> COMPLETE

          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.WAITING_FOR_AUTHENTICATION,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.USER_INPUT,
          com.hedvig.android.apollo.graphql.type.DataCollectionStatus.FAILED,
          -> FAILED
        }
      }
    }
  }
}