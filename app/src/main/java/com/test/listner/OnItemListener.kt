package com.test.listner
import android.view.View
import kotlin.String

/**
 * Created by rajnish yadav on 24 Jun 2020 at 18:34.
 */
interface OnItemListener {
    public fun onCellClickListener(
        from: String,
        view: View,
        position: Int,
        obj1: Any,
        obj2: Any,
        Ischeck: Boolean
    );
}