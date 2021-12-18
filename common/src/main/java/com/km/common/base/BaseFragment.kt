package com.km.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.km.common.adapter.MainPagerAdapter


abstract class BaseFragment<B : ViewDataBinding> : Fragment() {
    private var _binding: B? = null
    protected val mBinding get() = _binding!!
    protected val TAG: String = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        initData()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClick()
    }

    protected abstract fun initData()

    protected abstract fun getLayout(): Int
    protected abstract fun initOnClick()
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected open fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    protected open fun showEditTextDialog(title: String, confirm: (newPsd: String) -> Unit) {
        val editText = EditText(this.requireContext()).apply {
            maxLines = 1
            inputType = EditorInfo.TYPE_CLASS_TEXT
        }
        val inputDialog: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        inputDialog.apply {
            setTitle(title).setView(editText)
            setPositiveButton("确定") { _, _ -> confirm.invoke(editText.text.toString()) }
            setNegativeButton("取消") { _, _ -> }
        }.show()
    }
    protected open fun initNav(vp:ViewPager2,navView:BottomNavigationView,fragmentMap:Map<Int,()->Fragment>){
        vp.adapter = MainPagerAdapter(this,fragmentMap)
        vp.isUserInputEnabled = false
        navView.run {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
//                    R.id.menu_home -> vp.setCurrentItem(0, false)
//                    R.id.menu_video -> vp.setCurrentItem(1, false)
//                    R.id.menu_info -> vp.setCurrentItem(2, false)
                }
                // 这里注意返回true,否则点击失效
                true
            }
        }
    }
}