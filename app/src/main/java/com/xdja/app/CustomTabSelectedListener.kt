package com.xdja.app

import com.google.android.material.tabs.TabLayout

abstract  class CustomTabSelectedListener : TabLayout.OnTabSelectedListener{

    abstract fun onTabSelected(tabText: String)

    override fun onTabSelected(tab: TabLayout.Tab) {
        this.onTabSelected(tab.text.toString())
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}