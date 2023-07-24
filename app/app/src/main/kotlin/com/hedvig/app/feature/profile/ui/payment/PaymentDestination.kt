package com.hedvig.app.feature.profile.ui.payment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hedvig.android.core.designsystem.component.button.HedvigContainedButton
import com.hedvig.android.core.designsystem.component.button.HedvigContainedSmallButton
import com.hedvig.android.core.designsystem.component.card.HedvigCard
import com.hedvig.android.core.designsystem.component.textfield.HedvigTextField
import com.hedvig.android.core.designsystem.material3.typeContainer
import com.hedvig.android.core.designsystem.material3.typeElement
import com.hedvig.android.core.designsystem.newtheme.SquircleShape
import com.hedvig.android.core.designsystem.preview.HedvigPreview
import com.hedvig.android.core.designsystem.theme.HedvigTheme
import com.hedvig.android.core.icons.Hedvig
import com.hedvig.android.core.icons.hedvig.normal.Payments
import com.hedvig.android.core.icons.hedvig.normal.Waiting
import com.hedvig.android.core.ui.clearFocusOnTap
import com.hedvig.android.core.ui.hedvigDateTimeFormatter
import com.hedvig.android.core.designsystem.component.progress.HedvigFullScreenCenterAlignedProgress
import com.hedvig.android.core.ui.scaffold.HedvigScaffold
import com.hedvig.android.market.Market
import com.hedvig.app.feature.offer.usecase.CampaignCode
import com.hedvig.app.feature.payment.connectPayinIntent
import com.hedvig.app.feature.profile.ui.payment.PaymentViewModel2.*
import com.hedvig.hanalytics.PaymentType
import hedvig.resources.R
import java.time.LocalDate
import java.util.*

@Composable
fun PaymentDestination(
  viewModel: PaymentViewModel2,
  onBackPressed: () -> Unit,
) {
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AnimatedContent(targetState = uiState.isLoading) { loading ->
    when (loading) {
      true -> HedvigFullScreenCenterAlignedProgress(show = uiState.isLoading)
      false -> PaymentScreen(
        uiState = uiState,
        locale = Locale.ENGLISH,
        navigateUp = onBackPressed,
        onChangeBankAccount = {
          context.startActivity(
            connectPayinIntent(
              context,
              PaymentType.TRUSTLY,
              Market.SE,
              false,
            ),
          )
        },
        onAddDiscountCode = viewModel::onDiscountCodeAdded,
        onDiscountCodeChanged = viewModel::onDiscountCodeChanged,
      )
    }
  }
}

@Composable
fun PaymentScreen(
  uiState: PaymentUiState,
  locale: Locale,
  navigateUp: () -> Unit,
  onChangeBankAccount: () -> Unit,
  onAddDiscountCode: () -> Unit,
  onDiscountCodeChanged: (CampaignCode) -> Unit,
) {
  HedvigScaffold(
    topAppBarText = stringResource(R.string.PROFILE_PAYMENT_TITLE),
    navigateUp = navigateUp,
    modifier = Modifier.clearFocusOnTap(),
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      Spacer(Modifier.height(16.dp))
      NextPayment(uiState, locale)
      Spacer(Modifier.height(4.dp))
      Divider()
      Spacer(Modifier.height(4.dp))
      InsuranceCosts(uiState, locale)
      Spacer(Modifier.height(4.dp))
      TotalDiscount(uiState)
      Spacer(Modifier.height(4.dp))
      Divider()
      Spacer(Modifier.height(16.dp))
      if (uiState.activeDiscounts.isEmpty()) {
        AddDiscount(
          uiState = uiState,
          onAddDiscountCode = onAddDiscountCode,
          onDiscountCodeChanged = onDiscountCodeChanged,
        )
      }
      if (uiState.activeDiscounts.isNotEmpty()) {
        uiState.activeDiscounts.forEach {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(it.code)
            Text(
              text = it.displayName,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
          Spacer(Modifier.height(4.dp))
        }
        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(4.dp))
        TotalAmount(uiState)
      }
      Spacer(Modifier.height(32.dp))
      PaymentDetails(uiState)
      Spacer(Modifier.height(4.dp))
      Divider()
      PaymentHistory(onClick = {})
      Spacer(Modifier.height(16.dp))
      HedvigContainedButton(
        text = stringResource(id = R.string.PROFILE_PAYMENT_CHANGE_BANK_ACCOUNT),
        onClick = onChangeBankAccount,
      )
    }
  }
}

@Composable
private fun NextPayment(uiState: PaymentUiState, locale: Locale) {
  HedvigCard(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = uiState.nextChargeAmount?.format(locale) ?: "",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.displayMedium,
      modifier = Modifier.padding(vertical = 6.dp),
    )
  }
  Spacer(Modifier.height(12.dp))
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(stringResource(R.string.PAYMENTS_NEXT_PAYMENT_SECTION_TITLE))
    Text(
      text = uiState.nextChargeDate?.format(hedvigDateTimeFormatter(locale)) ?: "-",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun InsuranceCosts(
  uiState: PaymentUiState,
  locale: Locale,
) {
  uiState.insuranceCosts.forEach { insuranceCost ->
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Image(
          painter = painterResource(id = com.hedvig.android.core.ui.R.drawable.ic_pillow),
          contentDescription = null,
          modifier = Modifier.size(32.dp),
        )
        Spacer(Modifier.width(8.dp))
        Text(insuranceCost.displayName)
      }
      if (insuranceCost.cost != null) {
        Text(
          text = insuranceCost.cost.format(locale),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    Divider()
  }
}

@Composable
private fun TotalDiscount(uiState: PaymentUiState) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(stringResource(R.string.PAYMENTS_DISCOUNTS_SECTION_TITLE))
    Text(
      text = uiState.totalDiscount ?: "",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun AddDiscount(
  uiState: PaymentUiState,
  onAddDiscountCode: () -> Unit,
  onDiscountCodeChanged: (CampaignCode) -> Unit,
) {
  var showDiscountInput by rememberSaveable { mutableStateOf(uiState.activeDiscounts.isNotEmpty()) }
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(stringResource(R.string.PAYMENTS_ADD_CODE_LABEL))
    Switch(
      checked = showDiscountInput,
      onCheckedChange = { showDiscountInput = !showDiscountInput },
      colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.typeElement),
    )
  }
  Spacer(Modifier.height(12.dp))
  AnimatedVisibility(
    visible = showDiscountInput,
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      HedvigTextField(
        value = uiState.discountCode?.code ?: "",
        onValueChange = { onDiscountCodeChanged(CampaignCode(it)) },
        errorText = uiState.discountError?.let { stringResource(id = R.string.general_error) },
        label = {
          Text(stringResource(id = R.string.REFERRAL_ADDCOUPON_INPUTPLACEHOLDER))
        },
        keyboardActions = KeyboardActions(
          onDone = { onAddDiscountCode() },
        ),
        modifier = Modifier.weight(1f),
      )
      Spacer(Modifier.width(8.dp))
      HedvigContainedSmallButton(
        text = stringResource(id = R.string.PAYMENTS_ADD_CODE_BUTTON_LABEL),
        onClick = onAddDiscountCode,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.typeContainer,
          contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.widthIn(min = 127.dp),
      )
    }
  }
}


@Composable
private fun TotalAmount(uiState: PaymentUiState) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(stringResource(id = R.string.payment_details_receipt_card_total))
    Text(
      text = stringResource(
        id = R.string.OFFER_COST_AND_PREMIUM_PERIOD_ABBREVIATION,
        uiState.nextChargeAmount ?: "-",
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
fun PaymentDetails(uiState: PaymentUiState) {
  Text(stringResource(id = R.string.payment_details_navigation_bar_title))
  Spacer(Modifier.height(16.dp))
  Divider()
  Spacer(Modifier.height(4.dp))
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        imageVector = Icons.Hedvig.Payments,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
      )
      Spacer(Modifier.width(16.dp))
      Text(uiState.paymentMethod?.displayName ?: stringResource(id = R.string.info_card_missing_payment_title))
    }
    Text(
      text = uiState.paymentMethod?.displayValue ?: "",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
fun PaymentHistory(onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(SquircleShape)
      .clickable { onClick() }
      .padding(vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Hedvig.Waiting,
      contentDescription = null,
      modifier = Modifier.size(24.dp),
    )
    Spacer(Modifier.width(16.dp))
    Text(stringResource(id = R.string.PAYMENTS_PAYMENT_HISTORY_BUTTON_LABEL))
  }
}

@Composable
@HedvigPreview
fun PreviewPaymentScreen() {
  HedvigTheme(useNewColorScheme = true) {
    Surface {
      PaymentScreen(
        uiState = PaymentUiState(
          nextChargeAmount = "300 kr",
          nextChargeDate = LocalDate.now(),
          insuranceCosts = listOf(
            PaymentUiState.InsuranceCost(
              displayName = "Home Insurance",
              cost = "279kr/mån",
            ),
            PaymentUiState.InsuranceCost(
              displayName = "Accident Insurance",
              cost = "359kr/mån",
            ),
          ),
          totalDiscount = "-40kr/mån",
          activeDiscounts = listOf(
            PaymentUiState.Discount(code = "FREE", displayName = "Gratis i 6 mån"),
            PaymentUiState.Discount(code = "BANK", displayName = "-50kr/mån"),
          ),
          paymentMethod = PaymentUiState.PaymentMethod(
            displayName = "Nordea",
            displayValue = "31489*****",
          ),
        ),
        locale = Locale.ENGLISH,
        navigateUp = {},
        onChangeBankAccount = {},
        onAddDiscountCode = {},
        onDiscountCodeChanged = {},
      )
    }
  }
}

