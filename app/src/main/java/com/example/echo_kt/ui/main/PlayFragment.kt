package com.example.echo_kt.ui.main

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.echo_kt.adapter.getGaussianBlurBitmap
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.FragmentPlayBinding
import com.example.echo_kt.play.AudioObserver
import com.example.echo_kt.play.PlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayFragment : Fragment(), AudioObserver {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //因背景图多为深色，故将状态栏颜色设置为白色
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            vp.adapter = PlayPagerAdapter(this@PlayFragment)
            ibBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        this.context?.let { PlayerManager.instance.unregister(this) }
        //根据是否为深色模式恢复状态栏之前的颜色
        requireActivity().window.decorView.systemUiVisibility =
            if (this.resources.configuration.uiMode == 0x11) {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        _binding = null
        super.onDestroy()
    }

    override fun onReset() {

    }

    override fun onAudioBean(audioBean: SongBean) {
        audioBean.getAlbumBitmap()?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                val s = BitmapDrawable(resources, getGaussianBlurBitmap(it, 10f, 20)).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.MULTIPLY)
                    } else {
                        setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
                    }
                }
                withContext(Dispatchers.Main) {
                    binding.cl.background = s
                }
            }
        }
    }
}

const val SONG_PAGE_INDEX = 0
const val LYRIC_PAGE_INDEX = 1

class PlayPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        SONG_PAGE_INDEX to { MusicFragment() },
        LYRIC_PAGE_INDEX to { LyricFragment() }
    )

    override fun getItemCount(): Int = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
