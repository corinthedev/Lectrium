// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.utils

import android.content.Context
import org.corin.lectrium.repository.AcademicRepository

class ArchiveManager(context: Context) {
    private val repository = AcademicRepository(context)

    suspend fun resetSemesterSchedules() {
        repository.clearAllData()
    }
}
