package com.example.project1729.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project1729.data.db.dao.CheckDao
import com.example.project1729.data.db.dao.TestDao
import com.example.project1729.data.db.entity.CheckEntity
import com.example.project1729.data.db.entity.TestEntity


@Database(version = 2, entities = [CheckEntity::class, TestEntity::class])
abstract class Database : RoomDatabase() {

    abstract fun checkDao(): CheckDao
    abstract fun testDao(): TestDao
}

