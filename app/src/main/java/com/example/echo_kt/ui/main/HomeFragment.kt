package com.example.echo_kt.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.echo_kt.R
import com.example.echo_kt.adapter.MyErectAdapter
import com.example.echo_kt.adapter.SongListAdapter
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.HomeFragmentBinding
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.util.getDate
import com.example.echo_kt.util.getSongListId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding:HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter= MyErectAdapter(initErectAdapter())
    private lateinit var adapterSongList:SongListAdapter

    private fun initErectAdapter(): MutableList<ErectBean> {
        return mutableListOf(
            ErectBean(R.mipmap.localmusic,"本地音乐"),
            ErectBean(R.mipmap.lovemusic,"我喜欢的"),
            ErectBean(R.mipmap.historymusic,"最近播放")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.vmHome=viewModel
        binding.rvHome.adapter=adapter
        if (viewModel.songList.value!=null){
            adapterSongList=SongListAdapter(viewModel.songList.value!!)
            binding.rvMySongList.adapter=adapterSongList
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun onClick(){
        binding.ibtSetting.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_settingsFragment)
        }
        binding.setJumpSearch {
            NavHostFragment.findNavController(this).navigate(R.id.action_mainFragment_to_searchFragment)
        }
        binding.setNewSongList {
            showDialog()
        }
    }

    private fun showDialog() {
        val editText = EditText(this.context)
        val inputDialog: AlertDialog.Builder = AlertDialog.Builder(this.context)
        inputDialog.setTitle("输入歌单名字,别乱来").setView(editText)
        inputDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            if (editText.text.toString()!=""){
                val date= getDate()
                val songListBean = SongListBean(
                    id = getSongListId(editText.text.toString(),date),
                    name = editText.text.toString(),
                    date = date,
                    list = mutableListOf(),
                    coverImage = "https://images6.alphacoders.com/735/thumb-350-735055.jpg"
                )
                //存入数据库
                GlobalScope.launch {
                    AppDataBase.getInstance().customSongListDao().insertSongList(songListBean)
                }
            }
        }.show()
    }
}