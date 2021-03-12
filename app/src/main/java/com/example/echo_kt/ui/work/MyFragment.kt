package com.example.echo_kt.ui.work

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.R
import com.example.echo_kt.adapter.MyAcrossAdapter
import com.example.echo_kt.adapter.MyBannerAdapter
import com.example.echo_kt.adapter.MyErectAdapter
import com.example.echo_kt.data.AcrossBean
import com.example.echo_kt.data.BannerBean
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.databinding.MyFragmentBinding
import com.youth.banner.indicator.CircleIndicator

class MyFragment : Fragment() {

    companion object {
        fun newInstance() = MyFragment()
    }

    private lateinit var viewModel: MyViewModel
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapterX = MyErectAdapter(initErectBean())
    private val adapterY = MyAcrossAdapter(initAcrossBean())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        initBanner()
        initRecycleView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(){
    }

    private fun initBanner() {
        val bannerList = arrayListOf<BannerBean>(
            BannerBean(R.mipmap.a1,"11111"),
            BannerBean(R.mipmap.a2,"22222"),
            BannerBean(R.mipmap.a12,"33333"),
            BannerBean(R.mipmap.a13,"44444")
        )
        binding.banner.addBannerLifecycleObserver(this)
        binding.banner.adapter = MyBannerAdapter(bannerList)
        binding.banner.indicator= CircleIndicator(this.context)
    }

    private fun initRecycleView(){
        val rvErect = binding.rvErect
        rvErect.adapter=adapterX
        rvErect.layoutManager=GridLayoutManager(context,3)
        val rvAcross = binding.rvAcross
        rvAcross.adapter=adapterY
        rvAcross.layoutManager=LinearLayoutManager(context)
    }
    private fun initErectBean(): MutableList<ErectBean> {
        val userList = mutableListOf<ErectBean>()
        for (i in 1..6) {
            var imageId:Int=0
            when (i){
                1 -> imageId=R.mipmap.n1
                2 -> imageId=R.mipmap.n2
                3 -> imageId=R.mipmap.n3
                4 -> imageId=R.mipmap.n4
                5 -> imageId=R.mipmap.n5
                6 -> imageId=R.mipmap.n6
            }
            userList.add(ErectBean(imageId,i.toString()))
        }
        return userList
    }
    private fun initAcrossBean(): MutableList<AcrossBean> {
        val list = mutableListOf<AcrossBean>()
        for (i in 1..3) {
            var imageId:Int =0
            when (i){
                1 -> imageId=R.mipmap.a1
                2 -> imageId=R.mipmap.a2
                3 -> imageId=R.mipmap.a11
            }
            list.add( AcrossBean(imageId))
        }
        return list
    }
}