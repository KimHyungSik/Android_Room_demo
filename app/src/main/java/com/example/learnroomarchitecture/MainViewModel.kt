package com.example.learnroomarchitecture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnroomarchitecture.dataBaseRoom.TodoListTable

// 데이터 변경
// 뷰모델은 데이터와 변경사항을 알려주는 데이터를 가지고 있다
class MainViewModel:ViewModel() {

    companion object{
        private const val TAG = "MainViewModel로그"
    }

    // 뮤터블 라이브 데이터 - 수정 가능함
    // 라이브 데이터 - 값변동 안됨

    //
    private val _currentValue = MutableLiveData<ArrayList<TodoListTable>>()

    val currentValue : LiveData<ArrayList<TodoListTable>>
        get() = _currentValue

    init{
        Log.d(TAG, "MainViewModel - 생성자 호출")
    }

    fun updateValue(todoList: ArrayList<TodoListTable>){
        _currentValue.postValue(todoList)
    }
}