package com.hedvig.app.feature.home.ui.changeaddress.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.view.isVisible
import com.hedvig.app.BaseActivity
import com.hedvig.app.R
import com.hedvig.app.databinding.ChangeAddressResultActivityBinding
import com.hedvig.app.feature.loggedin.ui.LoggedInActivity
import com.hedvig.app.util.extensions.startChat
import com.hedvig.app.util.extensions.view.setHapticClickListener
import com.hedvig.app.util.extensions.viewBinding
import e
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChangeAddressResultActivity : BaseActivity(R.layout.change_address_result_activity) {

  override val screenName = "moving_flow_signed"

  private val binding by viewBinding(ChangeAddressResultActivityBinding::bind)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val result = intent.getParcelableExtra<Result>(RESULT) ?: run {
      e { "Programmer error: Missing argument RESULT in ${this.javaClass.name}" }
      return
    }

    with(binding) {
      toolbar.setNavigationOnClickListener { finish() }
      when (result) {
        is Result.Success -> {
          toolbar.isVisible = false
          image.setImageResource(R.drawable.illustration_move_success)
          title.setText(hedvig.resources.R.string.moving_confirmation_success_title)
          subtitle.text = if (result.date == null) {
            getString(hedvig.resources.R.string.moving_confirmation_success_no_date_paragraph_copy)
          } else {
            getString(
              hedvig.resources.R.string.moving_confirmation_success_paragraph,
              DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(result.date),
            )
          }

          continueButton.setText(hedvig.resources.R.string.moving_confirmation_success_button_text)
          continueButton.setHapticClickListener {
            startLoggedIn()
          }
        }
        Result.Error -> {
          toolbar.isVisible = true
          image.setImageResource(R.drawable.illustration_helicopter)
          title.setText(hedvig.resources.R.string.moving_uw_failure_title)
          subtitle.setText(hedvig.resources.R.string.moving_uw_failure_paragraph)
          continueButton.setText(hedvig.resources.R.string.moving_uw_failure_button_text)
          continueButton.setIconResource(R.drawable.ic_chat_white)
          continueButton.setHapticClickListener {
            startChat()
          }
        }
      }
    }
  }

  override fun onBackPressed() {
    startLoggedIn()
  }

  private fun startLoggedIn() {
    startActivity(
      LoggedInActivity.newInstance(
        this@ChangeAddressResultActivity,
        withoutHistory = true,
        showRatingDialog = true,
      ),
    )
  }

  sealed class Result : Parcelable {
    @Parcelize
    data class Success(
      val date: LocalDate?,
    ) : Result()

    @Parcelize
    object Error : Result()
  }

  companion object {
    private const val RESULT = "RESULT"
    fun newInstance(context: Context, result: Result) =
      Intent(context, ChangeAddressResultActivity::class.java).apply {
        putExtra(RESULT, result)
      }
  }
}