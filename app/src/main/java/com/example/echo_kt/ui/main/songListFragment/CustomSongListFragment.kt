package com.example.echo_kt.ui.main.songListFragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.echo_kt.R
import com.example.echo_kt.adapter.SongListItemAdapter
import com.example.echo_kt.adapter.SongViewModel
import com.example.echo_kt.api.showToast
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.data.SongListBean
import com.example.echo_kt.databinding.FragmentCustomListBinding
import com.example.echo_kt.play.PlayList
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.room.AppDataBase
import com.example.echo_kt.room.PlaylistSongCrossRef
import com.example.echo_kt.ui.SongListDialogFragment
import com.example.echo_kt.ui.main.HomeViewModel
import com.example.echo_kt.utils.getDate
import com.example.echo_kt.utils.getMipmapToUri
import com.example.echo_kt.utils.getRandomColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomSongListFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private val args: CustomSongListFragmentArgs by navArgs()
    private var _binding: FragmentCustomListBinding? = null
    private val binding get() = _binding!!
    private lateinit var songList: SongListBean
    private lateinit var songs: MutableList<SongBean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomListBinding.inflate(inflater, container, false)
        initToolBar()
        return binding.root
    }

    //?????????????????????
    private fun initAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.albumView.text = songList.name[0].toString()
            val rgb = getRandomColor()
            binding.albumView.solid = Color.rgb(rgb[0], rgb[1], rgb[2])
        } else {
            binding.albumView.solid = Color.MAGENTA
        }
        binding.albumView.corner = 20f
    }

    private fun initToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            /**
             * ??????toolbar ????????????????????????
             */
            toolbar.setOnMenuItemClickListener { item ->
                val b = when (item.itemId) {
                    R.id.action_rest -> {
                        onCreateDialog()
                        true
                    }
                    R.id.action_playAll -> {
                        if (songs.size>0){
                            PlayList.instance.switchAudioList(songs.toMutableList())
                            PlayerManager.instance.startAll()
                        }
                        true
                    }
                    else -> false
                }
                b
            }
        }
    }

    private fun onCreateDialog() {
        val newFragment = SongListDialogFragment()
        newFragment.show(childFragmentManager, "?????????????????????")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //????????????????????????????????????????????????????????????
        viewModel.songlistIndex = args.index
        //-1??????????????????????????????
        if (args.index == -1) {
            songList =
                SongListBean("like", "????????????", getDate(), getMipmapToUri(R.drawable.ic_collect))
            binding.songListBean = songList
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.likeSongs.observe(viewLifecycleOwner, {
                    this@CustomSongListFragment.songs = it.toMutableList()
                    initListAdapter()
                })
            }
        } else {
            songList = viewModel.readCustomList(args.index)
            binding.songListBean = songList
            lifecycleScope.launch(Dispatchers.IO) {
                val s = AppDataBase.getInstance().customSongListDao()
                    .getPlaylistsWithSongs(songList.playlistId).asLiveData()
                withContext(Dispatchers.Main) {
                    s.observe(this@CustomSongListFragment.viewLifecycleOwner, {
                        this@CustomSongListFragment.songs = it.songs.toMutableList()
                        initListAdapter()
                    })
                }
            }
        }
        initAlbum()
    }

    private fun initListAdapter() {
        binding.rvSongList.adapter = SongListItemAdapter(songs.toMutableList()).apply {
            setOnItemClickListener(object : SongListItemAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val vm: SongViewModel by activityViewModels()
                    vm.audioBean.set(songs[position])
                    view.findNavController()
                        .navigate(R.id.action_customSongListFragment_to_bottomDialogFragment)
                }

                //???????????????????????????
                override fun onItemLongClick(view: View, position: Int) {
                    showDeleteDialog(position, songList)
                }
            })
        }
        binding.rvSongList.layoutManager = LinearLayoutManager(context)
    }

    private fun showDeleteDialog(position: Int, songList: SongListBean) {
        val normalDialog: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)
        normalDialog.setTitle("????????????????????????")
        normalDialog.setMessage(songs[position].songName)
        normalDialog.setPositiveButton(
            "??????"
        ) { _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                //-1???????????????
                if (args.index != -1) {
                    //???????????????
                    AppDataBase.getInstance().customSongListDao().deletePlaylistsWithSong(
                        PlaylistSongCrossRef(
                            playlistId = songList.playlistId,
                            id = songs[position].id
                        )
                    )
                    //??????????????????
                    AppDataBase.getInstance().customSongListDao()
                        .updateSongList(songList.apply { number -= 1 })
                } else AppDataBase.getInstance().historyAudioDao().deleteAudio(songs[position].id)
            }
            binding.rvSongList.adapter!!.notifyItemRemoved(position)
            showToast("????????????")
        }
        // ??????
        normalDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}