package me.himanshusoni.chatmessageview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.util.AttributeSet


class JTextView : MTextViewBold {
    internal var left: Int = 0
    internal var top: Int = 0
    internal var right: Int = 0
    internal var bottom = 0
    private val paint = Paint()
    private var blocks: Array<String>? = null
    private var spaceOffset = 0f
    private var horizontalOffset = 0f
    private var verticalOffset = 0f
    private var horizontalFontOffset = 0f
    private var dirtyRegionWidth = 0f
    private var wrapEnabled = false
    private var _align = Align.RIGHT
    private var strecthOffset: Float = 0.toFloat()
    private var wrappedEdgeSpace: Float = 0.toFloat()
    private var block: String? = null
    private var wrappedLine: String? = null
    private var lineAsWords: Array<String>? = null
    private var wrappedObj: Array<Any>? = null

    private var cache: Bitmap? = null
    private var cacheEnabled = false

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        //set a minimum of left and right padding so that the texts are not too close to the side screen
        this.setPadding(10, 0, 10, 10)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.setPadding(10, 0, 10, 10)
    }

    constructor(context: Context) : super(context) {
        this.setPadding(10, 0, 10, 10)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        // TODO Auto-generated method stub

        super.setPadding(left + 10, top, right + 10, bottom + 10)
    }

    override fun setDrawingCacheEnabled(cacheEnabled: Boolean) {
        this.cacheEnabled = cacheEnabled
    }

    fun setText(st: String, wrap: Boolean) {
        wrapEnabled = wrap
        super.setText(st)
    }

    fun setTextAlign(align: Align) {
        _align = align
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        // If wrap is disabled then,
        // request original onDraw
        if (!wrapEnabled) {
            super.onDraw(canvas)
            return
        }

        // Active canas needs to be set
        // based on cacheEnabled
        var activeCanvas: Canvas?

        // Set the active canvas based on
        // whether cache is enabled
        if (cacheEnabled) {

            if (cache != null) {
                // Draw to the OS provided canvas
                // if the cache is not empty
                canvas.drawBitmap(cache!!, 0f, 0f, paint)
                return
            } else {
                // Create a bitmap and set the activeCanvas
                // to the one derived from the bitmap
                cache = Bitmap.createBitmap(
                        width, height,
                        Config.ARGB_4444
                )
                activeCanvas = Canvas(cache!!)
            }
        } else {
            // Active canvas is the OS
            // provided canvas
            activeCanvas = canvas
        }

        // Pull widget properties
        paint.color = currentTextColor
        paint.typeface = typeface
        paint.textSize = textSize
        paint.textAlign = _align
        paint.flags = Paint.ANTI_ALIAS_FLAG

        //minus out the paddings pixel
        dirtyRegionWidth = (width - paddingLeft - paddingRight).toFloat()
        var maxLines = Integer.MAX_VALUE
        val currentapiVersion = android.os.Build.VERSION.SDK_INT
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            maxLines = getMaxLines()
        }
        var lines = 1
        blocks = text.toString().split("((?<=\n)|(?=\n))".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        horizontalFontOffset = lineHeight - 0.5f
        verticalOffset = horizontalFontOffset // Temp fix
        spaceOffset = paint.measureText(" ")

        var i = 0
        while (i < blocks!!.size && lines <= maxLines) {
            block = blocks!![i]
            horizontalOffset = 0f

            if (block!!.length == 0) {
                i++
                continue
            } else if (block == "\n") {
                verticalOffset += horizontalFontOffset
                continue
            }

            block = block!!.trim { it <= ' ' }

            if (block!!.length == 0) {
                i++
                continue
            }
            wrappedObj = TextJustifyUtils.createWrappedLine(block!!, paint, spaceOffset, dirtyRegionWidth)

            wrappedLine = wrappedObj!![0] as String
            wrappedEdgeSpace = wrappedObj!![1] as Float
            lineAsWords = wrappedLine!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            strecthOffset =
                    if (wrappedEdgeSpace != java.lang.Float.MIN_VALUE) wrappedEdgeSpace / (lineAsWords!!.size - 1) else 0F
            for (j in lineAsWords!!.indices) {
                val word = lineAsWords!![j]
                if (lines == maxLines && j == lineAsWords!!.size - 1) {
                    activeCanvas.drawText("...", horizontalOffset, verticalOffset, paint)


                } else if (j == 0) {
                    //if it is the first word of the line, text will be drawn starting from right edge of textview
                    if (_align == Align.RIGHT) {
                        activeCanvas.drawText(word, (width - paddingRight).toFloat(), verticalOffset, paint)
                        // add in the paddings to the horizontalOffset
                        horizontalOffset += (width - paddingRight).toFloat()
                    } else {
                        activeCanvas.drawText(word, paddingLeft.toFloat(), verticalOffset, paint)
                        horizontalOffset += paddingLeft.toFloat()
                    }

                } else {
                    activeCanvas.drawText(word, horizontalOffset, verticalOffset, paint)
                }
                if (_align == Align.RIGHT)
                    horizontalOffset -= paint.measureText(word) + spaceOffset + strecthOffset
                else
                    horizontalOffset += paint.measureText(word) + spaceOffset + strecthOffset
            }

            lines++

            if (blocks!![i].length > 0) {
                blocks!![i] = blocks!![i].substring(wrappedLine!!.length)
                if (blocks!![i].length > 0) verticalOffset += horizontalFontOffset else verticalOffset += 0
                i--
            }
            i++
        }
        if (cacheEnabled) {
            // Draw the cache onto the OS provided
            // canvas.
            canvas.drawBitmap(cache!!, 0f, 0f, paint)
        }
    }
}