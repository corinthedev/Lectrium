// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.corin.lectrium.model.ClassSchedule

@Dao
interface ClassScheduleDao {
    @Query("SELECT * FROM class_schedules")
    fun getAllSchedules(): Flow<List<ClassSchedule>>

    @Query("SELECT * FROM class_schedules WHERE courseId = :courseId")
    fun getSchedulesForCourse(courseId: String): Flow<List<ClassSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ClassSchedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ClassSchedule>)

    @Update
    suspend fun updateSchedule(schedule: ClassSchedule)

    @Delete
    suspend fun deleteSchedule(schedule: ClassSchedule)

    @Query("DELETE FROM class_schedules WHERE courseId = :courseId")
    suspend fun deleteSchedulesForCourse(courseId: String)

    @Query("DELETE FROM class_schedules")
    suspend fun clearAllSchedules()
}
