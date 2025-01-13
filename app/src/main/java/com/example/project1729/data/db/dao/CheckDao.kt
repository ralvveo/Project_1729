package com.example.project1729.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project1729.data.db.entity.CheckEntity

@Dao
interface CheckDao {

    @Insert(entity = CheckEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheck(check: CheckEntity)

    @Query("SELECT * FROM check_table")
    suspend fun getChecksList(): List<CheckEntity>
}