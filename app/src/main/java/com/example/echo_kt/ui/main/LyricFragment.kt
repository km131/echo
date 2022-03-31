package com.example.echo_kt.ui.main

import android.graphics.Path
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.echo_kt.data.SongBean
import com.example.echo_kt.databinding.FragmentLyricBinding
import com.example.echo_kt.play.AudioObserver
import com.example.echo_kt.play.PlayerManager
import com.example.echo_kt.utils.getLrcList
import kotlinx.coroutines.launch

class LyricFragment : Fragment(), AudioObserver {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentLyricBinding? = null
    private val binding get() = _binding!!
    private var currentItemIndex: Int by mutableStateOf(
        0
    )
    private var timeList: MutableList<Long> by mutableStateOf(
        mutableListOf()
    )
    private var list: MutableList<Pair<Long, String>> by mutableStateOf(
        mutableListOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context?.let { PlayerManager.instance.register(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLyricBinding.inflate(inflater, container, false)
        initLyric()
        val view = binding.root
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface(
                        color = Color(255, 255, 255, 153),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {

                        var progress by remember {
                            mutableStateOf(0)
                        }
                        val listState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()

                        viewModel.playProgress.observe(viewLifecycleOwner, {
                            progress = it
                            var i = 0
                            for ((index, item) in timeList.withIndex()) {
                                if (progress >= item) {
                                    i = index
                                } else break
                            }
                            if (currentItemIndex != i) {
                                currentItemIndex = i
                                if (!isPressed) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(currentItemIndex - 5)
                                    }
                                }
                            }
                        })
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            LazyColumn(
                                modifier = Modifier, state = listState
                            ) {
                                itemsIndexed(list) { index: Int, item: Pair<Long, String> ->
                                    val startTime = item.first
                                    val endTime: Long =
                                        if (index != list.size - 1)
                                            list[index + 1].first
                                        else viewModel.maxProgress.get()!!.toLong()
                                    var lineProgress = 0f
                                    if (progress in startTime..endTime) {
                                        lineProgress =
                                            (progress - startTime).toFloat() / (endTime - startTime).toFloat()
                                    }

                                    Text(
                                        text = item.second,
                                        color = if (lineProgress == 0f) Color.DarkGray else Color.Blue,
                                        fontSize = 18.sp
                                    )
//                                    ProgressText(
//                                        modifier = Modifier.fillParentMaxWidth(),
//                                        lineProgress,
//                                        item.second
//                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    private fun initLyric() {
        currentItemIndex = 0
        list = getLrcList(viewModel.lyricStr.get()!!).toList().toMutableList()
        timeList = getLrcList(viewModel.lyricStr.get()!!).map { return@map it.key }.toMutableList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * @param progress 当前文本的播放进度
     */
    @Composable
    fun ProgressText(modifier: Modifier, progress: Float, text: String) {
        val textPaint by remember {
            mutableStateOf(Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                isDither = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 45f
                color = Color.DarkGray.toArgb()
            })
        }
        val textPaint2 by remember {
            mutableStateOf(Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                isDither = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 45f
                color = Color.Green.toArgb()
            })
        }
        Canvas(
            modifier = modifier
                .width(200.dp)
                .height(60.dp)
        ) {

            drawIntoCanvas {
                it.nativeCanvas.apply {
                    val textPath = Path()
                    val x = (width / 2).toFloat()
                    drawText(text, x, 60f, textPaint)
                    val progressPath = Path().apply {
                        addRect(
                            0f,
                            0f,
                            (width - textPaint.measureText(text)) / 2 + textPaint.measureText(text) * progress,
                            80f,
                            Path.Direction.CCW
                        )
                    }
                    textPaint.getTextPath(
                        text,
                        0,
                        text.length,
                        x,
                        60f,
                        textPath
                    )
                    progressPath.op(progressPath, textPath, Path.Op.INTERSECT)
                    drawPath(progressPath, textPaint2)
                }
            }
        }
    }

    override fun onReset() {
        viewModel.reset()
    }

    override fun onAudioBean(audioBean: SongBean) {
        super.onAudioBean(audioBean)
        initLyric()
    }
}