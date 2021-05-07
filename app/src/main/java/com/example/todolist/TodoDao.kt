package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao{
    @Insert()
    suspend fun inserTask(todoModel: TodoModel):Long // suspend bcz we need this fun to be run in different thread in coroutine

    @Query("Select * from TodoModel where isFinished != -1")
    fun getTask():LiveData<List<TodoModel>>

    @Query("Update TodoModel set isFinished = 1 where id=:uid")
    fun finishTash(uid:Long)

    @Query("Delete from TodoModel where id=:uid")
    fun deleteTask(uid: Long)
}