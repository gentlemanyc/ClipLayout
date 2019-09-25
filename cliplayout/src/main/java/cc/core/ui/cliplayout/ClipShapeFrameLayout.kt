package cc.core.ui.cliplayout

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout


class ClipShapeFrameLayout : FrameLayout {

    val clipDelegate = BaseClipDelegate()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        clipDelegate.init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        clipDelegate.init(context, attrs)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clipDelegate.onMeasure(this)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        clipDelegate.onLayout(this)
    }

    override fun dispatchDraw(canvas: Canvas) {
        clipDelegate.draw(this, canvas)
        super.dispatchDraw(canvas)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): FrameLayout.LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams : FrameLayout.LayoutParams, ClipLayoutParams {
        override fun radius(): Float {
            return radius
        }

        override fun ignoreClip(): Boolean {
            return ignoreClip
        }

        @SuppressLint("CustomViewStyleable")
        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            if (attrs != null) {
                val ta = c.obtainStyledAttributes(attrs, R.styleable.ClipShapeFrameLayout)
                radius = ta.getDimension(R.styleable.ClipShapeFrameLayout_layout_shape_radius, 0F)
                ignoreClip =
                    ta.getBoolean(R.styleable.ClipShapeFrameLayout_layout_ignore_clip, false)
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
        /**
         * 不剪裁，默认都裁剪。
         */
        var ignoreClip = false
    }
}