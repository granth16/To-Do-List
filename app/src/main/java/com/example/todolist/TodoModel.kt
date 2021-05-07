package com.example.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoModel(//this is our todomodel which contain different component
    var title:String,
    var description:String,
    var category: String,
    var date:Long,
    var time:Long,
    var isFinished:Int = -1,
    @PrimaryKey(autoGenerate = true)// this will generate the id automatically and increment also
    var id:Long = 0 // here we make id as a last parameter bcz we don't want to give id again and again as in constructor
)
