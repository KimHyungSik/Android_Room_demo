package com.example.learnroomarchitecture.dataBaseRoom

import androidx.room.*

@Dao
interface TodoListDao{
    @Query("SELECT * FROM TodoListTable")
    fun getAll(): List<TodoListTable>

    @Query("SELECT * FROM TodoListTable WHERE toDoId = :toDoId")
    fun findByID(toDoId: Int): TodoListTable

    @Query("SELECT * FROM TodoListTable WHERE toDo LIKE :query")
    fun findByName(query: String): List<TodoListTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodoListItem(todoList: TodoListTable)

    @Update
    fun updateTodoListItem(todoList: TodoListTable)

    @Delete
    fun deleteTodoListItem(todoList: TodoListTable)
}