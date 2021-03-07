package com.example.learnroomarchitecture.RecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.learnroomarchitecture.dataBaseRoom.TodoListTable
import kotlinx.android.synthetic.main.todo_list_recycler_item.view.*

class TodolistRecyclerViewHolder(itemView: View, todoListRecyclerviewInterface: InTodoListRecyclerview): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val todoTitle = itemView.todo_title
    private val todoDate = itemView.todo_date
    private val todoListView = itemView.swipe_view
    private val todoDeleteBtn = itemView.siwpe_delete_btn
    private var iTodo : InTodoListRecyclerview? = null

    init {
        this.iTodo = todoListRecyclerviewInterface
        todoListView.setOnClickListener(this)
    }

    fun bindWithView(todoTable: TodoListTable){
        todoTitle.text = todoTable.toDo
        todoDate.text = todoTable.toDoDate
    }

    override fun onClick(v: View?) {
        when(v){
            todoListView->{
                this.iTodo?.onClickedTodoItem(adapterPosition)
            }
            todoDeleteBtn->{
                this.iTodo?.onClickedTodoDeleteBtn(adapterPosition)
            }
        }
    }
}