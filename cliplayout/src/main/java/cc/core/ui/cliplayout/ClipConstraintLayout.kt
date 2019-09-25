package cc.core.ui.cliplayout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * All rights Reserved, Designed By yuanchao
 * @projectName ClipLayout
 * @package   cc.core.ui.cliplayout
 * @author 袁超
 * @date  19-9-25上午9:43
 */
class ClipConstraintLayout : ConstraintLayout {
    var clipDelegate = ClipDelegate()

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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        clipDelegate.onLayout(this)
    }

    override fun dispatchDraw(canvas: Canvas) {
        clipDelegate.draw(this, canvas)
        super.dispatchDraw(canvas)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ConstraintLayout.LayoutParams {
        return LayoutParams(context, attrs)
    }

    class LayoutParams : ConstraintLayout.LayoutParams, ClipLayoutParams {
        var pair: Pair<Float, Boolean>? = null
        override fun radius(): Float {
            return pair?.first ?: 0F
        }

        override fun ignoreClip(): Boolean {
            return pair?.second ?: false
        }

        constructor(source: ConstraintLayout.LayoutParams?) : super(source)
        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            pair = initAttrs(c, attrs)
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }
}