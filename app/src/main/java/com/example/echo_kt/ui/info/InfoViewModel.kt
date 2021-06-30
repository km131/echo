package com.example.echo_kt.ui.info

import androidx.lifecycle.ViewModel
import com.example.echo_kt.BaseApplication
import com.example.echo_kt.R
import com.example.echo_kt.data.ParagraphBean

class InfoViewModel:ViewModel() {
    val paragraphs: MutableList<ParagraphBean> by lazy {
        val s1 = ParagraphBean("功能介绍", BaseApplication.getContext().getString(R.string.app_function))
        val s2 = ParagraphBean(
            "使用事项",
            BaseApplication.getContext().getString(R.string.matter1) + "\n" +
                    BaseApplication.getContext().getString(R.string.matter2) + "\n" +
                    BaseApplication.getContext().getString(R.string.matter3) + "\n" +
                    BaseApplication.getContext().getString(R.string.matter4)
        )
        val s3 = ParagraphBean("关于软件", BaseApplication.getContext().getString(R.string.app_about))
        val s4 = ParagraphBean("免责声明", BaseApplication.getContext().getString(R.string.app_statement))
        val s5 = ParagraphBean("其他",BaseApplication.getContext().getString(R.string.app_else))
        mutableListOf(s1,s2,s3,s4,s5)
    }
}