// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.corin.lectrium.model.Assignment

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY dueTimestamp ASC")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    @Query("DELETE FROM assignments")
    suspend fun clearAllAssignments()
}
