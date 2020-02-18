package com.hedvig.app

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.hedvig.android.owldroid.type.MonetaryAmountV2Input
import com.hedvig.app.feature.keygear.KeyGearValuationViewModel
import org.threeten.bp.LocalDate

class MockKeyGearValuationViewModel : KeyGearValuationViewModel() {
    override val finishedUploading = MutableLiveData<Boolean>()

    override fun updatePurchaseDateAndPrice(
        id: String,
        date: LocalDate,
        price: MonetaryAmountV2Input
    ) {
        Handler().postDelayed({
            finishedUploading.postValue(true)
        }, 500L)
    }
}
