package com.hedvig.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hedvig.android.core.designsystem.component.button.HedvigTextButton
import com.hedvig.android.core.designsystem.material3.squircleLargeTop
import hedvig.resources.R
import kotlinx.coroutines.launch

@Composable
fun HedvigBottomSheet(
  onDismissRequest: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(true),
  content: @Composable () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    tonalElevation = 0.dp,
    shape = MaterialTheme.shapes.squircleLargeTop,
    windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Top),
  ) {
    content()
  }
}

@Composable
fun HedvigInfoBottomSheet(
  onDismissRequest: () -> Unit,
  sheetState: SheetState = rememberModalBottomSheetState(true),
  title: String,
  body: String,
) {
  val scope = rememberCoroutineScope()

  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    tonalElevation = 0.dp,
    shape = MaterialTheme.shapes.squircleLargeTop,
    windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Top),
  ) {
    Text(
      text = title,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    )
    Spacer(Modifier.height(16.dp))
    Text(
      text = body,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    )
    Spacer(Modifier.height(16.dp))
    HedvigTextButton(
      text = stringResource(id = R.string.general_close_button),
      onClick = {
        scope.launch {
          sheetState.hide()
        }.invokeOnCompletion {
          onDismissRequest()
        }
      },
      modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
    )
  }
}
