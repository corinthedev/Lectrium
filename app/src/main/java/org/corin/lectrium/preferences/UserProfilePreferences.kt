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
    val FIRST_NAME = stringPreferencesKey("first_name")
    val MIDDLE_NAME = stringPreferencesKey("middle_name")
    val LAST_NAME = stringPreferencesKey("last_name")
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
    val IS_PRIVACY_POLICY_ACCEPTED = booleanPreferencesKey("is_privacy_policy_accepted")
}

class UserProfilePreferences(private val context: Context) {
    val userProfile: Flow<UserProfile> = context.userProfileDataStore.data.map { preferences ->
        val savedName = preferences[ProfileKeys.PROFILE_NAME] ?: ""
        val parts = savedName.split(" ").filter { it.isNotBlank() }
        val defaultFirst = if (parts.isNotEmpty()) parts.first() else ""
        val defaultLast = if (parts.size > 1) parts.last() else ""

        val fName = preferences[ProfileKeys.FIRST_NAME] ?: defaultFirst
        val mName = preferences[ProfileKeys.MIDDLE_NAME] ?: ""
        val lName = preferences[ProfileKeys.LAST_NAME] ?: defaultLast

        UserProfile(
            firstName = fName,
            middleName = mName,
            lastName = lName,
            profileName = savedName.ifBlank {
                listOf(fName, mName, lName).filter { it.isNotBlank() }.joinToString(" ")
            },
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

    val isPrivacyPolicyAcceptedFlow: Flow<Boolean> =
        context.userProfileDataStore.data.map { preferences ->
            preferences[ProfileKeys.IS_PRIVACY_POLICY_ACCEPTED] ?: false
        }

    suspend fun updateUserProfile(
        firstName: String,
        middleName: String,
        lastName: String,
        email: String,
        age: String,
        profileUniversity: String,
        profileProgram: String,
        profileYearLevel: String,
        studentStatus: String
    ) {
        val fullName =
            listOf(firstName, middleName, lastName).filter { it.isNotBlank() }.joinToString(" ")
        context.userProfileDataStore.edit { preferences ->
            preferences[ProfileKeys.FIRST_NAME] = firstName
            preferences[ProfileKeys.MIDDLE_NAME] = middleName
            preferences[ProfileKeys.LAST_NAME] = lastName
            preferences[ProfileKeys.PROFILE_NAME] = fullName
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

    suspend fun setPrivacyPolicyAccepted(accepted: Boolean) {
        context.userProfileDataStore.edit { preferences ->
            preferences[ProfileKeys.IS_PRIVACY_POLICY_ACCEPTED] = accepted
        }
    }
}
