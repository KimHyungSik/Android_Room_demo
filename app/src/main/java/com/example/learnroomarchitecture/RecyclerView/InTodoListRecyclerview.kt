package com.example.learnroomarchitecture.RecyclerView

interface InTodoListRecyclerview {
    // 리스트 클릭시
    fun onClickedTodoItem(position: Int)
    // 삭제 버튼 클릭시
    fun onClickedTodoDeleteBtn(position: Int)
}