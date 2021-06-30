package com.example.testdemo.pagerrecyclerview

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.AbsListView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testdemo.R
//import com.example.testdemo.pagerrecyclerview.PagerGridLayoutManager.PageListener
import java.util.*


class PagerRecyclerViewActivity : AppCompatActivity() {

    private val mRows = 2
    private val mColumns = 3
    private var mRadioGroup: RadioGroup? = null
    private val mPageTotal // 总页数
            : TextView? = null
    private val mPageCurrent // 当前页数
            : TextView? = null

    private val mTotal = 0
    private val mCurrent = 0
    private lateinit var adapter:LabelAdapter
    var labelBeans: MutableList<HomeLabelBean> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager_recycler_view)
        init()
        initData()
    }

    private fun init() {
        adapter = LabelAdapter(this,R.layout.item_home_label,labelBeans)
//        val mLayoutManager = PagerGridLayoutManager(2,2,PagerGridLayoutManager.HORIZONTAL)
        val mLayoutManager = PagerManager(2,2,PagerManager.HORIZONTAL)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        mLayoutManager.setChangeSelectInScrolling(false)
//        mLayoutManager.setPageListener(object : PagerGridLayoutManager.PageListener {
        mLayoutManager.setPageListener(object : PagerManager.PageListener {
            override fun onPageSelect(pageIndex: Int) {
                Log.i("tag","---``--- $pageIndex")
            }

            override fun onPageSizeChanged(pageSize: Int) {
                Log.i("tag","---==--- $pageSize")
            }

        })
        recyclerView.layoutManager = mLayoutManager
//        val helper = PagerGridSnapHelper()
//        helper.attachToRecyclerView(recyclerView)

//        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
//            val count = adapter.itemCount
//        })
        recyclerView.adapter = adapter

//        findViewById<AppCompatTextView>(R.id.up).setOnClickListener {
//            mLayoutManager.prePage()
//        }
//        findViewById<AppCompatTextView>(R.id.next).setOnClickListener {
//            mLayoutManager.nextPage()
//        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.i("tag","$dx $dy -------")
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.i("tag","$newState -------")
            }

        })
        PagerConfig.setShowLog(true);

    }

    private fun initData() {
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_read, "", R.mipmap.icon_primary_home_read, "read", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_review, "", R.mipmap.icon_primary_home_review, "review", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_interest, "", R.mipmap.icon_primary_home_interest, "interest", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_literature, "", R.mipmap.icon_primary_home_literature, "literature", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_video, "", R.mipmap.icon_primary_home_video, "video", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_teaching, "", R.mipmap.icon_primary_home_teaching, "teaching", 0))
        labelBeans.add(HomeLabelBean(R.mipmap.primary_home_read, "", R.mipmap.icon_primary_home_read, "book", 0))
        adapter.notifyDataSetChanged()
    }


}