package cc.core.ui.cliplayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * All rights Reserved, Designed By yuanchao
 * @projectName ClipLayout
 * @package   cc.core.ui.cliplayout
 * @author 袁超
 * @date  19-9-25上午9:08
 */
class ClipDelegate {
    var pathList = mutableListOf<Pair<View, Path>>()
    var bgPath = Path()
    var bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var maskColor = Color.BLACK
    var mode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    var ignoreAll = false

    var clipType = Region.Op.DIFFERENCE

    fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ClipLayout)
            maskColor = ta.getColor(R.styleable.ClipLayout_clip_mask_color, Color.BLACK)
            ignoreAll = ta.getBoolean(R.styleable.ClipLayout_ignore_all_clip, false)
            val type = ta.getInt(R.styleable.ClipLayout_layout_clip_type, 0)
            mode = when (type) {
                0 -> PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
                1 -> PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                else ->  PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
            }
            ta.recycle()
        }
        bgPaint.color = maskColor
    }

    fun onMeasure(view: View) {
        bgPath.reset()
        bgPath.addRect(
            0F,
            0F,
            view.measuredWidth.toFloat(),
            view.measuredHeight.toFloat(),
            Path.Direction.CCW
        )
    }

    fun onLayout(viewGroup: ViewGroup) {
        if (pathList.isEmpty()) {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                val lp = child.layoutParams as ClipLayoutParams
                if (lp.ignoreClip()) {
                    continue
                }
                val radius = lp.radius()

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
                pathList.add(Pair(child, path))
            }
        }
    }

    fun draw(viewGroup: ViewGroup, canvas: Canvas) {
        if (ignoreAll) {
            return
        }
        if (viewGroup.isInEditMode) {
            pathList.forEachIndexed { index, pair ->
                if (pair.first.visibility == View.VISIBLE) {
                    canvas.clipPath(pair.second, clipType)
                }
            }
            canvas.drawColor(maskColor)
        } else {
            canvas.save()
            canvas.drawRect(
                viewGroup.left.toFloat(),
                viewGroup.top.toFloat(),
                viewGroup.right.toFloat(),
                viewGroup.bottom.toFloat(),
                bgPaint
            )
            bgPaint.xfermode = mode
            pathList.forEachIndexed { index, pair ->
                if (pair.first.visibility != View.GONE) {
                    canvas.drawPath(pair.second, bgPaint)
                }
            }
            bgPaint.xfermode = null
        }
    }
}

interface ClipLayoutParams {
    fun radius(): Float
    fun ignoreClip(): Boolean

    fun initAttrs(context: Context, attrs: AttributeSet?): Pair<Float, Boolean> {
        var radius = 0F
        var ignoreClip = false
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ClipLayout)
            radius = ta.getDimension(R.styleable.ClipLayout_layout_shape_radius, 0F)
            ignoreClip =
                ta.getBoolean(R.styleable.ClipLayout_layout_ignore_clip, false)
            ta.recycle()
        }
        return Pair(radius, ignoreClip)
    }
}