package com.example.echo_kt.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.echo_kt.R


class CircularProgressBar : View {
    /** 实线画笔大小 dp  */
    private var solidCircleWidth = 3f
    private var solidCircleColor = Color.GREEN
    private var mWidth = 0
    private var mHeight = 0
    private var progress: Float = 0f
    private lateinit var bitmap: Bitmap

    private lateinit var paint: Paint
    private val shader = RadialGradient(
        0f, 0f, 100f,
        Color.parseColor("#FF3D3D"),
        Color.parseColor("#FFFFFF"),
        Shader.TileMode.CLAMP
    )
    private val blurMaskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER)
    private val embossMaskFilter = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.2f, 8f, 10f)

    constructor(context: Context?) : super(context) {
        init(getContext())
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray: TypedArray =
            this.context.obtainStyledAttributes(attrs, R.styleable.DemoView)
        typedArray.getDrawable(R.styleable.DemoView_imageUrl)?.apply {
            bitmap = this.toBitmap()
        }
        solidCircleColor = typedArray.getColor(R.styleable.DemoView_progressBarColor, Color.GREEN)
        solidCircleWidth = typedArray.getFloat(R.styleable.DemoView_solidCircleWidth, 5f)
        typedArray.recycle()
        init(getContext())
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray: TypedArray =
            this.context.obtainStyledAttributes(attrs, R.styleable.DemoView)
//        imageRes = typedArray.getIndex(R.attr.imageUrl)
//        solidCircleColor = typedArray.getIndex(R.attr.progressBarColor)
        solidCircleWidth = typedArray.getIndex(R.attr.solidCircleWidth).toFloat()
        typedArray.recycle()
        init(getContext())
    }

    private fun init(context: Context) {
        paint = Paint()
        paint.apply {
            color = solidCircleColor
            shader = this@CircularProgressBar.shader
            strokeWidth = DensityUtils.spConvertToPx(context, solidCircleWidth)
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            maskFilter = blurMaskFilter
            //阴影
            //setShadowLayer(5f, 0f, 0f, Color.GRAY)
        }
        if (this::bitmap.isInitialized) {
            setBitmap(bitmap)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        init(context)
//        canvas!!.apply {
//            paint.isAntiAlias = true
//            paint.style = Paint.Style.FILL_AND_STROKE
//            paint.textSize = 100f
//            //paint.typeface = Typeface.createFromAsset(context.assets,"comic.ttf")
//            paint.shader = shader
//            paint.strokeWidth = 3F
//            paint.textSkewX = -0.2f
//            paint.isStrikeThruText = true
//            paint.isUnderlineText = true
//            drawCircle(300F, 300F, 100F, paint)
//            drawRoundRect(700F, 500F, 100F, 1500F, 50F, 50F, paint)
//            drawText("🐲竜神の剣を喰らえ⚔", 10f, 1100f, paint)
//            paint.style = Paint.Style.STROKE
//            paint.strokeWidth = 10F
//            drawOval(500f, 100f, 700f, 200f, paint)
//            drawArc(100f, 1100f, 500f, 1300f, 0f, 90f, false, paint)
//            drawArc(600f, 400f, 800f, 900f, 180f, 60f, true, paint)
//            drawCircle(800f, 800f, 100F, paint)
//            drawLine(60
//            0f, 800f, 1000f, 800f, paint)
//            drawLine(800f, 600f, 800f, 1000f, paint)
//            paint.setShadowLayer(10f, 0f, 0f, Color.GRAY)
//            paint.maskFilter = embossMaskFilter
//        }
        if (this@CircularProgressBar::bitmap.isInitialized) {
            canvas!!.drawBitmap(
                bitmap,
                mWidth * 0.3f - 14,
                mHeight * 0.5f - mWidth * 0.2f - 14,
                paint
            )
        }
        drawProgressCircle(canvas!!)
    }

    private fun drawProgressCircle(canvas: Canvas) {
        canvas.drawArc(
            mWidth * 0.1f,
            mHeight * 0.5f - mWidth * 0.4f,
            mWidth * 0.9f,
            mHeight * 0.5f + mWidth * 0.4f,
            270f,
            progress * 360,
            false,
            paint
        )
    }

    private fun drawImage(canvas: Canvas) {
        if (this@CircularProgressBar::bitmap.isInitialized) {
            canvas.drawBitmap(
                bitmap,
                mWidth * 0.3f - 14,
                mHeight * 0.5f - mWidth * 0.2f - 14,
                paint
            )
        }
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.width, Matrix().apply {
            postScale(1.5f, 1.5f)
        }, true)
        invalidate()
    }
}