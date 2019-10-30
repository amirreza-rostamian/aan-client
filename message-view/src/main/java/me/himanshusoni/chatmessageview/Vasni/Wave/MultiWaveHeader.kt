package me.himanshusoni.chatmessageview.Vasni.Wave

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorRes
import android.support.v4.graphics.ColorUtils
import android.util.AttributeSet
import android.view.ViewGroup
import me.himanshusoni.chatmessageview.R
import me.himanshusoni.chatmessageview.Vasni.VasniSchema
import java.lang.Float.parseFloat
import java.util.*

/**
 * 多重水波纹
 * Created by SCWANG on 2017/12/11.
 */
class MultiWaveHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ViewGroup(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private val mMatrix = Matrix()
    private val mltWave = ArrayList<Wave>()
    private var mWaveHeight: Int = 0
    private var mStartColor: Int = 0
    private var mCloseColor: Int = 0
    private var mGradientAngle: Int = 0
    var isRunning: Boolean = false
        private set
    var velocity: Float = 0.toFloat()
    private var mColorAlpha: Float = 0.toFloat()
    private var mProgress: Float = 0.toFloat()
    private var mLastTime: Long = 0

    var waveHeight: Int
        get() = mWaveHeight
        set(waveHeight) {
            this.mWaveHeight = VasniSchema.instance.dp2px(waveHeight.toFloat())
            if (!mltWave.isEmpty()) {
                val thisView = this
                updateWavePath(thisView.width, thisView.height)
            }
        }

    var progress: Float
        get() = mProgress
        set(progress) {
            this.mProgress = progress
            if (mPaint != null) {
                val thisView = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    var gradientAngle: Int
        get() = mGradientAngle
        set(angle) {
            this.mGradientAngle = angle
            if (!mltWave.isEmpty()) {
                val thisView = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    var startColor: Int
        get() = mStartColor
        set(color) {
            this.mStartColor = color
            if (!mltWave.isEmpty()) {
                val thisView = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    var closeColor: Int
        get() = mCloseColor
        set(color) {
            this.mCloseColor = color
            if (!mltWave.isEmpty()) {
                val thisView = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    var colorAlpha: Float
        get() = mColorAlpha
        set(alpha) {
            this.mColorAlpha = alpha
            if (!mltWave.isEmpty()) {
                val thisView = this
                updateLinearGradient(thisView.width, thisView.height)
            }
        }

    init {

        mPaint.isAntiAlias = true

        val ta = context.obtainStyledAttributes(attrs, R.styleable.MultiWaveHeader)

        mWaveHeight =
                ta.getDimensionPixelOffset(R.styleable.MultiWaveHeader_mwhWaveHeight, VasniSchema.instance.dp2px(50f))
        mStartColor = ta.getColor(R.styleable.MultiWaveHeader_mwhStartColor, -0xfa9330)
        mCloseColor = ta.getColor(R.styleable.MultiWaveHeader_mwhCloseColor, -0xce5002)
        mColorAlpha = ta.getFloat(R.styleable.MultiWaveHeader_mwhColorAlpha, 0.45f)
        mProgress = ta.getFloat(R.styleable.MultiWaveHeader_mwhProgress, 1f)
        velocity = ta.getFloat(R.styleable.MultiWaveHeader_mwhVelocity, 1f)
        mGradientAngle = ta.getInt(R.styleable.MultiWaveHeader_mwhGradientAngle, 45)
        isRunning = ta.getBoolean(R.styleable.MultiWaveHeader_mwhIsRunning, true)

        if (ta.hasValue(R.styleable.MultiWaveHeader_mwhWaves)) {
            tag = ta.getString(R.styleable.MultiWaveHeader_mwhWaves)
        } else if (tag == null) {
            tag = "70,25,1.4,1.4,-26\n" +
                    "100,5,1.4,1.2,15\n" +
                    "420,0,1.15,1,-10\n" +
                    "520,10,1.7,1.5,20\n" +
                    "220,0,1,1,-15"
        }

        ta.recycle()
    }

    //    @Override
    //    protected void onFinishInflate() {
    //        super.onFinishInflate();
    //
    //        int count = getChildCount();
    //        if (count > 0) {
    //            for (int i = 0; i < count; i++) {
    //                View child = getChildAt(i);
    //                if (child instanceof Wave) {
    //                    child.setVisibility(GONE);
    //                } else {
    //                    throw new RuntimeException("只能用Wave作为子视图，You can only use Wave as a subview.");
    //                }
    //            }
    //        }
    //    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateWavePath(w, h)
        updateLinearGradient(w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mltWave.size > 0 && mPaint != null) {
            val thisView = this
            val height = thisView.height
            val thisTime = System.currentTimeMillis()
            for (wave in mltWave) {
                mMatrix.reset()
                canvas.save()
                if (mLastTime > 0 && wave.velocity != 0f) {
                    var offsetX = wave.offsetX - wave.velocity * velocity * (thisTime - mLastTime).toFloat() / 1000f
                    if (-wave.velocity > 0) {
                        offsetX %= (wave.width / 2).toFloat()
                    } else {
                        while (offsetX < 0) {
                            offsetX += (wave.width / 2).toFloat()
                        }
                    }
                    wave.offsetX = offsetX
                    mMatrix.setTranslate(offsetX, (1 - mProgress) * height)//wave.offsetX =
                    canvas.translate(-offsetX, -wave.offsetY - (1 - mProgress) * height)
                } else {
                    mMatrix.setTranslate(wave.offsetX, (1 - mProgress) * height)
                    canvas.translate(-wave.offsetX, -wave.offsetY - (1 - mProgress) * height)
                }
                mPaint.shader.setLocalMatrix(mMatrix)
                canvas.drawPath(wave.path, mPaint)
                canvas.restore()
            }
            mLastTime = thisTime
        }
        if (isRunning) {
            invalidate()
        }
    }

    private fun updateLinearGradient(width: Int, height: Int) {
        val startColor = ColorUtils.setAlphaComponent(mStartColor, (mColorAlpha * 255).toInt())
        val closeColor = ColorUtils.setAlphaComponent(mCloseColor, (mColorAlpha * 255).toInt())

        val w = width.toDouble()
        val h = (height * mProgress).toDouble()
        val r = Math.sqrt(w * w + h * h) / 2
        val y = r * Math.sin(2.0 * Math.PI * mGradientAngle.toDouble() / 360)
        val x = r * Math.cos(2.0 * Math.PI * mGradientAngle.toDouble() / 360)
        mPaint.shader = LinearGradient(
                (w / 2 - x).toInt().toFloat(),
                (h / 2 - y).toInt().toFloat(),
                (w / 2 + x).toInt().toFloat(),
                (h / 2 + y).toInt().toFloat(),
                startColor,
                closeColor,
                Shader.TileMode.CLAMP
        )
    }

    private fun updateWavePath(w: Int, h: Int) {

        mltWave.clear()

        /*int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Wave wave = (Wave) getChildAt(i);
                wave.updateWavePath(w, h, mWaveHeight);
                mltWave.add(wave);
            }
        } else */if (tag is String) {
            var waves = tag.toString().split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if ("-1" == tag) {
                waves = "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split(
                        "\\s+".toRegex()
                ).dropLastWhile { it.isEmpty() }.toTypedArray()
            } else if ("-2" == tag) {
                waves = "0,0,1,0.5,90\n90,0,1,0.5,90".split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
            }
            for (wave in waves) {
                val args = wave.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (args.size == 5) {
                    mltWave.add(
                            Wave(/*getContext(),*/VasniSchema.instance.dp2px(parseFloat(args[0])),
                                    VasniSchema.instance.dp2px(parseFloat(args[1])),
                                    VasniSchema.instance.dp2px(parseFloat(args[4])),
                                    parseFloat(args[2]),
                                    parseFloat(args[3]),
                                    w,
                                    h,
                                    mWaveHeight / 2
                            )
                    )
                }
            }
        } else {
            mltWave.add(
                    Wave(/*getContext(),*/VasniSchema.instance.dp2px(50f),
                            VasniSchema.instance.dp2px(0f),
                            VasniSchema.instance.dp2px(5f),
                            1.7f,
                            2f,
                            w,
                            h,
                            mWaveHeight / 2
                    )
            )
        }

    }

    fun setWaves(waves: String) {
        tag = waves
        if (mLastTime > 0) {
            val thisView = this
            updateWavePath(thisView.width, thisView.height)
        }
    }

    fun setStartColorId(@ColorRes colorId: Int) {
        val thisView = this
        startColor = VasniSchema.instance.getColor(thisView.context, colorId)
    }

    fun setCloseColorId(@ColorRes colorId: Int) {
        val thisView = this
        closeColor = VasniSchema.instance.getColor(thisView.context, colorId)
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            mLastTime = System.currentTimeMillis()
            invalidate()
        }
    }

    fun stop() {
        isRunning = false
    }
}
