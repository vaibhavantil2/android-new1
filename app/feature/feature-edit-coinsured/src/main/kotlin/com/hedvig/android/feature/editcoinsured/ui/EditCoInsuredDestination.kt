package com.hedvig.android.feature.editcoinsured.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hedvig.android.core.designsystem.component.button.HedvigContainedButton
import com.hedvig.android.core.designsystem.component.progress.HedvigFullScreenCenterAlignedProgressDebounced
import com.hedvig.android.core.designsystem.material3.squircleLargeTop
import com.hedvig.android.core.designsystem.preview.HedvigPreview
import com.hedvig.android.core.designsystem.theme.HedvigTheme
import com.hedvig.android.core.ui.appbar.m3.TopAppBarWithBack
import com.hedvig.android.core.ui.dialog.ErrorDialog
import com.hedvig.android.core.ui.rememberHedvigDateTimeFormatter
import com.hedvig.android.feature.editcoinsured.data.CoInsured
import com.hedvig.android.feature.editcoinsured.data.Member
import hedvig.resources.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun EditCoInsuredDestination(
  viewModel: EditCoInsuredViewModel,
  allowEdit: Boolean,
  navigateUp: () -> Unit,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  EditCoInsuredScreen(
    navigateUp = navigateUp,
    allowEdit = allowEdit,
    uiState = uiState,
    onSave = {
      viewModel.emit(EditCoInsuredEvent.AddCoInsured(it))
    },
    onFetchInfo = {
      viewModel.emit(EditCoInsuredEvent.FetchCoInsuredPersonalInformation(it))
    },
    onDismissError = {
      viewModel.emit(EditCoInsuredEvent.OnDismissError)
    },
    onResetBottomSheetState = {
      viewModel.emit(EditCoInsuredEvent.ResetBottomSheetState)
    },
    onAddCoInsuredClicked = {
      viewModel.emit(EditCoInsuredEvent.OnAddCoInsuredClicked)
    },
  )
}

@Composable
private fun EditCoInsuredScreen(
  navigateUp: () -> Unit,
  allowEdit: Boolean,
  uiState: EditCoInsuredState,
  onSave: (CoInsured) -> Unit,
  onAddCoInsuredClicked: () -> Unit,
  onFetchInfo: (ssn: String) -> Unit,
  onDismissError: () -> Unit,
  onResetBottomSheetState: () -> Unit,
) {
  Column(Modifier.fillMaxSize()) {
    TopAppBarWithBack(
      title = stringResource(id = R.string.COINSURED_EDIT_TITLE),
      onClick = navigateUp,
    )

    when (uiState) {
      is EditCoInsuredState.Error -> {
        ErrorDialog(
          title = stringResource(id = R.string.general_error),
          message = uiState.message,
          onDismiss = onDismissError,
        )
      }

      is EditCoInsuredState.Loaded -> {
        val coroutineScope = rememberCoroutineScope()
        if (uiState.bottomSheetState.show) {
          val sheetState = rememberModalBottomSheetState(true)
          ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = onResetBottomSheetState,
            shape = MaterialTheme.shapes.squircleLargeTop,
            sheetState = sheetState,
            tonalElevation = 0.dp,
            windowInsets = BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Top),
          ) {
            AddCoInsuredBottomSheetContent(
              onSave = onSave,
              onFetchInfo = onFetchInfo,
              onDismiss = {
                coroutineScope.launch {
                  sheetState.hide()
                }.invokeOnCompletion {
                  onResetBottomSheetState()
                }
              },
              isLoading = uiState.bottomSheetState.isLoadingPersonalInfo,
              coInsured = uiState.bottomSheetState.coInsuredFromSsn,
              errorMessage = uiState.bottomSheetState.coInsuredFromSsnError,
            )
          }
        }

        Column {
          CoInsuredList(uiState.listState, allowEdit)
          if (!allowEdit) {
            Spacer(Modifier.height(8.dp))
            HedvigContainedButton(
              text = stringResource(id = R.string.CONTRACT_ADD_COINSURED),
              onClick = onAddCoInsuredClicked,
              modifier = Modifier.padding(horizontal = 16.dp),
            )
          }
        }
      }

      EditCoInsuredState.Loading -> HedvigFullScreenCenterAlignedProgressDebounced()
    }
  }
}

@Composable
private fun CoInsuredList(uiState: EditCoInsuredState.Loaded.CoInsuredListState, allowEdit: Boolean) {
  val dateTimeFormatter = rememberHedvigDateTimeFormatter()
  Column {
    uiState.member?.let {
      InsuredRow(
        displayName = it.displayName,
        identifier = it.ssn ?: "",
        hasMissingInfo = false,
        allowEdit = false,
        isMember = true,
        onRemove = { },
        onEdit = { },
      )
    }
    Divider(Modifier.padding(horizontal = 16.dp))
    uiState.coInsured.forEachIndexed { index, coInsured ->
      if (index != 0) {
        Divider()
      }

      InsuredRow(
        displayName = coInsured.displayName.ifBlank { stringResource(id = R.string.CONTRACT_COINSURED) },
        identifier = coInsured.identifier(dateTimeFormatter) ?: stringResource(id = R.string.CONTRACT_NO_INFORMATION),
        hasMissingInfo = coInsured.hasMissingInfo,
        isMember = false,
        allowEdit = allowEdit,
        onRemove = { },
        onEdit = { },
      )
    }
  }
}

@Composable
@HedvigPreview
private fun EditCoInsuredScreenEditablePreview() {
  HedvigTheme {
    Surface {
      EditCoInsuredScreen(
        navigateUp = { },
        allowEdit = true,
        uiState = EditCoInsuredState.Loaded(
          listState = EditCoInsuredState.Loaded.CoInsuredListState(
            coInsured = persistentListOf(
              CoInsured(
                "Test",
                "Testersson",
                LocalDate.fromEpochDays(300),
                "19910113-1093",
                hasMissingInfo = false,
              ),
              CoInsured(
                null,
                null,
                null,
                null,
                hasMissingInfo = true,
              ),
            ),
            member = Member(
              firstName = "Member",
              lastName = "Membersson",
              ssn = "197312331093",
            ),
          ),
          bottomSheetState = EditCoInsuredState.Loaded.BottomSheetState(
            coInsuredFromSsn = null,
            isLoadingPersonalInfo = false,
          ),
        ),
        onSave = {},
        onFetchInfo = {},
        onDismissError = {},
        onResetBottomSheetState = {},
        onAddCoInsuredClicked = {},
      )
    }
  }
}

@Composable
@HedvigPreview
private fun EditCoInsuredScreenNonEditablePreview() {
  HedvigTheme {
    Surface {
      EditCoInsuredScreen(
        navigateUp = { },
        allowEdit = false,
        uiState = EditCoInsuredState.Loaded(
          listState = EditCoInsuredState.Loaded.CoInsuredListState(
            coInsured = persistentListOf(
              CoInsured(
                "Test",
                "Testersson",
                LocalDate.fromEpochDays(300),
                "19910113-1093",
                hasMissingInfo = false,
              ),
              CoInsured(
                null,
                null,
                null,
                null,
                hasMissingInfo = true,
              ),
            ),
            member = Member(
              firstName = "Member",
              lastName = "Membersson",
              ssn = "197312331093",
            ),
          ),
          bottomSheetState = EditCoInsuredState.Loaded.BottomSheetState(
            coInsuredFromSsn = null,
            isLoadingPersonalInfo = false,
          ),
        ),
        onSave = {},
        onFetchInfo = {},
        onDismissError = {},
        onResetBottomSheetState = {},
        onAddCoInsuredClicked = {},
      )
    }
  }
}
