package com.hedvig.app.feature.claims.ui.commonclaim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.hedvig.android.auth.android.AuthenticatedObserver
import com.hedvig.android.core.common.android.parcelableExtra
import com.hedvig.android.hanalytics.featureflags.FeatureManager
import com.hedvig.app.R
import com.hedvig.app.databinding.ActivityCommonClaimBinding
import com.hedvig.app.feature.claims.ui.commonclaim.bulletpoint.BulletPointsAdapter
import com.hedvig.app.feature.claims.ui.startClaimsFlow
import com.hedvig.app.util.extensions.compatSetDecorFitsSystemWindows
import com.hedvig.app.util.extensions.view.applyNavigationBarInsets
import com.hedvig.app.util.extensions.view.applyStatusBarInsets
import com.hedvig.app.util.extensions.view.disable
import com.hedvig.app.util.extensions.view.enable
import com.hedvig.app.util.extensions.view.setHapticClickListener
import com.hedvig.app.util.extensions.view.setupToolbarScrollListener
import com.hedvig.app.util.extensions.viewBinding
import com.hedvig.hanalytics.AppScreen
import com.hedvig.hanalytics.HAnalytics
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CommonClaimActivity : AppCompatActivity(R.layout.activity_common_claim) {

  private val imageLoader: ImageLoader by inject()
  private val binding by viewBinding(ActivityCommonClaimBinding::bind)
  private val hAnalytics: HAnalytics by inject()
  private val featureManager: FeatureManager by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(AuthenticatedObserver())

    val data = intent.parcelableExtra<CommonClaimsData>(CLAIMS_DATA)
      ?: error("Programmer error: incorrect arguments passed to ${this.javaClass.name}")
    getViewModel<CommonClaimViewModel>()

    binding.apply {
      window.compatSetDecorFitsSystemWindows(false)
      root.applyNavigationBarInsets()
      toolbar.applyStatusBarInsets()

      toolbar.setNavigationOnClickListener {
        onBackPressedDispatcher.onBackPressed()
      }
      root.setupToolbarScrollListener(toolbar = toolbar)
      toolbar.title = data.title

      bulletPointsRecyclerView.adapter =
        BulletPointsAdapter(imageLoader).also {
          it.submitList(data.bulletPoints)
        }
    }

    binding.apply {
      firstMessage.commonClaimFirstMessage.text = data.layoutTitle
      firstMessage.commonClaimCreateClaimButton.text = data.buttonText
      if (data.isFirstVet()) {
        firstMessage.commonClaimCreateClaimButton.enable()
        firstMessage.commonClaimCreateClaimButton.setHapticClickListener {
          startActivity(getFirstVetIntent(this@CommonClaimActivity))
        }
      } else if (data.eligibleToClaim) {
        firstMessage.commonClaimCreateClaimButton.enable()
        firstMessage.commonClaimCreateClaimButton.setHapticClickListener {
          lifecycleScope.launch {
            hAnalytics.beginClaim(AppScreen.COMMON_CLAIM_DETAIL)
            startClaimsFlow(
              fragmentManager = supportFragmentManager,
              commonClaimId = data.id,
              featureManager = featureManager,
              context = this@CommonClaimActivity,
            )
          }
        }
      } else {
        firstMessage.commonClaimCreateClaimButton.disable()
      }
    }
  }

  companion object {
    private const val CLAIMS_DATA = "claims_data"

    fun newInstance(context: Context, data: CommonClaimsData) =
      Intent(context, CommonClaimActivity::class.java).apply {
        putExtra(CLAIMS_DATA, data)
      }
  }
}