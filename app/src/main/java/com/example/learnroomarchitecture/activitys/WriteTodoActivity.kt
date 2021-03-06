package com.example.learnroomarchitecture.activitys

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.learnroomarchitecture.Utlis.toSimpleDateFomat
import com.example.learnroomarchitecture.dataBaseRoom.TodoListDatabase
import com.example.learnroomarchitecture.dataBaseRoom.TodoListTable
import com.example.learnroomarchitecture.databinding.ActivityWriteTodolistBinding
import java.lang.Exception
import java.util.*

class WriteTodoActivity: AppCompatActivity(), View.OnClickListener {

    private val TAG = "WriteTodoActivity로그"

    lateinit var binding: ActivityWriteTodolistBinding
    private var insertOrUpdate: Boolean = true
    private var todoListDb: TodoListDatabase? =null
    private var todoItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWriteTodolistBinding.inflate(layoutInflater)

        setContentView(binding.root)

        insertOrUpdate = intent.getBooleanExtra("insertData", false)
        todoItemId = intent.getIntExtra("todoItmeId", -1)
        val inputText = intent.getStringExtra("todoItem")
        binding.writeTodoEditText.setText(inputText)

        todoListDb = TodoListDatabase.getInstance(this)

        binding.cancelWriteTodoBtn.setOnClickListener(this)
        binding.writeTodoBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.cancelWriteTodoBtn->{
                val intent = Intent().apply {
                    putExtra("result", "cancel")
                }
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
            binding.writeTodoBtn->{
                if(insertOrUpdate){
                    Log.d(TAG, "WriteTodoActivity - onClick() : insert todo list")
                    insertTodoListDb()
                }else{
                    Log.d(TAG, "WriteTodoActivity - onClick() : update todo list")
                    updateTodoListDb()
                }
            }
        }
    }

    private fun insertTodoListDb(){
        val run = Runnable {
            try {
                val todo = binding.writeTodoEditText.text.toString()
                val date = Date().toSimpleDateFomat()
                val todoListTable = TodoListTable()

                todoListTable.toDo = todo
                todoListTable.toDoDate = date
                todoListDb?.todoListDao()?.insertTodoListItem(todoListTable)

                val intent = Intent().apply {
                    putExtra("result", "addData")
                }
                setResult(RESULT_OK, intent)
                finish()
            }catch (e: Exception){
                Log.d(TAG, "WriteTodoActivity - error : $e")
            }
        }
        val addThread = Thread(run)
        addThread.start()
    }

    private fun updateTodoListDb(){
        Thread(Runnable {
            try{
                val todo = binding.writeTodoEditText.text.toString()
                val date = Date().toSimpleDateFomat()
                val todoListTable = TodoListTable(todoItemId, todo, date)

                todoListDb?.todoListDao()?.updateTodoListItem(todoListTable)
                val intent = Intent().apply {
                    putExtra("result", "addData")
                }
                setResult(RESULT_OK, intent)
                finish()
            }catch (e: Exception){
                Log.d(TAG, "WriteTodoActivity - updateTodoListDb() : error : $e")
            }
        }).start()

    }
}