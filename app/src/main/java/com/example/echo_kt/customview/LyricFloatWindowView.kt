package com.example.echo_kt.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.echo_kt.R

class LyricFloatWindowView : View {
    private val TAG = "LyricFloatWindowView"
    private var windowWidth: Int = 0
    private var windowHeight: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mPaddingLeft: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingRight: Int = 0
    private var mPaddingBottom: Int = 0

    private lateinit var textPaint: TextPaint
    private var mTextSize = 0
    private var mTextColor = Color.GREEN

    //悬浮窗垂直位置
    private var mVerticalPosition:Float = 0f

    //歌词垂直间距
    private val mLyricVerticalPadding = 10
    private var lyric1 = ""
        set(value) {
            field = value
        }
    private var lyric2 = ""
        set(value) {
            field = value
        }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FloatWindowView, defStyle, 0
        )
        textPaint = TextPaint().apply {
            textSize = mTextSize.toFloat()
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        windowHeight = context.resources?.displayMetrics?.heightPixels ?: 0
        windowWidth = context.resources?.displayMetrics?.widthPixels ?: 0
        mWidth = measuredWidth
        mHeight = measuredHeight
        Log.e(TAG, "onMeasure: $widthMeasureSpec,$heightMeasureSpec")
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawColor(getResources().getColor(R.color.gray_400))
            drawText(
                lyric1, (paddingLeft + mWidth / 2).toFloat(),
                (paddingTop + mHeight / 2).toFloat(), textPaint
            )
            drawText(
                lyric2, (paddingLeft + mWidth / 2).toFloat(),
                (paddingTop + mHeight / 2 + mLyricVerticalPadding).toFloat(), textPaint
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "onTouchEvent: ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE ->{
                Log.i(TAG, "onTouchEvent: ACTION_MOVE")
                //拖动控件时只改变控件垂直位置
                mVerticalPosition = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP ->{
                Log.i(TAG, "onTouchEvent: ACTION_UP")

            }
        }
        return super.onTouchEvent(event);
    }
}