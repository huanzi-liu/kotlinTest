package com.example.testdemo.span

import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.testdemo.R
import com.example.testdemo.databinding.ActivitySpanBinding

@RequiresApi(Build.VERSION_CODES.P)
class SpanActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpanBinding;
    val texts = SpannableString(" 深圳市蓝色冰川教育科技有限公司是一家致力于“教育+科技” 的企业，创始人拥有30多年的教育经验，研发成果包含多个教育理论模型和一系列教育创新理论，并获得国家专利。" +
            "公司拥有着卓越的教学实践成果，其中改革开放40周年的最高教育成果的记录：薛辉，全世界唯一一个没读任何大学，直接考进哥伦比亚读博士，普林斯顿读博士后；同时也是全国40年的最高纪录。")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_span)

        binding = DataBindingUtil.setContentView<ActivitySpanBinding>(this, R.layout.activity_span)
        init()
    }

    private fun init() {
        TestSpan1()
        TestSpan2()
        TestSpan3()
    }

    private fun TestSpan1() {
        val text = "tt ee xx tt ss pp aa nn bb cc dd ff gg hh ii jj kk ll mm nn o\to pp qq rr uu vv ww yy zz"
        val s = SpannableString(text)
        s.setSpan(BackgroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(ForegroundColorSpan(Color.BLUE), 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(DrawableMarginSpan(getDrawable(R.mipmap.ic_launcher)!!), 0, s.length, 0)
        s.setSpan(MaskFilterSpan(BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(MaskFilterSpan(EmbossMaskFilter(floatArrayOf(10f, 10f, 10f), 4f, 6f, 35f)), 9, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StrikethroughSpan(), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(UnderlineSpan(), 15, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(QuoteSpan(Color.RED), 19, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(BulletSpan(15, Color.RED), 21, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(AbsoluteSizeSpan(15, true), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(ImageSpan(this, R.mipmap.ic_launcher_round), 27, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        s.setSpan(IconMarginSpan(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)),0,s.length,0)
        s.setSpan(RelativeSizeSpan(2f), 30, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(ScaleXSpan(3f), 33, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StyleSpan(Typeface.BOLD), 36, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(StyleSpan(Typeface.ITALIC), 36, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(SubscriptSpan(), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(SuperscriptSpan(), 42, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.apply {
            setSpan(ForegroundColorSpan(Color.YELLOW), 46, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(SuperscriptSpan(), 46, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        s.setSpan(TextAppearanceSpan(this, R.style.TextStyle), 48, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(TypefaceSpan(Typeface.createFromAsset(assets, "fonts/Asap-BoldItalic.ttf")), 51, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(TypefaceSpan("monospace"), 54, 56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(URLSpan("http://www.baidu.com"), 57, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(TabStopSpan.Standard(1), 60, 64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(LeadingMarginSpan.Standard(20), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        s.setSpan(LineHeightSpan.Standard(20),0,s.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tv.text = s
    }

    private fun TestSpan2() {
        binding.tv2.setText(texts,TextView.BufferType.SPANNABLE)
        val s = binding.tv2.text as Spannable
//        val s = SpannableString(text)
        s.apply {
            s.setSpan(TextRoundSpan(3, 200), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(RainbowSpan(10), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            s.setSpan(DrawableMarginSpan(getDrawable(R.mipmap.ic_launcher)!!),0,s.length,0)
        }
//        binding.tv2.text = s
//        binding.tv2.invalidate()
//        binding.tv2.requestLayout()

    }

    private fun TestSpan3() {
        binding.tv3.setText(texts,TextView.BufferType.SPANNABLE)
        val s = binding.tv3.text as Spannable
        val drawable = ContextCompat.getDrawable(this@SpanActivity,R.mipmap.ic_launcher)!!
        drawable.setBounds(0,0,80,80)
        s.apply {
            s.setSpan(RoundBackGroundColorRectSpan(Color.WHITE,Color.YELLOW,15f),100,110,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            s.setSpan(RoundBackGroundColorSpan(Color.WHITE,Color.BLACK,10f,"标签"),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(RoundBackGroundColorSpan2(Color.WHITE,Color.RED,Color.MAGENTA,10f,"标签"),0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(RainbowSpan(10), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(ExImageSpan(drawable,DynamicDrawableSpan.ALIGN_CENTER),20,24,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val indices = getIndices(texts.toString(),'，')
        for (i in indices) {
            println(i)
        }

        var start =0
        var end = 0
        for (i in 0..indices.size) {
            val clickSpan = ClickSpan()
            end = if (i<indices.size) indices[i] else texts.length
            s.setSpan(clickSpan,start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end+1
        }
        binding.tv3.highlightColor = Color.CYAN
        binding.tv3.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun getIndices(text: String, char: Char) :MutableList<Int>{
        var pos = text.indexOf(char, 0)
        var indices:MutableList<Int> = mutableListOf()
        while (pos != -1) {
            indices.add(pos)
            pos = text.indexOf(char,pos+1)
        }
        return indices
    }

}