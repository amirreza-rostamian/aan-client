package me.himanshusoni.chatmessageview.ui

import android.graphics.Paint
import android.view.Gravity
import android.widget.TextView

object TextJustifyUtils {
    internal val SYSTEM_NEWLINE = "\n"
    internal val COMPLEXITY = 5.12f  //Reducing this will increase efficiency but will decrease effectiveness
    internal val p = Paint()

    fun justify(textView: TextView) {
        val paint = Paint()

        val blocks: Array<String>
        var spaceOffset = 0f
        var textWrapWidth: Float

        var spacesToSpread: Int
        var wrappedEdgeSpace: Float
        var block: String
        var lineAsWords: Array<String>
        var wrappedLine: String
        var smb = ""
        var wrappedObj: Array<Any>

        // Pull widget properties
        paint.color = textView.currentTextColor
        paint.typeface = textView.typeface
        paint.textSize = textView.textSize

        textWrapWidth = textView.width.toFloat()
        spaceOffset = paint.measureText(" ")
        blocks = textView.text.toString().split("((?<=\n)|(?=\n))".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()

        if (textWrapWidth < 20) {
            return
        }

        var i = 0
        while (i < blocks.size) {
            block = blocks[i]

            if (block.length == 0) {
                i++
                continue
            } else if (block == "\n") {
                smb += block
                continue
            }

            block = block.trim { it <= ' ' }

            if (block.length == 0) {
                i++
                continue
            }

            wrappedObj = TextJustifyUtils.createWrappedLine(block, paint, spaceOffset, textWrapWidth)
            wrappedLine = wrappedObj[0] as String
            wrappedEdgeSpace = wrappedObj[1] as Float
            lineAsWords = wrappedLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            spacesToSpread =
                    (if (wrappedEdgeSpace != java.lang.Float.MIN_VALUE) (wrappedEdgeSpace / spaceOffset).toInt() else 0)

            for (word in lineAsWords) {
                smb += "$word "

                if (--spacesToSpread > 0) {
                    smb += " "
                }
            }

            smb = smb.trim { it <= ' ' }


            if (blocks[i].length > 0) {
                blocks[i] = blocks[i].substring(wrappedLine.length)

                if (blocks[i].length > 0) {
                    smb += "\n"
                }

                i--
            }
            i++
        }

        textView.gravity = Gravity.LEFT
        textView.text = smb
    }

    fun createWrappedLine(block: String, paint: Paint, spaceOffset: Float, maxWidth: Float): Array<Any> {
        var maxWidth = maxWidth
        var cacheWidth = maxWidth
        val origMaxWidth = maxWidth

        var line = ""

        for (word in block.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            cacheWidth = paint.measureText(word)
            maxWidth -= cacheWidth

            if (maxWidth <= 0) {
                return arrayOf(line, maxWidth + cacheWidth + spaceOffset)
            }

            line += "$word "
            maxWidth -= spaceOffset

        }

        return if (paint.measureText(block) <= origMaxWidth) {
            arrayOf(block, java.lang.Float.MIN_VALUE)
        } else arrayOf(line, maxWidth)
    }

    /* @author Mathew Kurian */

    fun run(tv: TextView, origWidth: Float) {
        val s = tv.text.toString()
        p.typeface = tv.typeface
        val splits = s.split(SYSTEM_NEWLINE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val width = origWidth - 5
        for (x in splits.indices)
            if (p.measureText(splits[x]) > width) {
                splits[x] = wrap(splits[x], width, p)
                val microSplits =
                        splits[x].split(SYSTEM_NEWLINE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (y in 0 until microSplits.size - 1)
                    microSplits[y] = justify(removeLast(microSplits[y], " "), width, p)
                val smb_internal = StringBuilder()
                for (z in microSplits.indices)
                    smb_internal.append(microSplits[z] + if (z + 1 < microSplits.size) SYSTEM_NEWLINE else "")
                splits[x] = smb_internal.toString()
            }
        val smb = StringBuilder()
        for (cleaned in splits)
            smb.append(cleaned + SYSTEM_NEWLINE)
        tv.gravity = Gravity.LEFT
        tv.text = smb
    }

    private fun wrap(s: String, width: Float, p: Paint): String {
        val str = s.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() //regex
        val smb = StringBuilder() //save memory
        smb.append(SYSTEM_NEWLINE)
        for (x in str.indices) {
            val length = p.measureText(str[x])
            val pieces = smb.toString().split(SYSTEM_NEWLINE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                if (p.measureText(pieces[pieces.size - 1]) + length > width)
                    smb.append(SYSTEM_NEWLINE)
            } catch (e: Exception) {
            }

            smb.append(str[x] + " ")
        }
        return smb.toString().replaceFirst(SYSTEM_NEWLINE.toRegex(), "")
    }

    private fun removeLast(s: String, g: String): String {
        if (s.contains(g)) {
            val index = s.lastIndexOf(g)
            val indexEnd = index + g.length
            return if (index == 0)
                s.substring(1)
            else if (index == s.length - 1)
                s.substring(0, index)
            else
                s.substring(0, index) + s.substring(indexEnd)
        }
        return s
    }

    private fun justifyOperation(s: String, width: Float, p: Paint): String {
        var s = s
        var holder = (COMPLEXITY * Math.random()).toFloat()
        while (s.contains(java.lang.Float.toString(holder)))
            holder = (COMPLEXITY * Math.random()).toFloat()
        val holder_string = java.lang.Float.toString(holder)
        var lessThan = width
        val timeOut = 100
        var current = 0
        while (p.measureText(s) < lessThan && current < timeOut) {
            s = s.replaceFirst(" ([^$holder_string])".toRegex(), " $holder_string$1")
            lessThan = p.measureText(holder_string) + lessThan - p.measureText(" ")
            current++
        }
        return s.replace(holder_string.toRegex(), " ")
    }

    private fun justify(s: String, width: Float, p: Paint): String {
        var s = s
        while (p.measureText(s) < width) {
            s = justifyOperation(s, width, p)
        }
        return s
    }
}


