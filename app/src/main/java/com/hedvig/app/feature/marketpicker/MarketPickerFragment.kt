package com.hedvig.app.feature.marketpicker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.hedvig.app.R
import com.hedvig.app.databinding.FragmentMarketPickerBinding
import com.hedvig.app.feature.settings.SettingsActivity
import com.hedvig.app.util.extensions.view.updateMargin
import com.hedvig.app.util.extensions.viewBinding
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import org.koin.android.viewmodel.ext.android.viewModel

class MarketPickerFragment : Fragment(R.layout.fragment_market_picker) {
    private val viewModel: MarketPickerViewModel by viewModel()
    private val binding by viewBinding(FragmentMarketPickerBinding::bind)

    @SuppressLint("ApplySharedPref")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val newMarketPref = sharedPreferences.getString(SettingsActivity.SETTINGS_NEW_MARKET, null)

        binding.apply {
            picker.doOnApplyWindowInsets { view, insets, initialState ->
                view.updateMargin(bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom)
            }

            picker.adapter = PickerAdapter(parentFragmentManager, viewModel)

            viewModel.data.observe(viewLifecycleOwner) { data ->
                (picker.adapter as PickerAdapter).items = listOf(
                    Model.Button,
                    Model.LanguageModel(data.language),
                    Model.MarketModel(data.market),
                    Model.Title
                )
            }
        }
    }
}
