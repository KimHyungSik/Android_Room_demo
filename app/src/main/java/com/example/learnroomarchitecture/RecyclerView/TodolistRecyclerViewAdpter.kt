package com.example.learnroomarchitecture.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnroomarchitecture.R
import com.example.learnroomarchitecture.dataBaseRoom.TodoListTable

class TodolistRecyclerViewAdpter(todoListRecyclerviewInterface: InTodoListRecyclerview):
                                RecyclerView.Adapter<TodolistRecyclerViewHolder>()
{
    private var todoListArray = ArrayList<TodoListTable>()
    var iTodoList : InTodoListRecyclerview? = null
    private val TAG = "TodolistRecyclerViewAdp로그"

    init {
        this.iTodoList = todoListRecyclerviewInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodolistRecyclerViewHolder {
        return TodolistRecyclerViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.todo_list_recycler_item,parent ,false),
                this.iTodoList!!
        )
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "TodolistRecyclerViewAdpter - getItemCount() : todoListArray.size ${todoListArray.size}")
        return todoListArray.size
    }

    override fun onBindViewHolder(holder: TodolistRecyclerViewHolder, position: Int) {
        Log.d(TAG, "TodolistRecyclerViewAdpter - onBindViewHolder() : ${this.todoListArray[position].toString()}")
        val dataItem: TodoListTable = this.todoListArray[position]
        holder.bindWithView(dataItem)
    }

    fun submitList(todoListArray: ArrayList<TodoListTable>){
        this.todoListArray = todoListArray
    }
}