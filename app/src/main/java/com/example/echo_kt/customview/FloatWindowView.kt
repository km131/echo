package com.example.echo_kt.customview

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.example.echo_kt.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


/**
 * 悬浮窗
 */
class FloatWindowView : View {
    private val TAG = "FloatWindowView"

    private var _exampleString: String? = resources.getString(R.string.name)
    private var _exampleColor: Int = Color.RED
    private var _exampleDimension: Float = 24f

    private lateinit var textPaint: TextPaint
    private lateinit var mPaint: Paint
    private lateinit var mPaint2: Paint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f
    var exampleDrawable: Drawable = context.resources.getDrawable(R.mipmap.close, null)

    private var windowWidth: Int = 0
    private var windowHeight: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    //是否展开
    private var isUnfold = false
    private var globalRegion: Region? = null
    private var animProgress: Float = 0f
        set(value) {
            field = value
            Log.e(TAG, "animProgress: $animProgress")
            invalidate()
        }

    private var mPaddingLeft: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingRight: Int = 0
    private var mPaddingBottom: Int = 0

    private var contentWidth: Int = 0
    private var contentHeight: Int = 0

    //用于填充Path
    var mShader: Shader? = null

    private var itemList: MutableList<FloatItemBean>? = null
    private var itemRegionList: MutableList<Region>? = null
    private var itemClickList: MutableList<() -> Unit>? = null

    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
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

        _exampleString = a.getString(
            R.styleable.FloatWindowView_exampleString
        )
        _exampleColor = a.getColor(
            R.styleable.FloatWindowView_exampleColor,
            exampleColor
        )

        _exampleDimension = a.getDimension(
            R.styleable.FloatWindowView_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.FloatWindowView_exampleDrawable)) {
//            exampleDrawable = a.getDrawable(
//                R.styleable.FloatWindowView_exampleDrawable
//            )
            exampleDrawable.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }
        mPaint = Paint().apply { style = Paint.Style.FILL;color = Color.GREEN }
        mPaint2 = Paint().apply { style = Paint.Style.FILL;color = Color.RED }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    @SuppressLint("ServiceCast")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        windowHeight = context.resources?.displayMetrics?.heightPixels ?: 0
        windowWidth = context.resources?.displayMetrics?.widthPixels ?: 0
        mWidth = measuredWidth
        mHeight = measuredHeight
        Log.e(TAG, "onMeasure: $widthMeasureSpec,$heightMeasureSpec")
    }

    private fun invalidateTextPaintAndMeasurements() {
//        textPaint.let {
//            it.textSize = exampleDimension
//            it.color = exampleColor
//            textWidth = it.measureText(exampleString)
//            textHeight = it.fontMetrics.bottom
//        }
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawFilter = paintDrawFilter

        exampleString?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint
            )
        }

        if (isUnfold) {
            drawItem(canvas)
        }
        canvas.drawColor(Color.argb(30, 225, 225, 225))
        // Draw the example drawable on top of the text.
        exampleDrawable.let {
            if (!isUnfold) {
                it.setBounds(
                    paddingLeft, paddingTop,
                    contentWidth, contentHeight
                )
            } else {
                it.setBounds(
                    paddingLeft + 100, paddingTop + 100,
                    paddingLeft + 200, paddingTop + 200
                )
            }
            canvas.save()
            canvas.rotate(135 * animProgress, contentWidth / 2f, contentHeight / 2f)
            it.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isUnfold) {
                    if (itemList == null) {
                        itemList = mutableListOf(
                            FloatItemBean(
                                resources.getDrawable(R.mipmap.album, null),
                                null,
                                Path()
                            ),
                            FloatItemBean(ColorDrawable(Color.CYAN), null, Path()),
                            FloatItemBean(ColorDrawable(Color.YELLOW), null, Path()),
                            FloatItemBean(ColorDrawable(Color.RED), null, Path())
                        )
                    }
                    if (itemRegionList == null) {
                        itemRegionList = mutableListOf(
                            Region(), Region(), Region(), Region()
                        )
                    }
                    startAnim()
                } else {
                    val x = (event.x).toInt()
                    val y = (event.y).toInt()
                    if (x in 100..200 && y in 100..200) {
                        startAnim()
                        Log.i(TAG, "onDraw:开关 x:$x,y:$y")
                        return false
                    }
                    itemRegionList?.forEachIndexed { index, region ->
                        if (region.contains(x, y)) {
                            itemClickList?.run {
                                if (index < this.size) {
                                    this[index].invoke()
                                } else {
                                    Toast.makeText(context, "该Item未设置点击事件", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } ?: run {
                                Toast.makeText(this.context, "圆${index + 1}被点击", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            Log.i(TAG, "onDraw:itemIndex:$index x:$x,y:$y")
                            return false
                        }
                    }
                    Log.i(TAG, "onDraw:未触发有效点击事件 x:$x,y:$y")
                }
            }
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mPaddingLeft = paddingLeft
        mPaddingTop = paddingTop
        mPaddingRight = paddingRight
        mPaddingBottom = paddingBottom

        contentWidth = width - paddingLeft - paddingRight
        contentHeight = height - paddingTop - paddingBottom
        if (isUnfold) {
            globalRegion = Region(0, 0, w, h)
        }
    }

    //缩放动画
    private fun startAnim() {
        isUnfold = !isUnfold
        var v1 = 0f
        var v2 = 1f
        this.layoutParams.apply {
            if (isUnfold) {
                width *= 3
                height *= 3
            } else {
                width /= 3
                height /= 3
                v1 = 1f
                v2 = 0f
                itemList = null
                itemClickList = null
                itemRegionList = null
            }
        }
        val holder1 = PropertyValuesHolder.ofFloat("animProgress", v1, v2)

        val animator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(this, holder1)
        animator.duration = 500
        animator.start()
    }

    //绘制Item
    private fun drawItem(canvas: Canvas) {
        val itemLength: Int = itemList!!.size
        val v = 1f / itemLength.toFloat() * 360
        itemList!!.forEachIndexed { index, item ->
            // 150：圆心(view中心点) 到xy轴的距离
            // 110：item所在圆的半径
            val x = 150 + sin(v * (index + 1) * PI / 180) * 110 * animProgress
            val y = 150 - cos(v * (index + 1) * PI / 180) * 110 * animProgress
            item.apply {
                this.path.reset()
                this.path.addCircle(
                    x.toFloat(), y.toFloat(),
                    (contentWidth * 0.12 * animProgress).toFloat(),
                    Path.Direction.CW
                )
                globalRegion?.apply {
                    itemRegionList!![index].setPath(item.path, this)
                }
                this.drawable?.run {
                    val drawable = ShapeDrawable(OvalShape().apply {
                        drawable = this@run
                    })
                    val r = contentWidth * 0.12 * animProgress
                    drawable.setBounds((x - r).toInt(), (y - r).toInt(), (x + r).toInt(), (y + r).toInt())
                    mShader = BitmapShader(
                        this.toBitmap((r * 2 + 1).toInt(), (r * 2 + 1).toInt(), null),
                        Shader.TileMode.REPEAT,
                        Shader.TileMode.REPEAT
                    )
                    drawable.paint.shader = mShader
                    drawable.draw(canvas)
                }
            }
        }
    }

    fun setItemList(itemList: MutableList<FloatItemBean>) {
        this.itemList = itemList
    }

    fun setClickListener(itemClickList: MutableList<() -> Unit>) {
        this.itemClickList = itemClickList
    }
}