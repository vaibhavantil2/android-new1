package com.hedvig.android.feature.payments.data

import com.hedvig.android.core.uidata.UiMoney
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
internal data class Discount(
  val code: String,
  val displayName: String?,
  val description: String?,
  val expiresAt: LocalDate?,
  val amount: UiMoney?,
  val isReferral: Boolean,
) {
  fun isExpired(now: LocalDate) = expiresAt?.let { it < now } ?: true
}
