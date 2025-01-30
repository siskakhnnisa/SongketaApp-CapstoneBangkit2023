package com.songketa.songket_recognition_app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.songketa.songket_recognition_app.data.model.User
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PHONE_KEY] = user.phone
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USER_ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PHONE_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}