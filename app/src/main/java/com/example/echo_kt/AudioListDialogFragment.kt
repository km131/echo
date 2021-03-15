package com.example.echo_kt

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.echo_kt.databinding.AudioListDialogBinding
import com.example.echo_kt.databinding.AudioListDialogItemBinding
import com.example.echo_kt.ui.main.AudioBean
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.ui.main.AudioObserver
import com.example.echo_kt.ui.main.MainViewModel

const val ARG_ITEM_COUNT = "item_count"

class AudioListDialogFragment : BottomSheetDialogFragment(),AudioObserver {

    private var _binding: AudioListDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.context?.let { PlayerManager.instance.register(this) }
        _binding = AudioListDialogBinding.inflate(inflater, container, false)
        binding.bottomList.adapter = initAudioData()?.let { ItemAdapter(it) }
        binding.mainVm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }


    private inner class ViewHolder internal constructor(
        private val binding: AudioListDialogItemBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: AudioBean) {
            binding.apply {
                binding.pvm = item
                executePendingBindings()
            }
        }
        init {
            binding.playOnclick= View.OnClickListener {
                //PlayerManager.instance.getPlayList()
                PlayerManager.instance.play(binding.pvm)
            }
        }
    }


    private inner class ItemAdapter internal constructor(private var mList: MutableList<AudioBean>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(AudioListDialogItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(mList[position])

        }

        override fun getItemCount(): Int {
            return mList.size
        }
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

    /**
     * 获取音频列表数据
     */
    private fun initAudioData(): MutableList<AudioBean>? {
        return this.context?.let { PlayList.instance.readLocalPlayList(it) }
    }

    override fun onPlayMode(playMode: Int) {
        when (playMode) {
            PlayList.PlayMode.ORDER_PLAY_MODE ->{
                viewModel.playModePic.set(R.mipmap.play_order)
                viewModel.playModeText.set("顺序播放")
            }

            PlayList.PlayMode.RANDOM_PLAY_MODE ->{
                viewModel.playModePic.set(R.mipmap.play_random)
                viewModel.playModeText.set("随机播放")
            }
            PlayList.PlayMode.SINGLE_PLAY_MODE ->{
                viewModel.playModePic.set(R.mipmap.play_single)
                viewModel.playModeText.set("单曲循环")
            }
        }
    }

    override fun onReset() {
        viewModel.reset()
    }
}