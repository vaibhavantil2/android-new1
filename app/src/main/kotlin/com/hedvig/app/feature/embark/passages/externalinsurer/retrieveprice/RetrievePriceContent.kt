package com.hedvig.app.feature.embark.passages.externalinsurer.retrieveprice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.hedvig.android.core.designsystem.component.button.LargeContainedTextButton
import com.hedvig.app.R
import com.hedvig.app.ui.compose.composables.ErrorDialog
import com.hedvig.app.ui.compose.textutil.SwedishSSNVisualTransformation

@Composable
fun RetrievePriceContent(
  onRetrievePriceInfo: () -> Unit,
  onIdentityInput: (String) -> Unit,
  onDismissError: () -> Unit,
  input: String,
  title: String,
  placeholder: String,
  label: String,
  inputErrorMessage: String?,
  errorMessage: String?,
  modifier: Modifier = Modifier,
) {
  val baseMargin = dimensionResource(R.dimen.base_margin)
  val baseMarginDouble = dimensionResource(R.dimen.base_margin_double)
  val baseMarginQuadruple = dimensionResource(R.dimen.base_margin_quadruple)

  Column(
    modifier = modifier
      .padding(baseMarginDouble)
      .fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(baseMarginDouble),
  ) {
    Text(
      modifier = Modifier.padding(top = baseMargin),
      text = title,
      style = MaterialTheme.typography.h6,
    )
    TextField(
      modifier = Modifier.fillMaxWidth(),
      value = input,
      onValueChange = {
        if (it.length <= 10) {
          onIdentityInput(it)
        }
      },
      singleLine = true,
      placeholder = { Text(placeholder) },
      label = {
        Text(inputErrorMessage ?: label)
      },
      colors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.background,
      ),
      isError = inputErrorMessage != null,
      visualTransformation = SwedishSSNVisualTransformation(),
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Send,
      ),
      keyboardActions = KeyboardActions(
        onSend = { onRetrievePriceInfo() },
      ),
    )
    LargeContainedTextButton(
      modifier = Modifier.padding(top = baseMarginQuadruple),
      text = stringResource(hedvig.resources.R.string.insurely_ssn_continue_button_text),
      onClick = onRetrievePriceInfo,
    )
  }

  if (errorMessage != null) {
    ErrorDialog(message = errorMessage, onDismiss = onDismissError)
  }
}