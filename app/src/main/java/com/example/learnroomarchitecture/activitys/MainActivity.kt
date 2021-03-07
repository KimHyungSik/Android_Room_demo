package com.example.learnroomarchitecture.activitys

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnroomarchitecture.MainViewModel
import com.example.learnroomarchitecture.RecyclerView.InTodoListRecyclerview
import com.example.learnroomarchitecture.RecyclerView.SwipeHelperCallBack
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        todoListViewMode = ViewModelProvider(this).get(MainViewModel::class.java)

        setContentView(binding.root)

        todoListDb = TodoListDatabase.getInstance(this)
        binding.addListItem.setOnClickListener(this)

        // 리사이클러뷰 어뎁터 설정
        this.todoListRecyclerAdpter = TodolistRecyclerViewAdpter(this)
        this.todoListRecyclerAdpter.submitList(todoListArray)

        // 리사이클러뷰 레이아웃 설정
        val myLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        myLinearLayoutManager.stackFromEnd = true

        // 리사이클러 뷰 스와이프 설정
        val swipeHelperCallBack = SwipeHelperCallBack().apply {
            setClamp(152f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.todoListRecycler)

        // 리사이클러뷰 설정
        binding.todoListRecycler.apply {
            layoutManager = myLinearLayoutManager
            this.scrollToPosition(todoListRecyclerAdpter.itemCount - 1)
            adapter = todoListRecyclerAdpter
            setOnTouchListener { _, _ ->
                swipeHelperCallBack.removePreviousClamp(this)
                false
            }
        }

        getTodoListAll()

        // 라이브 데이타 옵저버 설정
        // 옵저빙 중인 데이터가 변경 시 함수 호출
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

    override fun onClickedTodoDeleteBtn(position: Int) {
        Log.d(TAG, "MainActivity - onClickedTodoDeleteBtn() ")
        try{
            Thread(Runnable {
                kotlin.run {
                    val todo = todoListArray[position].toDo
                    val date = todoListArray[position].toDoDate
                    val toId = todoListArray[position].toDoId
                    val todoListTable = TodoListTable(toId,todo,date)
                    todoListDb?.todoListDao()?.deleteTodoListItem(todoListTable)
                    getTodoListAll()
                }
            }).start()
        }catch (e: Exception){
            Log.d(TAG, "MainActivity - onClickedTodoDeleteBtn(): error : $e")
        }
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