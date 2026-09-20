// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

data class UserProfile(
    val profileName: String = "",
    val email: String = "",
    val age: String = "",
    val university: String = "",
    val program: String = "",
    val yearLevel: String = "1st Year",
    val studentStatus: String = "Regular",
    val profileImage: String? = null,
    val isSetupComplete: Boolean = false
)
