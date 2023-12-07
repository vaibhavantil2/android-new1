package com.hedvig.android.feature.payments2.data

import kotlinx.serialization.Serializable

@Serializable
data class PaymentConnection(
  val connectionInfo: ConnectionInfo?,
  val status: PaymentConnectionStatus,
) {
  @Serializable
  data class ConnectionInfo(
    val displayName: String,
    val displayValue: String,
  )

  @Serializable
  enum class PaymentConnectionStatus {
    ACTIVE,
    PENDING,
    NEEDS_SETUP,
    UNKNOWN,
  }

  val hasConnectedPayment: Boolean = status == PaymentConnectionStatus.ACTIVE
}
