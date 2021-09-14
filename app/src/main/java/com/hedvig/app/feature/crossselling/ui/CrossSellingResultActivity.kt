package com.hedvig.app.feature.crossselling.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.hedvig.app.BaseActivity

class CrossSellingResultActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crossSellingResult: CrossSellingResult =
            intent.getParcelableExtra(CROSS_SELLING_RESULT) ?: CrossSellingResult.Error
        setContent {
            CrossSellingResultScreen(crossSellingResult)
        }
    }

    companion object {
        fun newInstance(context: Context, crossSellingResult: CrossSellingResult): Intent {
            return Intent(context, CrossSellingResultActivity::class.java).apply {
                putExtra(CROSS_SELLING_RESULT, crossSellingResult)
            }
        }

        private const val CROSS_SELLING_RESULT = "CROSS_SELLING_RESULT"
    }
}
