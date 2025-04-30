package com.example.project1729.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project1729.data.db.entity.TestEntity

@Dao
interface TestDao {

    @Insert(entity = TestEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity)

    @Query("SELECT * FROM test_table")
    suspend fun getTestsList(): List<TestEntity>

    @Query("DELETE FROM test_table WHERE type = :type")
    suspend fun deleteTests(type: String)
}

