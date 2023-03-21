package com.hedvig.android.feature.terminateinsurance.step.unknown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.hedvig.android.core.designsystem.component.button.LargeContainedTextButton
import com.hedvig.android.core.designsystem.component.button.LargeOutlinedTextButton
import com.hedvig.android.feature.terminateinsurance.ui.TerminationInfoScreen
import hedvig.resources.R

@Composable
internal fun UnknownScreenDestination(
  windowSizeClass: WindowSizeClass,
  openChat: () -> Unit,
  navigateBack: () -> Unit,
) {
  UnknownScreenScreen(
    windowSizeClass = windowSizeClass,
    openChat = openChat,
    navigateBack = navigateBack,
  )
}

@Composable
private fun UnknownScreenScreen(
  windowSizeClass: WindowSizeClass,
  openChat: () -> Unit,
  navigateBack: () -> Unit,
) {
  TerminationInfoScreen(
    windowSizeClass = windowSizeClass,
    navigateBack = navigateBack,
    title = "",
    headerText = stringResource(R.string.TERMINATION_NOT_SUCCESSFUL_TITLE),
    bodyText = "Could not find next step in flow. Please try again.",
    bottomContent = {
      Column {
        LargeOutlinedTextButton(
          text = stringResource(id = R.string.open_chat),
          onClick = openChat,
        )
        Spacer(Modifier.height(16.dp))
        LargeContainedTextButton(
          text = stringResource(R.string.general_done_button),
          onClick = navigateBack,
        )
      }
    },
    icon = ImageVector.vectorResource(com.hedvig.android.core.designsystem.R.drawable.ic_warning_triangle),
  )
}