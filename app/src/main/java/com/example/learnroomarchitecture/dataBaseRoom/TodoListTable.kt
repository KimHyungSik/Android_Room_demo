package com.example.learnroomarchitecture.dataBaseRoom

import androidx.room.*

@Entity
class TodoListTable(
    @PrimaryKey(autoGenerate = true) var toDoId: Int?,
    @ColumnInfo(name = "toDo") var toDo: String,
    @ColumnInfo(name = "date") var toDoDate: String
){
    constructor(): this(null,"", "")
}

