package com.example.learnroomarchitecture.RecyclerView

import android.graphics.Canvas
import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_list_recycler_item.view.*
import java.lang.Float.max
import java.lang.Float.min

class SwipeHelperCallBack: ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null    // 현재 선택된 viewHolder index
    private var previousPostion: Int? = null    // 이전에 선택한 viewHolder index
    private var currentDx = 0f
    private var clamp = 0f          // 고정 될 길이

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPostion = viewHolder.adapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ACTION_STATE_SWIPE){
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)
            currentDx = x
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                x,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // View의 가로 길이의 절반까지만 swipe 되도록
        val min: Float = -view.width.toFloat()/2
        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        // 이동 시킬 뷰
        return (viewHolder as TodolistRecyclerViewHolder).itemView.swipe_view
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // 현재 View가 고정되어 있지 않고 사용자가 -clamp 이상 swip시 isClamped ture로변경 아닐시 false로 변경
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean){
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean{
        // tag를 이용해서 선택 되었는지 확인
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setClamp(clamp:Float){
        // 초기에 고정 값 받기
        this.clamp = clamp
    }

    fun removePreviousClamp(recyclerView: RecyclerView){
        if (currentPosition == previousPostion)
            return
        previousPostion?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPostion = null
        }
    }
}

