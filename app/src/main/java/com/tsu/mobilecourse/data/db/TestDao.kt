package com.tsu.mobilecourse.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.concurrent.Flow

@Dao
interface TestDao {
    @Query("SELECT * FROM tests")
    suspend fun getAllTests(): List<TestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(tests: TestEntity): Unit
}