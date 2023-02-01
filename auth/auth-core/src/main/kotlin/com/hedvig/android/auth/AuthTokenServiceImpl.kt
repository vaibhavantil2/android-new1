package com.hedvig.android.auth

import com.hedvig.android.auth.storage.AuthTokenStorage
import com.hedvig.android.auth.token.AuthTokens
import com.hedvig.android.auth.token.LocalRefreshToken
import com.hedvig.authlib.AccessToken
import com.hedvig.authlib.AuthRepository
import com.hedvig.authlib.AuthTokenResult
import com.hedvig.authlib.RefreshToken
import com.hedvig.authlib.RefreshTokenGrant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import slimber.log.v

class AuthTokenServiceImpl(
  private val authTokenStorage: AuthTokenStorage,
  private val authRepository: AuthRepository,
  coroutineScope: CoroutineScope,
) : AuthTokenService {

  @Suppress("NAME_SHADOWING")
  override val authStatus: StateFlow<AuthStatus?> = authTokenStorage.getTokens()
    .mapLatest { authTokens ->
      val (accessToken, refreshToken) = authTokens ?: return@mapLatest AuthStatus.LoggedOut
      AuthStatus.LoggedIn(accessToken, refreshToken)
    }
    .stateIn(
      coroutineScope,
      SharingStarted.Eagerly,
      null,
    )

  override suspend fun getTokens(): AuthTokens? {
    return authTokenStorage.getTokens().first()
  }

  override suspend fun refreshAndGetAccessToken(): AccessToken? {
    val refreshToken = getRefreshToken() ?: return null
    return when (val result = authRepository.exchange(RefreshTokenGrant(refreshToken.token))) {
      is AuthTokenResult.Error -> {
        v { "Refreshing token failed. Invalidating present tokens" }
        invalidateTokens()
        null
      }
      is AuthTokenResult.Success -> {
        v { "Refreshing token success. Updating tokens" }
        authTokenStorage.updateTokens(result.accessToken, result.refreshToken)
        result.accessToken
      }
    }
  }

  override suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken) {
    authTokenStorage.updateTokens(accessToken, refreshToken)
  }

  override suspend fun invalidateTokens() {
    authTokenStorage.clearTokens()
  }

  private suspend fun getRefreshToken(): LocalRefreshToken? {
    return authTokenStorage.getTokens().first()?.refreshToken
  }

  override suspend fun migrateFromToken(token: String) {
    when (val result = authRepository.migrateOldToken(token)) {
      is AuthTokenResult.Error -> {
        // logout
      }
      is AuthTokenResult.Success -> {
        updateTokens(result.accessToken, result.refreshToken)
      }
    }
  }
}