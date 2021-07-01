package com.example.echo_kt.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echo_kt.R
import com.example.echo_kt.api.showToast
import com.example.echo_kt.data.ErectBean
import com.example.echo_kt.data.ShowSearchBean
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.BottomDialogSongBinding
import com.example.echo_kt.databinding.ListItemSearchBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.room.AppDataBase
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongListItemAdapter(private var mList:  MutableList<SongBean>) : RecyclerView.Adapter<SongListItemAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int){}
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListItemAdapter.ViewHolder {
        return ViewHolder(
            ListItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: SongListItemAdapter.ViewHolder, position: Int) {
        val bean = mList[position]
        holder.bind(bean)
        holder.getBinding().apply {
            llPlay.setOnClickListener {
                PlayerManager.instance.playNewAudio(bean)
            }
            llPlay.setOnLongClickListener {
                onItemClickListener.onItemLongClick(holder.itemView, position)
                true
            }
            btnOther.setOnClickListener {
                onItemClickListener.onItemClick(holder.itemView, position)
            }
        }
    }
    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(private val binding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SongBean) {
            binding.apply {
                bean = ShowSearchBean(item.songName,item.author)
            }
        }
        fun getBinding():ListItemSearchBinding{
            return binding
        }
    }
}
class BottomDialogFragment : BottomSheetDialogFragment(){

    private lateinit var binding: BottomDialogSongBinding
    private val viewModel: SongViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogSongBinding.inflate(inflater, container, false)
        initOptionList()
        return binding.root
    }

    private fun initOptionList() {
        binding.tvSongName.text = viewModel.audioBean.get()!!.songName
        binding.tvSingerName.text = viewModel.audioBean.get()!!.author
        binding.rvItemList.adapter = MyErectAdapter(initErectAdapter()).apply {
            setOnItemClickListener(object : MyErectAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    when (position) {
                        0 -> {
                            viewModel.audioBean.get()?.let {
                                //添加到下一首播放
                                PlayList.instance.setNextPlay(it)
                                showToast("添加成功")
                                findNavController().navigateUp()
                            }?:showToast("SongListAdapter：106。添加失败,可能接口或网络出问题了")
                        }
                        1 -> {
                            //加到歌单
                            findNavController().navigate(R.id.action_bottomDialogFragment_to_addToPlayListDialog)
                        }
                        2 -> {
                            //下载(具体代码gitee上应该有)
                            showToast("考虑到版权问题，该功能被注释掉了")
                        }
                        3 -> {
                            //收藏
                            GlobalScope.launch {
                                AppDataBase.getInstance().songDao().updateSong(viewModel.audioBean.get()!!.apply {
                                    isLike = true
                                })
                            }
                            view.isClickable=false
                            showToast("收藏成功")
                        }
                    }
                }

                override fun onItemLongClick(view: View, position: Int) {

                }
            })
        }
        binding.rvItemList.layoutManager = LinearLayoutManager(this.context).apply{
            orientation=LinearLayoutManager.HORIZONTAL
        }
    }
    private fun initErectAdapter(): MutableList<ErectBean> {
        return mutableListOf(
            ErectBean(R.mipmap.add_next_play,"下一首播放"),
            ErectBean(R.mipmap.add_song_list,"加到歌单"),
            ErectBean(R.mipmap.download,"下载"),
            ErectBean(R.mipmap.no_collect,"收藏")
        )
    }
}
//放置当前操作的歌曲
class SongViewModel: ViewModel(){
    val audioBean = ObservableField<SongBean>()
}
