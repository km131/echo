package com.example.echo_kt

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.api.showToast
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.AudioListDialogBinding
import com.example.echo_kt.databinding.AudioListDialogItemBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.play.AudioObserver
import com.example.echo_kt.ui.main.MainViewModel

const val ARG_ITEM_COUNT = "item_count"

class AudioListDialogFragment : BottomSheetDialogFragment(),
    AudioObserver {

    private var _binding: AudioListDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.context?.let { PlayerManager.instance.register(this) }
        _binding = AudioListDialogBinding.inflate(inflater, container, false)
        binding.bottomList.adapter = PlayerManager.instance.getPlayList().let {
            ItemAdapter(it).apply {
                setOnItemClickListener(object :ItemAdapter.OnItemClickListener{
                    override fun onItemClickPlay(view: View, position: Int) {
                        PlayerManager.instance.playNewAudio(it[position])

                    }

                    override fun onItemClickRemove(view: View, position: Int) {
                        it.removeAt(position)
                        binding.bottomList.adapter!!.notifyDataSetChanged()
                        showToast("已删除")
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
        // 将列表滑动到正在播放的歌曲
        if (PlayerManager.instance.getPlayList().size > 0) {
            (binding.bottomList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                PlayList.instance.getIndex(),
                0
            )
        }
        binding.mainVm = viewModel
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    override fun onAudioBean(audioBean: SongBean) {
        if (_binding != null)
            binding.bottomList.adapter?.notifyDataSetChanged()
    }

    override fun onReset() {
        viewModel.reset()
    }
    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int): AudioListDialogFragment =
            AudioListDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }

    override fun onPlayMode(playMode: Int) {
        when (playMode) {
            PlayList.PlayMode.ORDER_PLAY_MODE -> {
                viewModel.playModePic.set(R.mipmap.order)
                viewModel.playModeText.set("顺序播放")
            }
            PlayList.PlayMode.RANDOM_PLAY_MODE ->{
                viewModel.playModePic.set(R.mipmap.random)
                viewModel.playModeText.set("随机播放")
            }
            PlayList.PlayMode.SINGLE_PLAY_MODE ->{
                viewModel.playModePic.set(R.mipmap.single)
                viewModel.playModeText.set("单曲循环")
            }
        }
    }
    class ViewHolder internal constructor(
        private val binding: AudioListDialogItemBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: SongBean) {
            binding.apply {
                binding.pvm = item
                executePendingBindings()
            }
        }
        fun getBind():AudioListDialogItemBinding{
            return binding
        }
    }

}
class ItemAdapter internal constructor(private var mList: MutableList<SongBean>) :
    RecyclerView.Adapter<AudioListDialogFragment.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClickPlay(view: View, position: Int)
        fun onItemClickRemove(view: View, position: Int)
        //TODO 长按拖动到指定位置
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioListDialogFragment.ViewHolder {
        return AudioListDialogFragment.ViewHolder(
            AudioListDialogItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AudioListDialogFragment.ViewHolder, position: Int) {
        holder.bind(mList[position])
        holder.getBind().clItem.setOnClickListener {
            onItemClickListener.onItemClickPlay(holder.itemView,position)
        }
        holder.getBind().imageRemove.setOnClickListener {
            onItemClickListener.onItemClickRemove(holder.itemView,position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}