package com.example.project1729.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project1729.data.db.dao.CheckDao
import com.example.project1729.data.db.entity.CheckEntity


@Database(version = 1, entities = [CheckEntity::class])
abstract class Database : RoomDatabase() {

    abstract fun checkDao(): CheckDao
}