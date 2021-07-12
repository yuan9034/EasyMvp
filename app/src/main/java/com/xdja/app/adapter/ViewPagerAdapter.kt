package com.xdja.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(var fragments: List<Fragment>,fm:FragmentManager):FragmentPagerAdapter(fm,
    BEHAVIOR_SET_USER_VISIBLE_HINT
) {

    override fun getCount(): Int =2

    override fun getItem(position: Int): Fragment= fragments[position]
}