package com.hedvig.android.feature.odyssey.step.informdeflect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.hedvig.android.core.designsystem.component.button.HedvigContainedButton
import com.hedvig.android.core.designsystem.component.button.HedvigContainedSmallButton
import com.hedvig.android.core.designsystem.component.card.HedvigCard
import com.hedvig.android.core.designsystem.material3.rememberShapedColorPainter
import com.hedvig.android.core.designsystem.preview.HedvigPreview
import com.hedvig.android.core.ui.infocard.VectorInfoCard
import com.hedvig.android.core.ui.preview.calculateForPreview
import com.hedvig.android.core.ui.preview.rememberPreviewImageLoader
import com.hedvig.android.data.claimflow.ClaimFlowDestination
import com.hedvig.android.data.claimflow.DeflectPartner
import com.hedvig.android.feature.odyssey.ui.ClaimFlowScaffold
import com.hedvig.android.logger.LogPriority
import com.hedvig.android.logger.logcat
import hedvig.resources.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DeflectGlassDamageDestination(
  deflectGlassDamage: ClaimFlowDestination.DeflectGlassDamage,
  openChat: () -> Unit,
  closeClaimFlow: () -> Unit,
  windowSizeClass: WindowSizeClass,
  navigateUp: () -> Unit,
  openUrl: (String) -> Unit,
  imageLoader: ImageLoader,
) {
  DeflectGlassDamageScreen(
    partners = deflectGlassDamage.partners,
    openChat = openChat,
    closeClaimFlow = closeClaimFlow,
    windowSizeClass = windowSizeClass,
    navigateUp = navigateUp,
    openUrl = openUrl,
    imageLoader = imageLoader,
  )
}

@Composable
private fun DeflectGlassDamageScreen(
  partners: ImmutableList<DeflectPartner>,
  openChat: () -> Unit,
  closeClaimFlow: () -> Unit,
  windowSizeClass: WindowSizeClass,
  navigateUp: () -> Unit,
  openUrl: (String) -> Unit,
  imageLoader: ImageLoader,
) {
  ClaimFlowScaffold(
    windowSizeClass = windowSizeClass,
    navigateUp = navigateUp,
    closeClaimFlow = closeClaimFlow,
  ) {
    Spacer(Modifier.height(8.dp))
    VectorInfoCard(
      text = stringResource(R.string.SUBMIT_CLAIM_GLASS_DAMAGE_INFO_LABEL),
      modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(Modifier.height(16.dp))
    Text(
      text = stringResource(R.string.SUBMIT_CLAIM_PARTNER_TITLE),
      modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(Modifier.height(16.dp))
    partners.forEachIndexed { index, partner ->
      if (index > 0) {
        Spacer(Modifier.height(8.dp))
      }
      HedvigCard(
        colors = CardDefaults.outlinedCardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth(),
      ) {
        Column(Modifier.padding(16.dp)) {
          AsyncImage(
            model = partner.imageUrl,
            contentDescription = null,
            imageLoader = imageLoader,
            placeholder = rememberShapedColorPainter(MaterialTheme.colorScheme.surface),
            modifier = Modifier
              .padding(16.dp)
              .fillMaxWidth()
              .height(40.dp),
          )
          Spacer(Modifier.height(16.dp))
          Text(
            text = stringResource(R.string.SUBMIT_CLAIM_GLASS_DAMAGE_ONLINE_BOOKING_LABEL),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
          )
          Spacer(Modifier.height(16.dp))
          HedvigContainedButton(
            text = stringResource(R.string.SUBMIT_CLAIM_GLASS_DAMAGE_ONLINE_BOOKING_BUTTON),
            onClick = {
              val url = partner.url
              if (url != null) {
                openUrl(url)
              } else {
                logcat(LogPriority.ERROR) {
                  """
                  |Partner URL was null for DeflectGlassDamageDestination! Deflect partner:[$this]. 
                  |This is problematic because the UI offers no real help to the member, the CTA button does nothing.
                  """.trimMargin()
                }
              }
            },
          )
        }
      }
    }
    Spacer(Modifier.height(24.dp))
    Text(
      text = stringResource(R.string.SUBMIT_CLAIM_HOW_IT_WORKS_TITLE),
      modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.SUBMIT_CLAIM_GLASS_DAMAGE_HOW_IT_WORKS_LABEL),
      modifier = Modifier.padding(horizontal = 16.dp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(Modifier.height(32.dp))
    Text(
      text = stringResource(R.string.SUBMIT_CLAIM_NEED_HELP_TITLE),
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    )
    Text(
      text = stringResource(R.string.SUBMIT_CLAIM_NEED_HELP_LABEL),
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    )
    Spacer(Modifier.height(24.dp))
    HedvigContainedSmallButton(
      text = stringResource(R.string.open_chat),
      onClick = openChat,
      modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
    )
    Spacer(Modifier.height(16.dp))
    Spacer(Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)))
  }
}

@HedvigPreview
@Composable
private fun PreviewDeflectGlassDamageScreen() {
  DeflectGlassDamageScreen(
    partners = persistentListOf(
      DeflectPartner(
        id = "1",
        imageUrl = "test",
        phoneNumber = "1234",
        url = "test",
      ),
      DeflectPartner(
        id = "2",
        imageUrl = "test2",
        phoneNumber = "4321",
        url = "test2",
      ),
    ),
    openChat = {},
    closeClaimFlow = {},
    windowSizeClass = WindowSizeClass.calculateForPreview(),
    navigateUp = {},
    imageLoader = rememberPreviewImageLoader(),
    openUrl = {},
  )
}
