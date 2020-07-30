package com.hedvig.app.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import com.agoda.kakao.edit.KTextInputLayout
import com.agoda.kakao.picker.date.KDatePicker
import java.time.LocalDate

fun KTextInputLayout.hasError(@StringRes resId: Int) =
    hasError(ApplicationProvider.getApplicationContext<Context>().getString(resId))

fun KTextInputLayout.hasError(@StringRes resId: Int, vararg formatArgs: Any) =
    hasError(ApplicationProvider.getApplicationContext<Context>().getString(resId, *formatArgs))

fun KDatePicker.setDate(date: LocalDate) = setDate(date.year, date.monthValue, date.dayOfMonth)
