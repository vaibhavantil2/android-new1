package com.hedvig.android.feature.forever

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import arrow.core.raise.either
import arrow.fx.coroutines.parZip
import com.hedvig.android.core.common.ErrorMessage
import com.hedvig.android.core.uidata.UiMoney
import com.hedvig.android.data.forever.ForeverRepository
import com.hedvig.android.feature.forever.data.GetReferralsInformationUseCase
import com.hedvig.android.molecule.android.MoleculeViewModel
import com.hedvig.android.molecule.public.MoleculePresenter
import com.hedvig.android.molecule.public.MoleculePresenterScope
import giraffe.ReferralTermsQuery
import giraffe.ReferralsQuery
import giraffe.fragment.ReferralFragment

internal class ForeverViewModel(
  private val foreverRepository: ForeverRepository,
  private val getReferralTermsUseCase: GetReferralsInformationUseCase,
) : MoleculeViewModel<ForeverEvent, ForeverUiState>(
  ForeverUiState.Loading,
  ForeverPresenter(
    foreverRepository = foreverRepository,
    getReferralsInformationUseCase = getReferralTermsUseCase,
  ),
)

internal class ForeverPresenter(
  private val foreverRepository: ForeverRepository,
  private val getReferralsInformationUseCase: GetReferralsInformationUseCase,
) : MoleculePresenter<ForeverEvent, ForeverUiState> {
  @Composable
  override fun MoleculePresenterScope<ForeverEvent>.present(lastState: ForeverUiState): ForeverUiState {
    var isLoadingForeverData by remember { mutableStateOf(lastState.isLoadingForeverData) }
    var foreverDataLoadIteration by remember { mutableStateOf(0) }
    var foreverDataErrorMessage by remember { mutableStateOf(lastState.foreverDataErrorMessage) }
    var foreverData by remember { mutableStateOf(lastState.foreverData) }

    var referralCodeToSubmit by remember { mutableStateOf<String?>(null) }
    var referralCodeToSubmitErrorMessage by remember { mutableStateOf<ForeverRepository.ReferralError?>(null) }
    var showReferralCodeToSubmitSuccess by remember { mutableStateOf<Boolean>(false) }

    CollectEvents { event ->
      when (event) {
        ForeverEvent.ShowedReferralCodeSuccessfulChangeMessage -> showReferralCodeToSubmitSuccess = false
        ForeverEvent.ShowedReferralCodeSubmissionError -> referralCodeToSubmitErrorMessage = null
        ForeverEvent.RetryLoadReferralData -> foreverDataLoadIteration++
        is ForeverEvent.SubmitNewReferralCode -> referralCodeToSubmit = event.code
      }
    }

    LaunchedEffect(foreverDataLoadIteration) {
      isLoadingForeverData = true
      foreverDataErrorMessage = null
      either {
        parZip(
          { foreverRepository.getReferralsData().bind() },
          { getReferralsInformationUseCase.invoke().bind() },
        ) { referralsData, terms ->
          ForeverUiState.ForeverData(
            referralsData = referralsData,
            referralTerms = terms,
          )
        }
      }.fold(
        ifLeft = { foreverDataErrorMessage = it },
        ifRight = { foreverData = it },
      )
      isLoadingForeverData = false
    }

    LaunchedEffect(referralCodeToSubmit) {
      val codeToSubmit = referralCodeToSubmit
      if (codeToSubmit == null) return@LaunchedEffect
      foreverRepository.updateCode(codeToSubmit).fold(
        ifLeft = {
          referralCodeToSubmit = null
          referralCodeToSubmitErrorMessage = it
        },
        ifRight = {
          referralCodeToSubmit = null
          showReferralCodeToSubmitSuccess = true
          foreverDataLoadIteration++ // Trigger a refetch of the data to update the campaign code
        },
      )
    }

    return ForeverUiState(
      foreverData = foreverData,
      isLoadingForeverData = isLoadingForeverData,
      foreverDataErrorMessage = foreverDataErrorMessage,
      referralCodeLoading = referralCodeToSubmit != null,
      referralCodeErrorMessage = referralCodeToSubmitErrorMessage,
      showReferralCodeSuccessfullyChangedMessage = showReferralCodeToSubmitSuccess,
    )
  }
}

sealed interface ForeverEvent {
  data object ShowedReferralCodeSuccessfulChangeMessage : ForeverEvent
  data object ShowedReferralCodeSubmissionError : ForeverEvent
  data class SubmitNewReferralCode(val code: String) : ForeverEvent
  data object RetryLoadReferralData : ForeverEvent
}

internal data class ForeverUiState(
  val foreverData: ForeverData?,
  val isLoadingForeverData: Boolean,
  val foreverDataErrorMessage: ErrorMessage?,
  val referralCodeLoading: Boolean,
  val referralCodeErrorMessage: ForeverRepository.ReferralError?,
  val showReferralCodeSuccessfullyChangedMessage: Boolean,
) {

  data class ForeverData(
    val campaignCode: String?,
    val incentive: UiMoney?,
    val grossPriceAmount: UiMoney?,
    val referralUrl: String?,
    val potentialDiscountAmount: UiMoney?,
    val currentDiscountAmount: UiMoney?,
    val currentNetAmount: UiMoney?,
    val referrals: List<Referral>,
  ) {
    constructor(
      referralsData: ReferralsQuery.Data,
      referralTerms: ReferralTermsQuery.ReferralTerms?,
    ) : this(
      campaignCode = referralsData.referralInformation.campaign.code,
      incentive = referralsData
        .referralInformation
        .campaign
        .incentive
        ?.asMonthlyCostDeduction
        ?.amount
        ?.fragments
        ?.monetaryAmountFragment
        .let(UiMoney::fromMonetaryAmountFragment),
      grossPriceAmount = referralsData
        .referralInformation
        .costReducedIndefiniteDiscount
        ?.fragments
        ?.costFragment
        ?.monthlyGross
        ?.fragments
        ?.monetaryAmountFragment
        .let(UiMoney::fromMonetaryAmountFragment),
      referralUrl = referralTerms?.url,
      potentialDiscountAmount = referralsData
        .referralInformation
        .campaign
        .incentive
        ?.asMonthlyCostDeduction
        ?.amount
        ?.fragments
        ?.monetaryAmountFragment
        .let(UiMoney::fromMonetaryAmountFragment),
      currentDiscountAmount = referralsData
        .referralInformation
        .costReducedIndefiniteDiscount
        ?.fragments
        ?.costFragment
        ?.monthlyDiscount
        ?.fragments
        ?.monetaryAmountFragment
        .let(UiMoney::fromMonetaryAmountFragment),
      currentNetAmount = referralsData
        .referralInformation
        .costReducedIndefiniteDiscount
        ?.fragments
        ?.costFragment
        ?.monthlyNet
        ?.fragments
        ?.monetaryAmountFragment
        .let(UiMoney::fromMonetaryAmountFragment),
      referrals = referralsData.referralInformation.invitations.map {
        Referral(
          name = it.fragments.referralFragment.name,
          state = when {
            it.fragments.referralFragment.asInProgressReferral != null -> ReferralState.IN_PROGRESS
            it.fragments.referralFragment.asActiveReferral != null -> ReferralState.ACTIVE
            it.fragments.referralFragment.asTerminatedReferral != null -> ReferralState.TERMINATED
            else -> ReferralState.UNKNOWN
          },
          discount = it.fragments
            .referralFragment
            .asActiveReferral
            ?.discount
            ?.fragments
            ?.monetaryAmountFragment
            .let(UiMoney::fromMonetaryAmountFragment),
        )
      },
    )
  }

  data class Referral(
    val name: String?,
    val state: ReferralState,
    val discount: UiMoney?,
  )

  enum class ReferralState {
    ACTIVE, IN_PROGRESS, TERMINATED, UNKNOWN
  }

  companion object {
    val Loading: ForeverUiState = ForeverUiState(
      foreverData = null,
      isLoadingForeverData = true,
      foreverDataErrorMessage = null,
      referralCodeLoading = false,
      referralCodeErrorMessage = null,
      showReferralCodeSuccessfullyChangedMessage = false,
    )
  }
}

private val ReferralFragment.name: String?
  get() {
    asActiveReferral?.name?.let { return it }
    asInProgressReferral?.name?.let { return it }
    asTerminatedReferral?.name?.let { return it }
    return null
  }