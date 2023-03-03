package com.hedvig.android.core.designsystem.material2

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Take shapes from existing theme setup
// https://github.com/HedvigInsurance/android/blob/ced77986fac0fd7867c8e24ba05d0176a112050e/app/src/main/res/values/theme.xml#L27-L33
// https://github.com/HedvigInsurance/android/blob/0dfcbd61bd6b4f4b0d5bbd93e339deff3e15b5a9/app/src/main/res/values/shape_themes.xml#L4-L10
internal val HedvigShapes: Shapes
  @Composable
  get() = MaterialTheme.shapes.copy(
    medium = RoundedCornerShape(8.0.dp),
    large = RoundedCornerShape(8.0.dp),
  )