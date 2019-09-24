package cc.core.ui.cliplayout

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.view.View


class ClipShapeFrameLayout : FrameLayout {
    private var pathList = mutableListOf<Path>()
    private var bgPath = Path()
    private var bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var maskColor = Color.BLACK

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ClipShapeFrameLayout)
            maskColor = ta.getColor(R.styleable.ClipShapeFrameLayout_clip_mask_color, Color.BLACK)
            ta.recycle()
        }
        bgPaint.color = maskColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bgPath.addRect(
            0F,
            0F,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            Path.Direction.CCW
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (pathList.isEmpty()) {
            for (i in 0 until childCount) {

                val child = getChildAt(i)
                val lp = child.layoutParams as LayoutParams
                val radius = lp.radius

                val path = Path()
                val rectLeft = child.left.toFloat()
                val rectRight = child.top.toFloat()

                val rect =
                    RectF(
                        rectLeft,
                        rectRight,
                        rectLeft + child.measuredWidth.toFloat(),
                        rectRight + child.measuredHeight.toFloat()
                    )

                if (radius == 0F) {
                    path.addRect(rect, Path.Direction.CCW)
                } else {
                    path.addRoundRect(
                        rect,
                        floatArrayOf(
                            radius, radius, radius, radius, radius, radius, radius, radius
                        ),
                        Path.Direction.CCW
                    )
                }
                pathList.add(path)
            }
        }
    }

    val mode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isInEditMode) {
            pathList.forEachIndexed { index, path ->
                canvas.clipPath(path, Region.Op.DIFFERENCE)
            }
            canvas.drawColor(maskColor)
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            canvas.save()
            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                bgPaint
            )
            bgPaint.xfermode = mode
            pathList.forEachIndexed { index, path ->
                canvas.drawPath(path, bgPaint)
            }
            bgPaint.xfermode = null
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): FrameLayout.LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams : FrameLayout.LayoutParams {
        @SuppressLint("CustomViewStyleable")
        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            if (attrs != null) {
                val ta = c.obtainStyledAttributes(attrs, R.styleable.ClipShapeFrameLayout)
                radius = ta.getDimension(R.styleable.ClipShapeFrameLayout_layout_shape_radius, 0F)
                ta.recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, gravity: Int) : super(width, height, gravity)
        constructor(source: ViewGroup.LayoutParams) : super(source)
        constructor(source: MarginLayoutParams) : super(source)
        @TargetApi(Build.VERSION_CODES.KITKAT)
        constructor(source: FrameLayout.LayoutParams) : super(source)

        var radius = 0f
    }
}