package me.proton.android.pass.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val DEFAULT_BIOMETRIC_LOCK = BiometricLockState.Disabled.value()
private val DEFAULT_THEME_PREFERENCE = ThemePreference.System.value()
private val DEFAULT_ON_BOARDING_PREFERENCE = HasCompletedOnBoarding.NotCompleted.value()
private val DEFAULT_AUTHENTICATED_PREFERENCE = HasAuthenticated.NotAuthenticated.value()

class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceRepository {

    override fun setBiometricLockState(state: BiometricLockState): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PassPreferences.BIOMETRIC_LOCK] = state.value()
        }
        emit(Unit)
    }

    override fun getBiometricLockState(): Flow<BiometricLockState> =
        dataStore.data
            .map { preferences ->
                val value = preferences[PassPreferences.BIOMETRIC_LOCK] ?: DEFAULT_BIOMETRIC_LOCK
                BiometricLockState.from(value)
            }

    override fun setHasAuthenticated(state: HasAuthenticated): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PassPreferences.HAS_AUTHENTICATED] = state.value()
        }
        emit(Unit)
    }

    override fun getHasAuthenticated(): Flow<HasAuthenticated> =
        dataStore.data
            .map { preferences ->
                val value = preferences[PassPreferences.HAS_AUTHENTICATED]
                    ?: DEFAULT_AUTHENTICATED_PREFERENCE
                HasAuthenticated.from(value)
            }

    override fun setHasCompletedOnBoarding(state: HasCompletedOnBoarding): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PassPreferences.HAS_COMPLETED_ON_BOARDING] = state.value()
        }
        emit(Unit)
    }

    override fun getHasCompletedOnBoarding(): Flow<HasCompletedOnBoarding> =
        dataStore.data
            .map { preferences ->
                val value = preferences[PassPreferences.HAS_COMPLETED_ON_BOARDING]
                    ?: DEFAULT_ON_BOARDING_PREFERENCE
                HasCompletedOnBoarding.from(value)
            }

    override fun setThemePreference(theme: ThemePreference): Flow<Unit> = flow {
        dataStore.edit { preferences ->
            preferences[PassPreferences.THEME] = theme.value()
        }
        emit(Unit)
    }

    override fun getThemePreference(): Flow<ThemePreference> =
        dataStore.data
            .map { preferences ->
                val value = preferences[PassPreferences.THEME] ?: DEFAULT_THEME_PREFERENCE
                ThemePreference.from(value)
            }

    override fun clearPreferences(): Flow<Unit> = flow {
        dataStore.edit { preferences -> preferences.clear() }
        emit(Unit)
    }
}
