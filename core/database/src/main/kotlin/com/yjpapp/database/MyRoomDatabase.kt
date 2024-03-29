package com.yjpapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yjpapp.database.mystock.MyStockDao
import com.yjpapp.database.mystock.MyStockEntity

@Database(
    entities = [MyStockEntity::class],
    version = 3,
    exportSchema = false
)

abstract class MyRoomDatabase: RoomDatabase() {
    abstract fun myStockDao(): MyStockDao

    companion object {
        val DB_NAME = "room-db"
    }
}