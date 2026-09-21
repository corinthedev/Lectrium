// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Exam
import org.corin.lectrium.model.Project

@Database(
    entities = [
        Course::class,
        ClassSchedule::class,
        Assignment::class,
        Project::class,
        Exam::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LectriumConverters::class)
abstract class LectriumDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun classScheduleDao(): ClassScheduleDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun projectDao(): ProjectDao
    abstract fun examDao(): ExamDao

    companion object {
        @Volatile
        private var INSTANCE: LectriumDatabase? = null

        fun getInstance(context: Context): LectriumDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LectriumDatabase::class.java,
                    "lectrium_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
