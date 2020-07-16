package com.xdja.easymvp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xdja.easymvp.widget.RecycleViewDivider
import org.jetbrains.anko.dip

/**
 * @author yuanwanli
 * @des   针对RecycleView的扩展
 * @date 2019/7/30
 */
//设置RecyclerView的方向
fun RecyclerView.setOrientation(context: Context, orientation: Int) {
    val linearLayoutManager = LinearLayoutManager(context)
    linearLayoutManager.orientation = orientation
    layoutManager = linearLayoutManager
}

//设置RecyclerView的水平分割线(灰色)
fun RecyclerView.setVerticalDivider(
    context: Context,
    dividerHeight: Int,
    colorResource: Int
) {
    val dip = dip(dividerHeight)
    val color = ContextCompat.getColor(context, colorResource)
    val divider = RecycleViewDivider(context, LinearLayoutManager.VERTICAL, dip, color)
    addItemDecoration(divider)
}