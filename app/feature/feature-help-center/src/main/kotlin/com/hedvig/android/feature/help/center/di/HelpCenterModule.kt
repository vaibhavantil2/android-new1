package com.hedvig.android.feature.help.center.di

import com.hedvig.android.feature.help.center.HelpCenterViewModel
import com.hedvig.android.feature.help.center.data.GetCommonClaimsUseCase
import com.hedvig.android.feature.help.center.data.GetQuickLinksUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val helpCenterModule = module {
  single<GetCommonClaimsUseCase> {
    GetCommonClaimsUseCase(get())
  }
  single<GetQuickLinksUseCase> {
    GetQuickLinksUseCase(
      apolloClient = get(),
      featureManager = get(),
    )
  }
  viewModel<HelpCenterViewModel> {
    HelpCenterViewModel(
      getCommonClaimsUseCase = get<GetCommonClaimsUseCase>(),
      getQuickLinksUseCase = get<GetQuickLinksUseCase>(),
    )
  }
}
