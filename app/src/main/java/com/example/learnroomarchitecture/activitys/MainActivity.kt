package com.example.learnroomarchitecture.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnroomarchitecture.MainViewModel
import com.example.learnroomarchitecture.RecyclerView.InTodoListRecyclerview
import com.example.learnroomarchitecture.RecyclerView.TodolistRecyclerViewAdpter
import com.example.learnroomarchitecture.dataBaseRoom.TodoListDatabase
import com.example.learnroomarchitecture.dataBaseRoom.TodoListTable
import com.example.learnroomarchitecture.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener, InTodoListRecyclerview {

    private val TAG = "MainActivity로그"

    private var todoListDb: TodoListDatabase? =null

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListViewMode: MainViewModel

    private var todoListArray = ArrayList<TodoListTable>()

    private lateinit var todoListRecyclerAdpter: TodolistRecyclerViewAdpter

    private val RESULT_WRITE_TODO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        todoListViewMode = ViewModelProvider(this).get(MainViewModel::class.java)

        setContentView(binding.root)

        todoListDb = TodoListDatabase.getInstance(this)
        binding.addListItem.setOnClickListener(this)

        this.todoListRecyclerAdpter = TodolistRecyclerViewAdpter(this)
        this.todoListRecyclerAdpter.submitList(todoListArray)

        val myLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        myLinearLayoutManager.stackFromEnd = true

        binding.todoListRecycler.apply {
            layoutManager = myLinearLayoutManager
            this.scrollToPosition(todoListRecyclerAdpter.itemCount - 1)
            adapter = todoListRecyclerAdpter
        }

        getTodoListAll()

        todoListViewMode.currentValue.observe(this, Observer {
            todoListRecyclerAdpter.submitList(it)
            todoListRecyclerAdpter.notifyDataSetChanged()
        })
    }

    override fun onClick(v: View?) {
        when(v){
            binding.addListItem->{
                val intent = Intent(this, WriteTodoActivity::class.java).apply {
                    putExtra("todoItme", "")
                    putExtra("insertData", true)
                }
                startActivityForResult(intent, RESULT_WRITE_TODO)
            }
        }
    }

    private fun getTodoListAll(){
        try{
        Thread(Runnable {
            kotlin.run {
                todoListArray = todoListDb?.todoListDao()?.getAll()!! as ArrayList<TodoListTable>
                todoListViewMode.updateValue(todoListArray)
                Log.d(TAG, "MainActivity - getTodoListAll : ${todoListArray.size}")

            }
            runOnUiThread{
//                this.todoListRecyclerAdpter.submitList(todoListArray)
//                this.todoListRecyclerAdpter.notifyDataSetChanged()
            }
        }).start()
        }catch (e: Exception){
            Log.d(TAG, "MainActivity - getTodoListAll : error : $e")
        }
    }

    override fun onClickedTodoItem(position: Int) {
        val intent = Intent(this, WriteTodoActivity::class.java).apply {
            putExtra("todoItmeId", todoListArray[position].toDoId)
            putExtra("todoItem", todoListArray[position].toDo)
            putExtra("insertData", false)
        }
        startActivityForResult(intent, RESULT_WRITE_TODO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            RESULT_WRITE_TODO->{
                if(resultCode == RESULT_OK){
                    getTodoListAll()
                }
            }
        }
    }

}