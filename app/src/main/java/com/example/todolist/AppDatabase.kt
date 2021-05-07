package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoModel::class],version = 1)
abstract class AppDatabase:RoomDatabase(){
    abstract fun todoDao():TodoDao
// we have to do this bcz we dont create two instance of the same data base
    // if we create the two instance it is not updating in the new todo list
    //burgtracker site i use as a refernce
    companion object{
    //singleton prevents mulitple instance of database at the same time
        @Volatile
        private var INSTANCE: AppDatabase?=null
        fun getDatabase(context: Context):AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this){ // since there may be multiple objects or instance of the room db therefore there should be synchronization between them so that there should not be deadlock
                val instance  = Room.databaseBuilder(  // if the tempinstance is not null then we create the new object of the room db and return that instance
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}