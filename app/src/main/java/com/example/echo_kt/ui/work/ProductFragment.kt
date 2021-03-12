package com.example.echo_kt.ui.work

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.R
import com.example.echo_kt.data.ProductBean
import com.example.echo_kt.databinding.FragmentProductBinding

/**
 * A fragment representing a list of Items.
 */
class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val rv = binding.list
        rv.adapter = ProductAdapter(initValues())
        rv.layoutManager = LinearLayoutManager(this.context)
        return binding.root
    }

    private fun initValues(): List<ProductBean> {
        val string="测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测"
        return listOf(
            ProductBean(R.mipmap.e2),
            ProductBean(string),
            ProductBean(R.mipmap.f1),
            ProductBean(string),
            ProductBean(R.mipmap.e2),
            ProductBean(R.mipmap.f2),
            ProductBean(string),
            ProductBean(R.mipmap.h1),
            ProductBean(string)
        )
    }
}