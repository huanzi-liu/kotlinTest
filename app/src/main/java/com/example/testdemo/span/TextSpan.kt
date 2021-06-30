package com.example.testdemo.span

import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.TextPaint
import android.text.style.*
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference

/**
 * 文字渐变
 */
class RainbowSpan(private val textLength: Int) : CharacterStyle(), UpdateAppearance {
    override fun updateDrawState(tp: TextPaint) {
        tp.style = Paint.Style.FILL
        val shader = LinearGradient(0f, 0f, tp.textSize.times(textLength), 0f, Color.RED, Color.DKGRAY, Shader.TileMode.CLAMP)
        tp.shader = shader
    }

}

/**
 * 缩进留空
 */
class TextRoundSpan(private val lines: Int, private val margin: Int) : LeadingMarginSpan.LeadingMarginSpan2 {
    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) margin else 0
    }

    override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence?, start: Int, end: Int, first: Boolean, layout: Layout?) {

    }

    override fun getLeadingMarginLineCount(): Int = lines

}

/**
 * img居中
 */
class ExImageSpan(drawable: Drawable, align: Int) : ImageSpan(drawable, align) {

    private val a = align
    private val d = drawable
    var drawableRef: WeakReference<Drawable>? = null

    private fun getCacheDrawable(): Drawable? {
        var ref: WeakReference<Drawable>? = drawableRef
        var dra: Drawable? = null
        if (ref != null) {
            dra = ref.get()
        }
        if (dra == null) {
            dra = d
            drawableRef = WeakReference<Drawable>(dra)
        }
        return dra
    }

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {

        val dra = getCacheDrawable()
        dra?.let {
            val rect = dra.bounds
            if (fm != null) {
                val fmPaint = paint.fontMetricsInt
                val textHeight = fmPaint.descent - fmPaint.ascent
                val imgHeight = rect.bottom - rect.top
                if (imgHeight > textHeight && a == ALIGN_CENTER) {
                    fm.ascent = fmPaint.ascent - (imgHeight - textHeight) / 2
                    fm.top = fmPaint.ascent - (imgHeight - textHeight) / 2
                    fm.descent = fmPaint.descent + (imgHeight - textHeight) / 2
                    fm.bottom = fmPaint.descent + (imgHeight - textHeight) / 2
                } else {
                    fm.ascent = -rect.bottom
                    fm.descent = 0
                    fm.top = fm.ascent
                    fm.bottom = 0
                }
            }
            return rect.right
        }

        return 0

    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
//        super.draw(canvas, text, start, end, x, top, y, bottom, paint)

        when (a) {
            ALIGN_CENTER -> {
                canvas.save()
                val fmPaint = paint.fontMetricsInt
                val fontHeight = fmPaint.descent - fmPaint.ascent
                val centerY = y + fmPaint.descent - fontHeight / 2
                val transY = centerY - (d.bounds.bottom - d.bounds.top) / 2
                canvas.translate(x, transY.toFloat())
                d.draw(canvas)
                canvas.restore()
            }
            else -> {
                canvas.save()
                val transY = top + paint.fontMetricsInt.ascent - paint.fontMetricsInt.top
                canvas.translate(x, transY.toFloat())
                d.draw(canvas)
                canvas.restore()
            }
        }
    }

}

class ClickSpan() : ClickableSpan() {
    override fun onClick(widget: View) {
        var tv = widget as TextView
        tv.text.subSequence(tv.selectionStart, tv.selectionEnd)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
    }
}

/**
 * 跟换字体颜色和背景色
 */
class RoundBackGroundColorRectSpan(val colorText: Int, val bgColor: Int, val radius: Float) : ReplacementSpan() {
    var spanSize = 0
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        spanSize = (paint.measureText(text, start, end) + 2 * radius).toInt()
        return spanSize

    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        paint.apply {
            color = bgColor
            isAntiAlias = true
        }
        val rect = RectF(x, y + paint.ascent(), x + spanSize, y + paint.descent())
        canvas.drawRoundRect(rect,radius,radius,paint)
        paint.color = colorText
        canvas.drawText(text.toString(),start,end,x+radius,y.toFloat(),paint)

    }

}

/**
 * 标签
 */
class RoundBackGroundColorSpan(val colorText: Int, val bgColor: Int, val radius: Float,val textString:String) : ReplacementSpan() {
    var spanSize = 0
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        spanSize = (paint.measureText(textString, 0, textString.length) + 2 * radius).toInt()
        return spanSize

    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val originalColor = paint.color
        paint.apply {
            color = bgColor
            isAntiAlias = true
        }
        val rect = RectF(x, y + paint.ascent(), x + spanSize, y + paint.descent())
        canvas.drawRoundRect(rect,radius,radius,paint)
        paint.color = colorText
        canvas.drawText(textString,x+radius,y.toFloat(),paint)
        paint.color = originalColor
    }

}

/**
 * 渐变标签
 */
class RoundBackGroundColorSpan2(val colorText: Int, val bgColorStart: Int,val bgColorEnd: Int, val radius: Float,val textString:String) : ReplacementSpan() {
    var spanSize = 0
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        spanSize = (paint.measureText(textString, 0, textString.length) + 2 * radius).toInt()
        return spanSize

    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
       val newPaint = paint.apply {
            color = colorText
            isAntiAlias = true
           textSize =paint.textSize
        }
        val rect = RectF(x, y + paint.ascent(), x + spanSize, y + paint.descent())
        val gradient = LinearGradient(0f,0f,rect.right,rect.bottom,bgColorStart,bgColorEnd,Shader.TileMode.CLAMP)
        newPaint.shader = gradient
        canvas.drawRoundRect(rect,radius,radius,newPaint)
        newPaint.shader = null
        canvas.drawText(textString,x+radius,y.toFloat(),newPaint)
    }

}