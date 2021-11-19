package com.example.echo_kt.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

import android.view.MotionEvent




class FloatItemView:View {
    var mPaint: Paint? = null
    var globalRegion: Region? = null
    var circleRegion1: Region? = null
    var circleRegion2: Region? = null
    var circlePath1: Path? = null
    var circlePath2: Path? = null

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
    fun init(attrs: AttributeSet?, defStyle: Int){
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.GRAY

        // 用Path创建两个圆
        circlePath1 = Path()
        circlePath2 = Path()
        circlePath1!!.addCircle(100f, 100f, 50f, Path.Direction.CW)
        circlePath2!!.addCircle(400f, 400f, 50f, Path.Direction.CW)

        // 创建 Region
        circleRegion1 = Region()
        circleRegion2 = Region()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        globalRegion = Region(0, 0, w, h)
        //将Path添加到Region 中
        circleRegion1!!.setPath(circlePath1!!, globalRegion!!)
        circleRegion2!!.setPath(circlePath2!!, globalRegion!!)

    }

    override fun onDraw(canvas: Canvas?) {
        // 注意此处将全局变量转化为局部变量，方便 GC 回收 Canvas
        val circle1 = circlePath1!!
        val circle2 = circlePath2!!
        // 绘制两个圆
        canvas!!.drawPath(circle1, mPaint!!)
        canvas.drawPath(circle2, mPaint!!)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toInt()
                val y = event.y.toInt()

                // ▼点击区域判断
                if (circleRegion1!!.contains(x, y)) {
                    Toast.makeText(this.context, "圆1被点击", Toast.LENGTH_SHORT).show()
                }
                if (circleRegion2!!.contains(x, y)) {
                    Toast.makeText(this.context, "圆2被点击", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onTouchEvent(event)
    }
}