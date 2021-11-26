package com.example.echo_kt.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.echo_kt.R
import com.example.echo_kt.adapter.MyErectAdapter
import com.example.echo_kt.adapter.SongListAdapter
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.HomeFragmentBinding
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.utils.getDate
import com.example.echo_kt.utils.getMipmapToUri
import com.example.echo_kt.utils.getSongListId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding:HomeFragmentBinding? = null
    private val binding get() = _binding!!
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
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater,container,false)
        initRvHome()
        initSongList()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    private fun initSongList() {
        binding.vmHome = viewModel
        // 观察LiveData，将这个活动作为LifecycleOwner和observer传递进来。
        viewModel.songList.observe(this.viewLifecycleOwner, { data ->
            // update UI
            adapterSongList = SongListAdapter(data).apply {
                setOnItemClickListener(object : SongListAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val action =
                            MainFragmentDirections.actionMainFragmentToCustomSongListFragment(
                                position
                            )
                        findNavController().navigate(action)

                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        showDialog(data[position])
                    }
                })
            }
            binding.rvMySongList.adapter = adapterSongList
        })
    }

    private fun initRvHome() {
        binding.rvHome.adapter = MyErectAdapter(initErectAdapter()).apply {
            setOnItemClickListener(object : MyErectAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    when (position) {
                            0 -> view.findNavController().navigate(R.id.action_mainFragment_to_localSongFragment)
                            1 -> {
                                val action =
                                    MainFragmentDirections.actionMainFragmentToCustomSongListFragment(-1)
                                findNavController().navigate(action)
                            }
                            2 -> view.findNavController().navigate(R.id.action_mainFragment_to_historySongFragment)
                            else -> R.id.action_mainFragment_to_localSongFragment
                        }
                }

                override fun onItemLongClick(view: View, position: Int) {}
            })
        }
        binding.rvHome.layoutManager = GridLayoutManager(this.context,3)
    }

    private fun showDialog(bean:SongListBean) {
        val normalDialog: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)
        normalDialog.setTitle("确定删除该歌单？")
        normalDialog.setMessage(bean.name)
        normalDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            GlobalScope.launch {
                AppDataBase.getInstance().customSongListDao()
                    .deleteSongList(bean.id)
            }
            Toast.makeText(binding.root.context, "删除成功", Toast.LENGTH_SHORT).show()
        }
        // 显示
        normalDialog.show()
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
        inputDialog.setTitle("输入歌单名字").setView(editText)
        inputDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            if (editText.text.toString()!=""){
                val date= getDate()
                val songListBean = SongListBean(
                    id = getSongListId(editText.text.toString(),date),
                    name = editText.text.toString(),
                    date = date,
                    //暂时如此,可扩展
                    coverImage = getMipmapToUri(R.mipmap.echo)
                )
                GlobalScope.launch {
                    //存入数据库
                    AppDataBase.getInstance().customSongListDao().insertSongList(songListBean)
                }
            }
        }.show()
    }
}