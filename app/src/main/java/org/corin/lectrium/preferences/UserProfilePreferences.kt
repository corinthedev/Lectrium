// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.corin.lectrium.model.UserProfile

val Context.userProfileDataStore by preferencesDataStore("user_profile_preferences")

object ProfileKeys {
    val PROFILE_NAME = stringPreferencesKey("profile_name")
    val PROFILE_EMAIL = stringPreferencesKey("profile_email")
    val PROFILE_AGE = stringPreferencesKey("profile_age")
    val PROFILE_UNIVERSITY = stringPreferencesKey("university")
    val PROFILE_PROGRAM = stringPreferencesKey("program")
    val PROFILE_YEAR_LEVEL = stringPreferencesKey("year_level")
    val STUDENT_STATUS = stringPreferencesKey("student_status")
    val PROFILE_IMAGE = stringPreferencesKey("profile_image")
    val PROFILE_SETUP_COMPLETE = booleanPreferencesKey("is_profile_setup_complete")
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
}

class UserProfilePreferences(private val context: Context) {
    val userProfile: Flow<UserProfile> = context.userProfileDataStore.data.map { preferences ->
        UserProfile(
            profileName = preferences[ProfileKeys.PROFILE_NAME] ?: "",
            email = preferences[ProfileKeys.PROFILE_EMAIL] ?: "",
            age = preferences[ProfileKeys.PROFILE_AGE] ?: "",
            university = preferences[ProfileKeys.PROFILE_UNIVERSITY] ?: "",
            program = preferences[ProfileKeys.PROFILE_PROGRAM] ?: "",
            yearLevel = preferences[ProfileKeys.PROFILE_YEAR_LEVEL] ?: "1st Year",
            studentStatus = preferences[ProfileKeys.STUDENT_STATUS] ?: "Regular",
            profileImage = preferences[ProfileKeys.PROFILE_IMAGE],
            isSetupComplete = preferences[ProfileKeys.PROFILE_SETUP_COMPLETE] ?: false
        )
    }

    val isOnboardingCompletedFlow: Flow<Boolean> = context.userProfileDataStore.data.map { preferences ->
        preferences[ProfileKeys.IS_ONBOARDING_COMPLETED] ?: false
    }

    suspend fun updateUserProfile(
        profileName: String,
        email: String,
        age: String,
        profileUniversity: String,
        profileProgram: String,
        profileYearLevel: String,
        studentStatus: String
    ) {
        context.userProfileDataStore.edit { preferences ->
            preferences[ProfileKeys.PROFILE_NAME] = profileName
            preferences[ProfileKeys.PROFILE_EMAIL] = email
            preferences[ProfileKeys.PROFILE_AGE] = age
            preferences[ProfileKeys.PROFILE_UNIVERSITY] = profileUniversity
            preferences[ProfileKeys.PROFILE_PROGRAM] = profileProgram
            preferences[ProfileKeys.PROFILE_YEAR_LEVEL] = profileYearLevel
            preferences[ProfileKeys.STUDENT_STATUS] = studentStatus
        }
    }

    suspend fun updateUserProfileImagePath(userProfileImagePath: String) {
        context.userProfileDataStore.edit { it[ProfileKeys.PROFILE_IMAGE] = userProfileImagePath }
    }

    suspend fun markProfileSetupComplete() {
        context.userProfileDataStore.edit { it[ProfileKeys.PROFILE_SETUP_COMPLETE] = true }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.userProfileDataStore.edit { preferences ->
            preferences[ProfileKeys.IS_ONBOARDING_COMPLETED] = completed
        }
    }
}
