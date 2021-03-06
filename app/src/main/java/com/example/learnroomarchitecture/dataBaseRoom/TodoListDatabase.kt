package com.example.learnroomarchitecture.dataBaseRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TodoListTable::class), version = 1)
abstract class TodoListDatabase : RoomDatabase(){
    abstract fun todoListDao(): TodoListDao
    companion object{
        private var INSTANCE: TodoListDatabase? = null
        fun getInstance(context: Context): TodoListDatabase?{
            if(INSTANCE == null){
                synchronized(TodoListDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TodoListDatabase::class.java,
                        "todolist.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destoryInstance(){
            INSTANCE = null
        }
    }
}