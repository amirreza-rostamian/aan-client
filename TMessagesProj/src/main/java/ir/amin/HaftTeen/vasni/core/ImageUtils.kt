package ir.amin.HaftTeen.vasni.core;

import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.annotation.IntRange
import android.view.View
import java.io.*

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/08/12
 * desc  : utils about image
</pre> *
 */
class ImageUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        /**
         * Bitmap to bytes.
         *
         * @param bitmap The bitmap.
         * @param format The format of bitmap.
         * @return bytes
         */
        fun bitmap2Bytes(bitmap: Bitmap?, format: CompressFormat): ByteArray? {
            if (bitmap == null) return null
            val baos = ByteArrayOutputStream()
            bitmap.compress(format, 100, baos)
            return baos.toByteArray()
        }

        /**
         * Bytes to bitmap.
         *
         * @param bytes The bytes.
         * @return bitmap
         */
        fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
            return if (bytes == null || bytes.size == 0)
                null
            else
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * Drawable to bitmap.
         *
         * @param drawable The drawable.
         * @return bitmap
         */
        fun drawable2Bitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                if (drawable.bitmap != null) {
                    return drawable.bitmap
                }
            }
            val bitmap: Bitmap
            if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                bitmap = Bitmap.createBitmap(
                        1, 1,
                        if (drawable.opacity != PixelFormat.OPAQUE)
                            Bitmap.Config.ARGB_8888
                        else
                            Bitmap.Config.RGB_565
                )
            } else {
                bitmap = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        if (drawable.opacity != PixelFormat.OPAQUE)
                            Bitmap.Config.ARGB_8888
                        else
                            Bitmap.Config.RGB_565
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }


        /**
         * Drawable to bytes.
         *
         * @param drawable The drawable.
         * @param format   The format of bitmap.
         * @return bytes
         */
        fun drawable2Bytes(drawable: Drawable?, format: CompressFormat): ByteArray? {
            return if (drawable == null) null else bitmap2Bytes(drawable2Bitmap(drawable), format)
        }

        /**
         * Bytes to drawable.
         *
         * @param bytes The bytes.
         * @return drawable
         */


        /**
         * View to bitmap.
         *
         * @param view The view.
         * @return bitmap
         */
        fun view2Bitmap(view: View?): Bitmap? {
            if (view == null) return null
            val ret = Bitmap.createBitmap(
                    view.width,
                    view.height,
                    Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(ret)
            val bgDrawable = view.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            return ret
        }

        /**
         * Return bitmap.
         *
         * @param file The file.
         * @return bitmap
         */
        fun getBitmap(file: File?): Bitmap? {
            return if (file == null) null else BitmapFactory.decodeFile(file.absolutePath)
        }

        /**
         * Return bitmap.
         *
         * @param file      The file.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */
        fun getBitmap(file: File?, maxWidth: Int, maxHeight: Int): Bitmap? {
            if (file == null) return null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(file.absolutePath, options)
        }

        /**
         * Return bitmap.
         *
         * @param filePath The path of file.
         * @return bitmap
         */
        fun getBitmap(filePath: String): Bitmap? {
            return if (isSpace(filePath)) null else BitmapFactory.decodeFile(filePath)
        }

        /**
         * Return bitmap.
         *
         * @param filePath  The path of file.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */
        fun getBitmap(filePath: String, maxWidth: Int, maxHeight: Int): Bitmap? {
            if (isSpace(filePath)) return null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }

        /**
         * Return bitmap.
         *
         * @param is The input stream.
         * @return bitmap
         */
        fun getBitmap(`is`: InputStream?): Bitmap? {
            return if (`is` == null) null else BitmapFactory.decodeStream(`is`)
        }

        /**
         * Return bitmap.
         *
         * @param is        The input stream.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */


        /**
         * Return bitmap.
         *
         * @param data   The data.
         * @param offset The offset.
         * @return bitmap
         */
        fun getBitmap(data: ByteArray, offset: Int): Bitmap? {
            return if (data.size == 0) null else BitmapFactory.decodeByteArray(data, offset, data.size)
        }

        /**
         * Return bitmap.
         *
         * @param data      The data.
         * @param offset    The offset.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */
        fun getBitmap(
                data: ByteArray,
                offset: Int,
                maxWidth: Int,
                maxHeight: Int
        ): Bitmap? {
            if (data.size == 0) return null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, offset, data.size, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeByteArray(data, offset, data.size, options)
        }

        /**
         * Return bitmap.
         *
         * @param resId The resource id.
         * @return bitmap
         */


        /**
         * Return bitmap.
         *
         * @param resId     The resource id.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */

        /**
         * Return bitmap.
         *
         * @param fd The file descriptor.
         * @return bitmap
         */
        fun getBitmap(fd: FileDescriptor?): Bitmap? {
            return if (fd == null) null else BitmapFactory.decodeFileDescriptor(fd)
        }

        /**
         * Return bitmap.
         *
         * @param fd        The file descriptor
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @return bitmap
         */
        fun getBitmap(
                fd: FileDescriptor?,
                maxWidth: Int,
                maxHeight: Int
        ): Bitmap? {
            if (fd == null) return null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(fd, null, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFileDescriptor(fd, null, options)
        }

        /**
         * Return the bitmap with the specified color.
         *
         * @param src     The source of bitmap.
         * @param color   The color.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the bitmap with the specified color
         */
        @JvmOverloads
        fun drawColor(
                src: Bitmap,
                @ColorInt color: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = if (recycle) src else src.copy(src.config, true)
            val canvas = Canvas(ret)
            canvas.drawColor(color, PorterDuff.Mode.DARKEN)
            return ret
        }

        /**
         * Return the scaled bitmap.
         *
         * @param src       The source of bitmap.
         * @param newWidth  The new width.
         * @param newHeight The new height.
         * @param recycle   True to recycle the source of bitmap, false otherwise.
         * @return the scaled bitmap
         */
        @JvmOverloads
        fun scale(
                src: Bitmap,
                newWidth: Int,
                newHeight: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the scaled bitmap
         *
         * @param src         The source of bitmap.
         * @param scaleWidth  The scale of width.
         * @param scaleHeight The scale of height.
         * @param recycle     True to recycle the source of bitmap, false otherwise.
         * @return the scaled bitmap
         */
        @JvmOverloads
        fun scale(
                src: Bitmap,
                scaleWidth: Float,
                scaleHeight: Float,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val matrix = Matrix()
            matrix.setScale(scaleWidth, scaleHeight)
            val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the clipped bitmap.
         *
         * @param src     The source of bitmap.
         * @param x       The x coordinate of the first pixel.
         * @param y       The y coordinate of the first pixel.
         * @param width   The width.
         * @param height  The height.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the clipped bitmap
         */
        @JvmOverloads
        fun clip(
                src: Bitmap,
                x: Int,
                y: Int,
                width: Int,
                height: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = Bitmap.createBitmap(src, x, y, width, height)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the skewed bitmap.
         *
         * @param src     The source of bitmap.
         * @param kx      The skew factor of x.
         * @param ky      The skew factor of y.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the skewed bitmap
         */
        fun skew(
                src: Bitmap,
                kx: Float,
                ky: Float,
                recycle: Boolean
        ): Bitmap? {
            return skew(src, kx, ky, 0f, 0f, recycle)
        }

        /**
         * Return the skewed bitmap.
         *
         * @param src     The source of bitmap.
         * @param kx      The skew factor of x.
         * @param ky      The skew factor of y.
         * @param px      The x coordinate of the pivot point.
         * @param py      The y coordinate of the pivot point.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the skewed bitmap
         */
        @JvmOverloads
        fun skew(
                src: Bitmap,
                kx: Float,
                ky: Float,
                px: Float = 0f,
                py: Float = 0f,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val matrix = Matrix()
            matrix.setSkew(kx, ky, px, py)
            val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the rotated bitmap.
         *
         * @param src     The source of bitmap.
         * @param degrees The number of degrees.
         * @param px      The x coordinate of the pivot point.
         * @param py      The y coordinate of the pivot point.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the rotated bitmap
         */
        @JvmOverloads
        fun rotate(
                src: Bitmap,
                degrees: Int,
                px: Float,
                py: Float,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            if (degrees == 0) return src
            val matrix = Matrix()
            matrix.setRotate(degrees.toFloat(), px, py)
            val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the rotated degree.
         *
         * @param filePath The path of file.
         * @return the rotated degree
         */
        fun getRotateDegree(filePath: String): Int {
            try {
                val exifInterface = ExifInterface(filePath)
                val orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> return 270
                    else -> return 0
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return -1
            }

        }

        /**
         * Return the round bitmap.
         *
         * @param src     The source of bitmap.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the round bitmap
         */
        fun toRound(src: Bitmap, recycle: Boolean): Bitmap? {
            return toRound(src, 0, 0, recycle)
        }

        /**
         * Return the round bitmap.
         *
         * @param src         The source of bitmap.
         * @param recycle     True to recycle the source of bitmap, false otherwise.
         * @param borderSize  The size of border.
         * @param borderColor The color of border.
         * @return the round bitmap
         */
        @JvmOverloads
        fun toRound(
                src: Bitmap,
                @IntRange(from = 0) borderSize: Int = 0,
                @ColorInt borderColor: Int = 0,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val width = src.width
            val height = src.height
            val size = Math.min(width, height)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val ret = Bitmap.createBitmap(width, height, src.config)
            val center = size / 2f
            val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            rectF.inset((width - size) / 2f, (height - size) / 2f)
            val matrix = Matrix()
            matrix.setTranslate(rectF.left, rectF.top)
            matrix.preScale(size.toFloat() / width, size.toFloat() / height)
            val shader = BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader
            val canvas = Canvas(ret)
            canvas.drawRoundRect(rectF, center, center, paint)
            if (borderSize > 0) {
                paint.shader = null
                paint.color = borderColor
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = borderSize.toFloat()
                val radius = center - borderSize / 2f
                canvas.drawCircle(width / 2f, height / 2f, radius, paint)
            }
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the round corner bitmap.
         *
         * @param src     The source of bitmap.
         * @param radius  The radius of corner.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the round corner bitmap
         */
        fun toRoundCorner(
                src: Bitmap,
                radius: Float,
                recycle: Boolean
        ): Bitmap? {
            return toRoundCorner(src, radius, 0, 0, recycle)
        }

        /**
         * Return the round corner bitmap.
         *
         * @param src         The source of bitmap.
         * @param radius      The radius of corner.
         * @param borderSize  The size of border.
         * @param borderColor The color of border.
         * @param recycle     True to recycle the source of bitmap, false otherwise.
         * @return the round corner bitmap
         */
        @JvmOverloads
        fun toRoundCorner(
                src: Bitmap,
                radius: Float,
                @IntRange(from = 0) borderSize: Int = 0,
                @ColorInt borderColor: Int = 0,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val width = src.width
            val height = src.height
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val ret = Bitmap.createBitmap(width, height, src.config)
            val shader = BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            val canvas = Canvas(ret)
            val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val halfBorderSize = borderSize / 2f
            rectF.inset(halfBorderSize, halfBorderSize)
            canvas.drawRoundRect(rectF, radius, radius, paint)
            if (borderSize > 0) {
                paint.shader = null
                paint.color = borderColor
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = borderSize.toFloat()
                paint.strokeCap = Paint.Cap.ROUND
                canvas.drawRoundRect(rectF, radius, radius, paint)
            }
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the round corner bitmap with border.
         *
         * @param src          The source of bitmap.
         * @param borderSize   The size of border.
         * @param color        The color of border.
         * @param cornerRadius The radius of corner.
         * @return the round corner bitmap with border
         */
        fun addCornerBorder(
                src: Bitmap,
                @IntRange(from = 1) borderSize: Int,
                @ColorInt color: Int,
                @FloatRange(from = 0.0) cornerRadius: Float
        ): Bitmap? {
            return addBorder(src, borderSize, color, false, cornerRadius, false)
        }

        /**
         * Return the round corner bitmap with border.
         *
         * @param src          The source of bitmap.
         * @param borderSize   The size of border.
         * @param color        The color of border.
         * @param cornerRadius The radius of corner.
         * @param recycle      True to recycle the source of bitmap, false otherwise.
         * @return the round corner bitmap with border
         */
        fun addCornerBorder(
                src: Bitmap,
                @IntRange(from = 1) borderSize: Int,
                @ColorInt color: Int,
                @FloatRange(from = 0.0) cornerRadius: Float,
                recycle: Boolean
        ): Bitmap? {
            return addBorder(src, borderSize, color, false, cornerRadius, recycle)
        }

        /**
         * Return the round bitmap with border.
         *
         * @param src        The source of bitmap.
         * @param borderSize The size of border.
         * @param color      The color of border.
         * @return the round bitmap with border
         */
        fun addCircleBorder(
                src: Bitmap,
                @IntRange(from = 1) borderSize: Int,
                @ColorInt color: Int
        ): Bitmap? {
            return addBorder(src, borderSize, color, true, 0f, false)
        }

        /**
         * Return the round bitmap with border.
         *
         * @param src        The source of bitmap.
         * @param borderSize The size of border.
         * @param color      The color of border.
         * @param recycle    True to recycle the source of bitmap, false otherwise.
         * @return the round bitmap with border
         */
        fun addCircleBorder(
                src: Bitmap,
                @IntRange(from = 1) borderSize: Int,
                @ColorInt color: Int,
                recycle: Boolean
        ): Bitmap? {
            return addBorder(src, borderSize, color, true, 0f, recycle)
        }

        /**
         * Return the bitmap with border.
         *
         * @param src          The source of bitmap.
         * @param borderSize   The size of border.
         * @param color        The color of border.
         * @param isCircle     True to draw circle, false to draw corner.
         * @param cornerRadius The radius of corner.
         * @param recycle      True to recycle the source of bitmap, false otherwise.
         * @return the bitmap with border
         */
        private fun addBorder(
                src: Bitmap,
                @IntRange(from = 1) borderSize: Int,
                @ColorInt color: Int,
                isCircle: Boolean,
                cornerRadius: Float,
                recycle: Boolean
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = if (recycle) src else src.copy(src.config, true)
            val width = ret.width
            val height = ret.height
            val canvas = Canvas(ret)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = color
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = borderSize.toFloat()
            if (isCircle) {
                val radius = Math.min(width, height) / 2f - borderSize / 2f
                canvas.drawCircle(width / 2f, height / 2f, radius, paint)
            } else {
                val halfBorderSize = borderSize shr 1
                val rectF = RectF(
                        halfBorderSize.toFloat(), halfBorderSize.toFloat(),
                        (width - halfBorderSize).toFloat(), (height - halfBorderSize).toFloat()
                )
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
            }
            return ret
        }

        /**
         * Return the bitmap with reflection.
         *
         * @param src              The source of bitmap.
         * @param reflectionHeight The height of reflection.
         * @param recycle          True to recycle the source of bitmap, false otherwise.
         * @return the bitmap with reflection
         */
        @JvmOverloads
        fun addReflection(
                src: Bitmap,
                reflectionHeight: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val REFLECTION_GAP = 0
            val srcWidth = src.width
            val srcHeight = src.height
            val matrix = Matrix()
            matrix.preScale(1f, -1f)
            val reflectionBitmap = Bitmap.createBitmap(
                    src, 0, srcHeight - reflectionHeight,
                    srcWidth, reflectionHeight, matrix, false
            )
            val ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.config)
            val canvas = Canvas(ret)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.drawBitmap(reflectionBitmap, 0f, (srcHeight + REFLECTION_GAP).toFloat(), null)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val shader = LinearGradient(
                    0f, srcHeight.toFloat(),
                    0f, (ret.height + REFLECTION_GAP).toFloat(),
                    0x70FFFFFF,
                    0x00FFFFFF,
                    Shader.TileMode.MIRROR
            )
            paint.shader = shader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawRect(0f, (srcHeight + REFLECTION_GAP).toFloat(), srcWidth.toFloat(), ret.height.toFloat(), paint)
            if (!reflectionBitmap.isRecycled) reflectionBitmap.recycle()
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the bitmap with text watermarking.
         *
         * @param src      The source of bitmap.
         * @param content  The content of text.
         * @param textSize The size of text.
         * @param color    The color of text.
         * @param x        The x coordinate of the first pixel.
         * @param y        The y coordinate of the first pixel.
         * @return the bitmap with text watermarking
         */
        fun addTextWatermark(
                src: Bitmap,
                content: String,
                textSize: Int,
                @ColorInt color: Int,
                x: Float,
                y: Float
        ): Bitmap? {
            return addTextWatermark(src, content, textSize.toFloat(), color, x, y, false)
        }

        /**
         * Return the bitmap with text watermarking.
         *
         * @param src      The source of bitmap.
         * @param content  The content of text.
         * @param textSize The size of text.
         * @param color    The color of text.
         * @param x        The x coordinate of the first pixel.
         * @param y        The y coordinate of the first pixel.
         * @param recycle  True to recycle the source of bitmap, false otherwise.
         * @return the bitmap with text watermarking
         */
        fun addTextWatermark(
                src: Bitmap,
                content: String?,
                textSize: Float,
                @ColorInt color: Int,
                x: Float,
                y: Float,
                recycle: Boolean
        ): Bitmap? {
            if (isEmptyBitmap(src) || content == null) return null
            val ret = src.copy(src.config, true)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val canvas = Canvas(ret)
            paint.color = color
            paint.textSize = textSize
            val bounds = Rect()
            paint.getTextBounds(content, 0, content.length, bounds)
            canvas.drawText(content, x, y + textSize, paint)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the bitmap with image watermarking.
         *
         * @param src       The source of bitmap.
         * @param watermark The image watermarking.
         * @param x         The x coordinate of the first pixel.
         * @param y         The y coordinate of the first pixel.
         * @param alpha     The alpha of watermark.
         * @param recycle   True to recycle the source of bitmap, false otherwise.
         * @return the bitmap with image watermarking
         */
        @JvmOverloads
        fun addImageWatermark(
                src: Bitmap,
                watermark: Bitmap,
                x: Int,
                y: Int,
                alpha: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = src.copy(src.config, true)
            if (!isEmptyBitmap(watermark)) {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                val canvas = Canvas(ret)
                paint.alpha = alpha
                canvas.drawBitmap(watermark, x.toFloat(), y.toFloat(), paint)
            }
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the alpha bitmap.
         *
         * @param src     The source of bitmap.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the alpha bitmap
         */
        @JvmOverloads
        fun toAlpha(src: Bitmap, recycle: Boolean? = false): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = src.extractAlpha()
            if (recycle!! && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the gray bitmap.
         *
         * @param src     The source of bitmap.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the gray bitmap
         */
        @JvmOverloads
        fun toGray(src: Bitmap, recycle: Boolean = false): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val ret = Bitmap.createBitmap(src.width, src.height, src.config)
            val canvas = Canvas(ret)
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
            paint.colorFilter = colorMatrixColorFilter
            canvas.drawBitmap(src, 0f, 0f, paint)
            if (recycle && !src.isRecycled) src.recycle()
            return ret
        }

        /**
         * Return the blur bitmap using stack.
         *
         * @param src     The source of bitmap.
         * @param radius  The radius(0...25).
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the blur bitmap
         */
        @JvmOverloads
        fun stackBlur(src: Bitmap, radius: Int, recycle: Boolean = false): Bitmap {
            var radius = radius
            val ret = if (recycle) src else src.copy(src.config, true)
            if (radius < 1) {
                radius = 1
            }
            val w = ret.width
            val h = ret.height

            val pix = IntArray(w * h)
            ret.getPixels(pix, 0, w, 0, 0, w, h)

            val wm = w - 1
            val hm = h - 1
            val wh = w * h
            val div = radius + radius + 1

            val r = IntArray(wh)
            val g = IntArray(wh)
            val b = IntArray(wh)
            var rsum: Int
            var gsum: Int
            var bsum: Int
            var x: Int
            var y: Int
            var i: Int
            var p: Int
            var yp: Int
            var yi: Int
            var yw: Int
            val vmin = IntArray(Math.max(w, h))

            var divsum = div + 1 shr 1
            divsum *= divsum
            val dv = IntArray(256 * divsum)
            i = 0
            while (i < 256 * divsum) {
                dv[i] = i / divsum
                i++
            }

            yi = 0
            yw = yi

            val stack = Array(div) { IntArray(3) }
            var stackpointer: Int
            var stackstart: Int
            var sir: IntArray
            var rbs: Int
            val r1 = radius + 1
            var routsum: Int
            var goutsum: Int
            var boutsum: Int
            var rinsum: Int
            var ginsum: Int
            var binsum: Int

            y = 0
            while (y < h) {
                bsum = 0
                gsum = bsum
                rsum = gsum
                boutsum = rsum
                goutsum = boutsum
                routsum = goutsum
                binsum = routsum
                ginsum = binsum
                rinsum = ginsum
                i = -radius
                while (i <= radius) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))]
                    sir = stack[i + radius]
                    sir[0] = p and 0xff0000 shr 16
                    sir[1] = p and 0x00ff00 shr 8
                    sir[2] = p and 0x0000ff
                    rbs = r1 - Math.abs(i)
                    rsum += sir[0] * rbs
                    gsum += sir[1] * rbs
                    bsum += sir[2] * rbs
                    if (i > 0) {
                        rinsum += sir[0]
                        ginsum += sir[1]
                        binsum += sir[2]
                    } else {
                        routsum += sir[0]
                        goutsum += sir[1]
                        boutsum += sir[2]
                    }
                    i++
                }
                stackpointer = radius

                x = 0
                while (x < w) {

                    r[yi] = dv[rsum]
                    g[yi] = dv[gsum]
                    b[yi] = dv[bsum]

                    rsum -= routsum
                    gsum -= goutsum
                    bsum -= boutsum

                    stackstart = stackpointer - radius + div
                    sir = stack[stackstart % div]

                    routsum -= sir[0]
                    goutsum -= sir[1]
                    boutsum -= sir[2]

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm)
                    }
                    p = pix[yw + vmin[x]]

                    sir[0] = p and 0xff0000 shr 16
                    sir[1] = p and 0x00ff00 shr 8
                    sir[2] = p and 0x0000ff

                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]

                    rsum += rinsum
                    gsum += ginsum
                    bsum += binsum

                    stackpointer = (stackpointer + 1) % div
                    sir = stack[stackpointer % div]

                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]

                    rinsum -= sir[0]
                    ginsum -= sir[1]
                    binsum -= sir[2]

                    yi++
                    x++
                }
                yw += w
                y++
            }
            x = 0
            while (x < w) {
                bsum = 0
                gsum = bsum
                rsum = gsum
                boutsum = rsum
                goutsum = boutsum
                routsum = goutsum
                binsum = routsum
                ginsum = binsum
                rinsum = ginsum
                yp = -radius * w
                i = -radius
                while (i <= radius) {
                    yi = Math.max(0, yp) + x

                    sir = stack[i + radius]

                    sir[0] = r[yi]
                    sir[1] = g[yi]
                    sir[2] = b[yi]

                    rbs = r1 - Math.abs(i)

                    rsum += r[yi] * rbs
                    gsum += g[yi] * rbs
                    bsum += b[yi] * rbs

                    if (i > 0) {
                        rinsum += sir[0]
                        ginsum += sir[1]
                        binsum += sir[2]
                    } else {
                        routsum += sir[0]
                        goutsum += sir[1]
                        boutsum += sir[2]
                    }

                    if (i < hm) {
                        yp += w
                    }
                    i++
                }
                yi = x
                stackpointer = radius
                y = 0
                while (y < h) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]

                    rsum -= routsum
                    gsum -= goutsum
                    bsum -= boutsum

                    stackstart = stackpointer - radius + div
                    sir = stack[stackstart % div]

                    routsum -= sir[0]
                    goutsum -= sir[1]
                    boutsum -= sir[2]

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w
                    }
                    p = x + vmin[y]

                    sir[0] = r[p]
                    sir[1] = g[p]
                    sir[2] = b[p]

                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]

                    rsum += rinsum
                    gsum += ginsum
                    bsum += binsum

                    stackpointer = (stackpointer + 1) % div
                    sir = stack[stackpointer]

                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]

                    rinsum -= sir[0]
                    ginsum -= sir[1]
                    binsum -= sir[2]

                    yi += w
                    y++
                }
                x++
            }
            ret.setPixels(pix, 0, w, 0, 0, w, h)
            return ret
        }

        /**
         * Save the bitmap.
         *
         * @param src      The source of bitmap.
         * @param filePath The path of file.
         * @param format   The format of the image.
         * @return `true`: success<br></br>`false`: fail
         */
        fun save(
                src: Bitmap,
                filePath: String,
                format: CompressFormat
        ): Boolean {
            return save(src, getFileByPath(filePath), format, false)
        }

        /**
         * Save the bitmap.
         *
         * @param src      The source of bitmap.
         * @param filePath The path of file.
         * @param format   The format of the image.
         * @param recycle  True to recycle the source of bitmap, false otherwise.
         * @return `true`: success<br></br>`false`: fail
         */
        fun save(
                src: Bitmap,
                filePath: String,
                format: CompressFormat,
                recycle: Boolean
        ): Boolean {
            return save(src, getFileByPath(filePath), format, recycle)
        }

        /**
         * Save the bitmap.
         *
         * @param src     The source of bitmap.
         * @param file    The file.
         * @param format  The format of the image.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return `true`: success<br></br>`false`: fail
         */
        @JvmOverloads
        fun save(
                src: Bitmap,
                file: File?,
                format: CompressFormat,
                recycle: Boolean = false
        ): Boolean {
            if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false
            var os: OutputStream? = null
            var ret = false
            try {
                os = BufferedOutputStream(FileOutputStream(file!!))
                ret = src.compress(format, 100, os)
                if (recycle && !src.isRecycled) src.recycle()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    os?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return ret
        }

        /**
         * Return whether it is a image according to the file name.
         *
         * @param file The file.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isImage(file: File?): Boolean {
            return file != null && isImage(file.path)
        }

        /**
         * Return whether it is a image according to the file name.
         *
         * @param filePath The path of file.
         * @return `true`: yes<br></br>`false`: no
         */
        fun isImage(filePath: String): Boolean {
            val path = filePath.toUpperCase()
            return (path.endsWith(".PNG") || path.endsWith(".JPG")
                    || path.endsWith(".JPEG") || path.endsWith(".BMP")
                    || path.endsWith(".GIF") || path.endsWith(".WEBP"))
        }

        /**
         * Return the type of image.
         *
         * @param filePath The path of file.
         * @return the type of image
         */
        fun getImageType(filePath: String): String {
            return getImageType(getFileByPath(filePath))
        }

        /**
         * Return the type of image.
         *
         * @param file The file.
         * @return the type of image
         */
        fun getImageType(file: File?): String {
            if (file == null) return ""
            var `is`: InputStream? = null
            try {
                `is` = FileInputStream(file)
                val type = getImageType(`is`)
                if (type != null) {
                    return type
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return getFileExtension(file.absolutePath)!!.toUpperCase()
        }

        private fun getFileExtension(filePath: String): String? {
            if (isSpace(filePath)) return filePath
            val lastPoi = filePath.lastIndexOf('.')
            val lastSep = filePath.lastIndexOf(File.separator)
            return if (lastPoi == -1 || lastSep >= lastPoi) "" else filePath.substring(lastPoi + 1)
        }

        private fun getImageType(`is`: InputStream?): String? {
            if (`is` == null) return null
            try {
                val bytes = ByteArray(8)
                return if (`is`.read(bytes, 0, 8) != -1) getImageType(bytes) else null
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }

        private fun getImageType(bytes: ByteArray): String? {
            if (isJPEG(bytes)) return "JPEG"
            if (isGIF(bytes)) return "GIF"
            if (isPNG(bytes)) return "PNG"
            return if (isBMP(bytes)) "BMP" else null
        }

        private fun isJPEG(b: ByteArray): Boolean {
            return (b.size >= 2
                    && b[0] == 0xFF.toByte() && b[1] == 0xD8.toByte())
        }

        private fun isGIF(b: ByteArray): Boolean {
            return (b.size >= 6
                    && b[0] == 'G'.toByte() && b[1] == 'I'.toByte()
                    && b[2] == 'F'.toByte() && b[3] == '8'.toByte()
                    && (b[4] == '7'.toByte() || b[4] == '9'.toByte()) && b[5] == 'a'.toByte())
        }

        private fun isPNG(b: ByteArray): Boolean {
            return b.size >= 8 && (b[0] == 137.toByte() && b[1] == 80.toByte()
                    && b[2] == 78.toByte() && b[3] == 71.toByte()
                    && b[4] == 13.toByte() && b[5] == 10.toByte()
                    && b[6] == 26.toByte() && b[7] == 10.toByte())
        }

        private fun isBMP(b: ByteArray): Boolean {
            return (b.size >= 2
                    && b[0].toInt() == 0x42 && b[1].toInt() == 0x4d)
        }

        private fun isEmptyBitmap(src: Bitmap?): Boolean {
            return src == null || src.width == 0 || src.height == 0
        }

        ///////////////////////////////////////////////////////////////////////////
        // about compress
        ///////////////////////////////////////////////////////////////////////////

        /**
         * Return the compressed bitmap using scale.
         *
         * @param src       The source of bitmap.
         * @param newWidth  The new width.
         * @param newHeight The new height.
         * @return the compressed bitmap
         */
        fun compressByScale(
                src: Bitmap,
                newWidth: Int,
                newHeight: Int
        ): Bitmap? {
            return scale(src, newWidth, newHeight, false)
        }

        /**
         * Return the compressed bitmap using scale.
         *
         * @param src       The source of bitmap.
         * @param newWidth  The new width.
         * @param newHeight The new height.
         * @param recycle   True to recycle the source of bitmap, false otherwise.
         * @return the compressed bitmap
         */
        fun compressByScale(
                src: Bitmap,
                newWidth: Int,
                newHeight: Int,
                recycle: Boolean
        ): Bitmap? {
            return scale(src, newWidth, newHeight, recycle)
        }

        /**
         * Return the compressed bitmap using scale.
         *
         * @param src         The source of bitmap.
         * @param scaleWidth  The scale of width.
         * @param scaleHeight The scale of height.
         * @return the compressed bitmap
         */
        fun compressByScale(
                src: Bitmap,
                scaleWidth: Float,
                scaleHeight: Float
        ): Bitmap? {
            return scale(src, scaleWidth, scaleHeight, false)
        }

        /**
         * Return the compressed bitmap using scale.
         *
         * @param src         The source of bitmap.
         * @param scaleWidth  The scale of width.
         * @param scaleHeight The scale of height.
         * @param recycle     True to recycle the source of bitmap, false otherwise.
         * @return he compressed bitmap
         */
        fun compressByScale(
                src: Bitmap,
                scaleWidth: Float,
                scaleHeight: Float,
                recycle: Boolean
        ): Bitmap? {
            return scale(src, scaleWidth, scaleHeight, recycle)
        }

        /**
         * Return the compressed bitmap using quality.
         *
         * @param src     The source of bitmap.
         * @param quality The quality.
         * @param recycle True to recycle the source of bitmap, false otherwise.
         * @return the compressed bitmap
         */
        @JvmOverloads
        fun compressByQuality(
                src: Bitmap,
                @IntRange(from = 0, to = 100) quality: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val baos = ByteArrayOutputStream()
            src.compress(CompressFormat.JPEG, quality, baos)
            val bytes = baos.toByteArray()
            if (recycle && !src.isRecycled) src.recycle()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * Return the compressed bitmap using quality.
         *
         * @param src         The source of bitmap.
         * @param maxByteSize The maximum size of byte.
         * @param recycle     True to recycle the source of bitmap, false otherwise.
         * @return the compressed bitmap
         */
        @JvmOverloads
        fun compressByQuality(
                src: Bitmap,
                maxByteSize: Long,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src) || maxByteSize <= 0) return null
            val baos = ByteArrayOutputStream()
            src.compress(CompressFormat.JPEG, 100, baos)
            val bytes: ByteArray
            if (baos.size() <= maxByteSize) {
                bytes = baos.toByteArray()
            } else {
                baos.reset()
                src.compress(CompressFormat.JPEG, 0, baos)
                if (baos.size() >= maxByteSize) {
                    bytes = baos.toByteArray()
                } else {
                    // find the best quality using binary search
                    var st = 0
                    var end = 100
                    var mid = 0
                    while (st < end) {
                        mid = (st + end) / 2
                        baos.reset()
                        src.compress(CompressFormat.JPEG, mid, baos)
                        val len = baos.size()
                        if (len.toLong() == maxByteSize) {
                            break
                        } else if (len > maxByteSize) {
                            end = mid - 1
                        } else {
                            st = mid + 1
                        }
                    }
                    if (end == mid - 1) {
                        baos.reset()
                        src.compress(CompressFormat.JPEG, st, baos)
                    }
                    bytes = baos.toByteArray()
                }
            }
            if (recycle && !src.isRecycled) src.recycle()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * Return the compressed bitmap using sample size.
         *
         * @param src        The source of bitmap.
         * @param sampleSize The sample size.
         * @param recycle    True to recycle the source of bitmap, false otherwise.
         * @return the compressed bitmap
         */
        @JvmOverloads
        fun compressBySampleSize(
                src: Bitmap,
                sampleSize: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val options = BitmapFactory.Options()
            options.inSampleSize = sampleSize
            val baos = ByteArrayOutputStream()
            src.compress(CompressFormat.JPEG, 100, baos)
            val bytes = baos.toByteArray()
            if (recycle && !src.isRecycled) src.recycle()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        }

        /**
         * Return the compressed bitmap using sample size.
         *
         * @param src       The source of bitmap.
         * @param maxWidth  The maximum width.
         * @param maxHeight The maximum height.
         * @param recycle   True to recycle the source of bitmap, false otherwise.
         * @return the compressed bitmap
         */
        @JvmOverloads
        fun compressBySampleSize(
                src: Bitmap,
                maxWidth: Int,
                maxHeight: Int,
                recycle: Boolean = false
        ): Bitmap? {
            if (isEmptyBitmap(src)) return null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val baos = ByteArrayOutputStream()
            src.compress(CompressFormat.JPEG, 100, baos)
            val bytes = baos.toByteArray()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            if (recycle && !src.isRecycled) src.recycle()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
        }

        private fun getFileByPath(filePath: String): File? {
            return if (isSpace(filePath)) null else File(filePath)
        }

        private fun createFileByDeleteOldFile(file: File?): Boolean {
            if (file == null) return false
            if (file.exists() && !file.delete()) return false
            if (!createOrExistsDir(file.parentFile)) return false
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

        }

        private fun createOrExistsDir(file: File?): Boolean {
            return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }


        @Throws(IOException::class)
        fun compressImage(
                imageFile: File,
                reqWidth: Int,
                reqHeight: Int,
                compressFormat: CompressFormat,
                quality: Int,
                destinationPath: String
        ): File {
            var fileOutputStream: FileOutputStream? = null
            val file = File(destinationPath).parentFile
            if (!file.exists()) {
                file.mkdirs()
            }
            try {
                fileOutputStream = FileOutputStream(destinationPath)
                // write the compressed bitmap at the destination specified by destinationPath.
                decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(
                        compressFormat,
                        quality,
                        fileOutputStream
                )
            } finally {
                if (fileOutputStream != null) {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }
            }

            return File(destinationPath)
        }

        @Throws(IOException::class)
        fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Int, reqHeight: Int): Bitmap {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false

            var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

            //check the rotation of the image and display it properly
            val exif: ExifInterface
            exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap =
                    Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            return scaledBitmap
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }
    }
}
/**
 * Return the bitmap with the specified color.
 *
 * @param src   The source of bitmap.
 * @param color The color.
 * @return the bitmap with the specified color
 */
/**
 * Return the scaled bitmap.
 *
 * @param src       The source of bitmap.
 * @param newWidth  The new width.
 * @param newHeight The new height.
 * @return the scaled bitmap
 */
/**
 * Return the scaled bitmap
 *
 * @param src         The source of bitmap.
 * @param scaleWidth  The scale of width.
 * @param scaleHeight The scale of height.
 * @return the scaled bitmap
 */
/**
 * Return the clipped bitmap.
 *
 * @param src    The source of bitmap.
 * @param x      The x coordinate of the first pixel.
 * @param y      The y coordinate of the first pixel.
 * @param width  The width.
 * @param height The height.
 * @return the clipped bitmap
 */
/**
 * Return the skewed bitmap.
 *
 * @param src The source of bitmap.
 * @param kx  The skew factor of x.
 * @param ky  The skew factor of y.
 * @return the skewed bitmap
 */
/**
 * Return the skewed bitmap.
 *
 * @param src The source of bitmap.
 * @param kx  The skew factor of x.
 * @param ky  The skew factor of y.
 * @param px  The x coordinate of the pivot point.
 * @param py  The y coordinate of the pivot point.
 * @return the skewed bitmap
 */
/**
 * Return the rotated bitmap.
 *
 * @param src     The source of bitmap.
 * @param degrees The number of degrees.
 * @param px      The x coordinate of the pivot point.
 * @param py      The y coordinate of the pivot point.
 * @return the rotated bitmap
 */
/**
 * Return the round bitmap.
 *
 * @param src The source of bitmap.
 * @return the round bitmap
 */
/**
 * Return the round bitmap.
 *
 * @param src         The source of bitmap.
 * @param borderSize  The size of border.
 * @param borderColor The color of border.
 * @return the round bitmap
 */
/**
 * Return the round corner bitmap.
 *
 * @param src    The source of bitmap.
 * @param radius The radius of corner.
 * @return the round corner bitmap
 */
/**
 * Return the round corner bitmap.
 *
 * @param src         The source of bitmap.
 * @param radius      The radius of corner.
 * @param borderSize  The size of border.
 * @param borderColor The color of border.
 * @return the round corner bitmap
 */
/**
 * Return the bitmap with reflection.
 *
 * @param src              The source of bitmap.
 * @param reflectionHeight The height of reflection.
 * @return the bitmap with reflection
 */
/**
 * Return the bitmap with image watermarking.
 *
 * @param src       The source of bitmap.
 * @param watermark The image watermarking.
 * @param x         The x coordinate of the first pixel.
 * @param y         The y coordinate of the first pixel.
 * @param alpha     The alpha of watermark.
 * @return the bitmap with image watermarking
 */
/**
 * Return the alpha bitmap.
 *
 * @param src The source of bitmap.
 * @return the alpha bitmap
 */
/**
 * Return the gray bitmap.
 *
 * @param src The source of bitmap.
 * @return the gray bitmap
 */
/**
 * Return the blur bitmap fast.
 *
 * zoom out, blur, zoom in
 *
 * @param src    The source of bitmap.
 * @param scale  The scale(0...1).
 * @param radius The radius(0...25).
 * @return the blur bitmap
 */
/**
 * Return the blur bitmap fast.
 *
 * zoom out, blur, zoom in
 *
 * @param src     The source of bitmap.
 * @param scale   The scale(0...1).
 * @param radius  The radius(0...25).
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the blur bitmap
 */
/**
 * Return the blur bitmap using render script.
 *
 * @param src    The source of bitmap.
 * @param radius The radius(0...25).
 * @return the blur bitmap
 */
/**
 * Return the blur bitmap using render script.
 *
 * @param src     The source of bitmap.
 * @param radius  The radius(0...25).
 * @param recycle True to recycle the source of bitmap, false otherwise.
 * @return the blur bitmap
 */
/**
 * Return the blur bitmap using stack.
 *
 * @param src    The source of bitmap.
 * @param radius The radius(0...25).
 * @return the blur bitmap
 */
/**
 * Save the bitmap.
 *
 * @param src    The source of bitmap.
 * @param file   The file.
 * @param format The format of the image.
 * @return `true`: success<br></br>`false`: fail
 */
/**
 * Return the compressed bitmap using quality.
 *
 * @param src     The source of bitmap.
 * @param quality The quality.
 * @return the compressed bitmap
 */
/**
 * Return the compressed bitmap using quality.
 *
 * @param src         The source of bitmap.
 * @param maxByteSize The maximum size of byte.
 * @return the compressed bitmap
 */
/**
 * Return the compressed bitmap using sample size.
 *
 * @param src        The source of bitmap.
 * @param sampleSize The sample size.
 * @return the compressed bitmap
 */
/**
 * Return the compressed bitmap using sample size.
 *
 * @param src       The source of bitmap.
 * @param maxWidth  The maximum width.
 * @param maxHeight The maximum height.
 * @return the compressed bitmap
 */
