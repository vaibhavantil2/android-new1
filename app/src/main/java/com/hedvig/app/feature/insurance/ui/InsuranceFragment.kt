package com.hedvig.app.feature.insurance.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.hedvig.android.owldroid.graphql.InsuranceQuery
import com.hedvig.app.R
import com.hedvig.app.databinding.FragmentInsuranceBinding
import com.hedvig.app.feature.insurance.service.InsuranceTracker
import com.hedvig.app.feature.loggedin.ui.LoggedInViewModel
import com.hedvig.app.util.extensions.view.remove
import com.hedvig.app.util.extensions.view.setupToolbarAlphaScrollListener
import com.hedvig.app.util.extensions.view.updatePadding
import com.hedvig.app.util.extensions.viewBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class InsuranceFragment : Fragment(R.layout.fragment_insurance) {
    private val insuranceViewModel: InsuranceViewModel by sharedViewModel()
    private val loggedInViewModel: LoggedInViewModel by sharedViewModel()
    private val tracker: InsuranceTracker by inject()
    private val binding by viewBinding(FragmentInsuranceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val scrollInitialTopPadding = recycler.paddingTop
            loggedInViewModel.toolbarInset.observe(viewLifecycleOwner) { toolbarInsets ->
                recycler.updatePadding(top = scrollInitialTopPadding + toolbarInsets)
            }

            val scrollInitialBottomPadding = recycler.paddingBottom
            loggedInViewModel.bottomTabInset.observe(viewLifecycleOwner) { bottomTabInset ->
                recycler.updatePadding(bottom = scrollInitialBottomPadding + bottomTabInset)
            }

            recycler.setupToolbarAlphaScrollListener(loggedInViewModel)
            recycler.adapter = InsuranceAdapter(parentFragmentManager, tracker, insuranceViewModel::load)
        }
        insuranceViewModel.data.observe(viewLifecycleOwner) { data ->
            bind(data)
        }
    }

    private fun bind(data: Result<InsuranceQuery.Data>) {
        binding.loadSpinner.root.remove()

        if (data.isFailure) {
            (binding.recycler.adapter as? InsuranceAdapter)?.items = listOf(InsuranceModel.Error)
            return
        }

        val successData = data.getOrNull() ?: return

        val infoBoxes = mutableListOf<InsuranceModel.Renewal>()

        val renewals = successData.contracts.mapNotNull { it.upcomingRenewal }

        renewals.forEach {
            infoBoxes.add(
                InsuranceModel.Renewal(
                    it.renewalDate,
                    it.draftCertificateUrl
                )
            )
        }

        val contracts = successData.contracts.map { InsuranceModel.Contract(it) }

        val upsells = mutableListOf<InsuranceModel.Upsell>()

        if (isNorway(successData.contracts)) {
            if (doesNotHaveHomeContents(successData.contracts)) {
                upsells.add(UPSELL_HOME_CONTENTS)
            } else if (doesNotHaveTravelInsurance(successData.contracts)) {
                upsells.add(UPSELL_TRAVEL)
            }
        }

        (binding.recycler.adapter as? InsuranceAdapter)?.items =
            listOf(InsuranceModel.Header) + infoBoxes + contracts + upsells
    }

    override fun onResume() {
        super.onResume()
        binding.recycler.scrollToPosition(0)
    }

    companion object {
        private val UPSELL_HOME_CONTENTS =
            InsuranceModel.Upsell(
                R.string.UPSELL_NOTIFICATION_CONTENT_TITLE,
                R.string.UPSELL_NOTIFICATION_CONTENT_DESCRIPTION,
                R.string.UPSELL_NOTIFICATION_CONTENT_CTA
            )

        private val UPSELL_TRAVEL =
            InsuranceModel.Upsell(
                R.string.UPSELL_NOTIFICATION_TRAVEL_TITLE,
                R.string.UPSELL_NOTIFICATION_TRAVEL_DESCRIPTION,
                R.string.UPSELL_NOTIFICATION_TRAVEL_CTA
            )

        fun isNorway(contracts: List<InsuranceQuery.Contract>) =
            contracts.any { it.currentAgreement.asNorwegianTravelAgreement != null || it.currentAgreement.asNorwegianHomeContentAgreement != null }

        fun doesNotHaveHomeContents(contracts: List<InsuranceQuery.Contract>) =
            contracts.none { it.currentAgreement.asNorwegianHomeContentAgreement != null }

        fun doesNotHaveTravelInsurance(contracts: List<InsuranceQuery.Contract>) =
            contracts.none { it.currentAgreement.asNorwegianTravelAgreement != null }
    }
}
