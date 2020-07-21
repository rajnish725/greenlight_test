package com.fycus.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(private var mSpace : Int ) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
       // super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view!!)
        if (position != 0) outRect.top = mSpace
    }


}