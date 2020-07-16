package com.xdja.easymvp.base.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author yuanwanli
 * @des   [Fragment]代理类,用于框架内部在每个Fragment的对应生命周期中插入需要的逻辑
 * @see FragmentDelegateImpl
 * @date 2020/6/23
 */
const val FRAGMENT_DELEGATE = "FRAGMENT_DELEGATE"

interface FragmentDelegate {
    fun onAttach(context: Context)
    fun onCreate(savedInstanceState: Bundle?)
    fun onCreateView(view: View, savedInstanceState: Bundle?)
    fun onActivityCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onSaveInstanceState(outState: Bundle)
    fun onDestroyView()
    fun onDestroy()
    fun onDetach()

    /**
     * Return true if the fragment is currently added to its activity.
     */
    fun isAdded(): Boolean
}