package me.himanshusoni.chatmessageview.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import me.himanshusoni.chatmessageview.R

class ProgressView : View {

    private val paint = Paint()
    private var hexHeight: Int = 0
    private var hexWidth: Int = 0
    private var hexPadding = 0
    private var actualProgress = 0f
    var maxAlpha = MAX_ALPHA
    var animationTime = PROGRESS_TIME
    var color: Int = 0
    var isRainbow: Boolean = false
    var cornerRadius: Int = 0
    var isShrink: Boolean = false

    private var indeterminateAnimator: AnimatorSet? = null

    constructor(context: Context) : super(context) {}

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(
            context,
            attrs,
            defStyle
    ) {
        initAttributes(attrs, defStyle)
        initPaint()
    }

    private fun initAttributes(attrs: AttributeSet, defStyle: Int) {
        val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.ProgressView,
                defStyle, 0
        )
        animationTime = a.getInteger(R.styleable.ProgressView_hive_animDuration, PROGRESS_TIME)
        maxAlpha = a.getInteger(R.styleable.ProgressView_hive_maxAlpha, MAX_ALPHA)
        if (a.hasValue(R.styleable.ProgressView_hive_color)) {
            color = a.getColor(R.styleable.ProgressView_hive_color, Color.BLACK)
        }
        isRainbow = a.getBoolean(R.styleable.ProgressView_hive_rainbow, false)
        isShrink = a.getBoolean(R.styleable.ProgressView_hive_shrink, false)
        cornerRadius = a.getInteger(R.styleable.ProgressView_hive_cornerRadius, 0)
        a.recycle()
    }

    private fun initPaint() {
        paint.alpha = 0
        paint.pathEffect = CornerPathEffect(cornerRadius.toFloat())
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    override fun setVisibility(visibility: Int) {
        val currentVisibility = getVisibility()
        super.setVisibility(visibility)
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimator()
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation()
            }
        }
    }

    private fun startAnimation() {
        resetAnimator()
    }

    private fun stopAnimation() {
        actualProgress = 0f
        if (indeterminateAnimator != null) {
            indeterminateAnimator!!.cancel()
            indeterminateAnimator = null
        }
    }

    private fun resetAnimator() {
        if (indeterminateAnimator != null && indeterminateAnimator!!.isRunning) {
            indeterminateAnimator!!.cancel()
        }
        val progressAnimator = ValueAnimator.ofFloat(0f, MAX_PROGRESS_VALUE.toFloat())
        progressAnimator.duration = animationTime.toLong()
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.addUpdateListener { animation ->
            actualProgress = animation.animatedValue as Float
            invalidate()
        }
        indeterminateAnimator = AnimatorSet()
        indeterminateAnimator!!.play(progressAnimator)
        indeterminateAnimator!!.addListener(object : AnimatorListenerAdapter() {
            internal var wasCancelled = false

            override fun onAnimationCancel(animation: Animator) {
                wasCancelled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!wasCancelled) {
                    resetAnimator()
                }
            }
        })
        indeterminateAnimator!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        hexWidth = viewWidth / 3
        hexHeight = viewWidth * 2 / 5
        hexPadding = viewWidth / 23
        setMeasuredDimension(viewWidth, viewWidth)
    }

    override fun onDraw(canvas: Canvas) {
        var alpha = getAlpha(1, actualProgress)
        paint.color = getHexagonColor(1)
        paint.alpha = alpha
        var hexPath = hiveRect(
                hexWidth / 2, hexPadding, hexWidth * 3 / 2, hexHeight + hexPadding,
                alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(2, actualProgress)
        paint.color = getHexagonColor(2)
        paint.alpha = alpha
        hexPath = hiveRect(
                hexWidth * 3 / 2, hexPadding, hexWidth * 5 / 2, hexHeight + hexPadding,
                alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(6, actualProgress)
        paint.color = getHexagonColor(6)
        paint.alpha = alpha
        hexPath = hiveRect(
                0, hexHeight * 3 / 4 + hexPadding, hexWidth,
                hexHeight * 7 / 4 + hexPadding, alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(7, actualProgress)
        paint.color = getHexagonColor(7)
        paint.alpha = alpha
        hexPath = hiveRect(
                hexWidth, hexHeight * 3 / 4 + hexPadding, hexWidth * 2,
                hexHeight * 7 / 4 + hexPadding, alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(3, actualProgress)
        paint.color = getHexagonColor(3)
        paint.alpha = alpha
        hexPath = hiveRect(
                hexWidth * 2, hexHeight * 3 / 4 + hexPadding, hexWidth * 3,
                hexHeight * 7 / 4 + hexPadding, alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(5, actualProgress)
        paint.color = getHexagonColor(5)
        paint.alpha = alpha
        hexPath = hiveRect(
                hexWidth / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 3 / 2,
                hexHeight * 10 / 4 + hexPadding, alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)

        alpha = getAlpha(4, actualProgress)
        paint.color = getHexagonColor(4)
        paint.alpha = alpha
        hexPath = hiveRect(
                hexWidth * 3 / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 5 / 2,
                hexHeight * 10 / 4 + hexPadding, alpha.toFloat() / maxAlpha
        )
        canvas.drawPath(hexPath, paint)
    }

    private fun getHexagonColor(position: Int): Int {
        return if (isRainbow && position <= rainbowColor.size) {
            rainbowColor[position - 1]
        } else {
            color
        }
    }

    private fun getAlpha(num: Int, progress: Float): Int {
        var alpha: Float
        if (progress > num * 100) {
            alpha = maxAlpha.toFloat()
        } else {
            val min = (num - 1) * 100
            alpha = if (progress - min > 0) progress - min else 0f
            alpha = alpha * maxAlpha / 100
        }
        if (progress > 700) {
            val fadeProgress = progress - 700
            if (fadeProgress > num * 100) {
                alpha = 0f
            } else {
                val min = (num - 1) * 100
                alpha = if (fadeProgress - min > 0) fadeProgress - min else 0f
                alpha = maxAlpha - alpha * maxAlpha / 100
            }
        }
        if (progress > 1400) {
            alpha = 0f
        }
        return alpha.toInt()
    }

    private fun hiveRect(left: Int, top: Int, right: Int, bottom: Int, percentage: Float): Path {
        val path = Path()
        val height = Math.abs(bottom - top)
        val width = Math.abs(right - left)
        var r = if (width > height) height else width
        r = r / 2
        var y = top
        if (isShrink) {
            y = top + (r - percentage * r).toInt()
            r = (percentage * r).toInt()
        }
        var x = (right - left) / 2 + left
        val edge = (r * Math.sqrt(3.0) / 2).toInt()
        path.moveTo(x.toFloat(), y.toFloat())
        x = x + edge
        y = y + r / 2
        path.lineTo(x.toFloat(), y.toFloat())
        y = y + r
        path.lineTo(x.toFloat(), y.toFloat())
        x = x - edge
        y = y + r / 2
        path.lineTo(x.toFloat(), y.toFloat())
        x = x - edge
        y = y - r / 2
        path.lineTo(x.toFloat(), y.toFloat())
        y = y - r
        path.lineTo(x.toFloat(), y.toFloat())
        path.close()
        return path
    }

    companion object {

        private val rainbowColor = intArrayOf(0xFF0E52, 0x29C034, 0xF39B03, 0x00B0F2, 0xFF0E52, 0x29C034, 0xFDC50B)

        private val MAX_PROGRESS_VALUE = 1450
        private val PROGRESS_TIME = 2000
        private val MAX_ALPHA = 70
    }
}
