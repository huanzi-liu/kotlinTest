package com.example.testdemo.pagerrecyclerview

import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class PagerManager(@IntRange(from = 1, to = 100) rows: Int, @IntRange(from = 1, to = 100) columns: Int, @OrientationType orientationType: Int) : RecyclerView.LayoutManager(), RecyclerView.SmoothScroller.ScrollVectorProvider {

    companion object {
        const val VERTICAL = 0
        const val HORIZONTAL = 1

    }

    @IntDef(VERTICAL, HORIZONTAL)
    annotation class OrientationType  // 滚动类型

    @OrientationType
    private val orientation = orientationType

    var row = rows
    var column = columns
    var onePageSize = row * column

    var itemFrames: SparseArray<Rect> = SparseArray()

    var offsetX = 0
    var offsetY = 0

    var itemWidth = 0
    var itemHeight = 0

    var maxScrollX = 0
    var maxScrollY = 0

    var widthUsed = 0
    var heightUsed = 0

    var scrollState = RecyclerView.SCROLL_STATE_IDLE

    var recyclerView: RecyclerView? = null

    var lastPageCount = -1
    var lastPageIndex = -1

    var allowContinuousScroll = true
    var changeSelectInScrolling = true

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view
    }

    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
        val width = View.MeasureSpec.getSize(widthSpec)
        val height = View.MeasureSpec.getSize(heightSpec)

        var widthMode = View.MeasureSpec.getMode(widthSpec)
        var heightMode = View.MeasureSpec.getMode(heightSpec)

        if (widthMode != EXACTLY && width > 0) {
            widthMode = EXACTLY
        }
        if (heightMode != EXACTLY && height > 0) {
            heightMode = EXACTLY
        }

        setMeasuredDimension(View.MeasureSpec.makeMeasureSpec(width, widthMode), View.MeasureSpec.makeMeasureSpec(height, heightMode))
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        if (state.isPreLayout || !state.didStructureChange()) {
            return
        }
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            setPageCount(0)
            setPageIndex(0, false)
            return
        } else {
            setPageCount(getTotalPageCount())
            setPageIndex(getPageIndexByOffset(), false)
        }

        var pageCount = itemCount / onePageSize
        if (itemCount % onePageSize != 0) {
//            pageCount += 1
            pageCount++
        }

        if (canScrollHorizontally()) {
            maxScrollX = (pageCount - 1) * getUsableWidth()
            maxScrollY = 0
            if (offsetX > maxScrollX) {
                offsetX = maxScrollX
            }
        } else {
            maxScrollY = (pageCount - 1) * getUsableHeight()
            maxScrollX = 0
            if (offsetY > maxScrollY) {
                offsetY = maxScrollY
            }
        }

        if (itemWidth <= 0) {
            itemWidth = getUsableWidth() / column
        }
        if (itemHeight <= 0) {
            itemHeight = getUsableHeight() / row
        }

        widthUsed = getUsableWidth() - itemWidth
        heightUsed = getUsableHeight() - itemHeight

        val pageSize = onePageSize * 2
        for (i in 0 until pageSize) {
            getItemFrameByPosition(i)
        }

        if (offsetX == 0 && offsetY == 0) {
            for (i in 0 until onePageSize) {
                if (i >= itemCount) {
                    break
                }
                val view = recycler.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, widthUsed, heightUsed)
            }
        }

        recyclerAddFillItems(recycler, state, true)
    }

    override fun onLayoutCompleted(state: RecyclerView.State) {
        super.onLayoutCompleted(state)
        if (state.isPreLayout) {
            return
        }
        setPageCount(getTotalPageCount())
        setPageIndex(getPageIndexByOffset(), false)
    }

    private fun setPageCount(pageCount: Int) {
        if (pageCount >= 0) {
            if (pageCount != lastPageCount) {
                pageListener?.onPageSizeChanged(pageCount)
            }
            lastPageCount = pageCount
        }
    }

    private fun setPageIndex(pageIndex: Int, isScrolling: Boolean) {
        if (pageIndex == lastPageIndex) {
            return
        }
        if (isAllowContinuousScroll()) {
            lastPageIndex = pageIndex
        } else {
            if (!isScrolling) {
                lastPageIndex = pageIndex
            }
        }
        if (isScrolling && !changeSelectInScrolling) {
            return
        }
        if (pageIndex >= 0) {
            pageListener?.onPageSelect(pageIndex)
        }

    }

    private fun isAllowContinuousScroll(): Boolean {
        return allowContinuousScroll
    }

    private fun getTotalPageCount(): Int {
        if (itemCount <= 0) {
            return 0
        }
        var totalCount = itemCount / onePageSize
        if (itemCount % onePageSize != 0) {
//            totalCount += 1
            totalCount++
        }
        return totalCount
    }

    private fun getPageIndexByOffset(): Int {
        var pageIndex = 0
        if (canScrollVertically()) {
            val pageHeight = getUsableHeight()
            if (offsetY <= 0 || pageHeight <= 0) {
                pageIndex = 0
            } else {
                pageIndex = offsetY / pageHeight
                if (offsetY % pageHeight > pageHeight / 2) {
//                    pageIndex += 1
                    pageIndex++
                }

            }
        } else {
            val pageWidth = getUsableWidth()
            if (offsetX <= 0 || pageWidth <= 0) {
                pageIndex = 0
            } else {
                pageIndex = offsetX / pageWidth
                if (offsetX % pageWidth > pageWidth / 2) {
//                    pageIndex += 1
                    pageIndex++
                }
            }
        }
        return pageIndex
    }

    private fun getItemFrameByPosition(pos: Int): Rect {
        var rect = itemFrames.get(pos)
        if (null == rect) {
            rect = Rect()
            val page = pos / onePageSize
            var offsetX = 0
            var offsetY = 0

            if (canScrollHorizontally()) {
                offsetX += getUsableWidth() * page
            } else {
                offsetY += getUsableHeight() * page
            }

            val pagePos = pos % onePageSize
            val row = pagePos / column
            val col = pagePos - (row * column)

            offsetX += col * itemWidth
            offsetY += row * itemHeight

            // 状态输出，用于调试

            Log.i("tag", "pagePos = $pagePos")
            Log.i("tag", "行 = $row")
            Log.i("tag", "列 = $col")

            Log.i("tag", "offsetX = $offsetX")
            Log.i("tag", "offsetY = $offsetY")

            rect.left = offsetX
            rect.top = offsetY
            rect.right = offsetX + itemWidth
            rect.bottom = offsetY + itemHeight

            itemFrames.put(pos, rect)
        }
        return rect
    }

    @SuppressLint("CheckResult")
    private fun recyclerAddFillItems(recyclerView: RecyclerView.Recycler, state: RecyclerView.State, isStart: Boolean) {
        if (state.isPreLayout) {
            return
        }
        val rect = Rect(offsetX - itemWidth, offsetY - itemHeight, getUsableWidth() + offsetX + itemWidth, getUsableHeight() + offsetY + itemHeight)
        rect.intersect(0, 0, maxScrollX + getUsableWidth(), maxScrollY + getUsableHeight())

        val pageIndex = getPageIndexByOffset()
        var startPos = (pageIndex * onePageSize) - (onePageSize * 2)
        if (startPos < 0) {
            startPos = 0
        }

        var stopPos = startPos + onePageSize * 4
        if (stopPos > itemCount) {
            stopPos = itemCount
        }

        detachAndScrapAttachedViews(recyclerView)

        if (isStart) {
            for (i in startPos until stopPos) {
                addOrRemove(recyclerView, rect, i)
            }
        } else {
            for (i in stopPos - 1 downTo startPos) {
                addOrRemove(recyclerView, rect, i)
            }
        }
    }

    private fun addOrRemove(recycler: RecyclerView.Recycler, rect: Rect, index: Int) {
        val view = recycler.getViewForPosition(index)
        val itemRect = getItemFrameByPosition(index)
        if (!Rect.intersects(itemRect, rect)) {
            removeAndRecycleView(view, recycler)
        } else {
            addView(view)
            measureChildWithMargins(view, widthUsed, heightUsed)
            val lp = view.layoutParams as RecyclerView.LayoutParams
            layoutDecorated(view,
                    rect.left - offsetX + lp.leftMargin + paddingLeft,
                    rect.top - offsetY + lp.topMargin + paddingTop,
                    rect.right - offsetX - lp.rightMargin + paddingLeft,
                    rect.bottom - offsetY - lp.bottomMargin + paddingTop)
        }

    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val x = offsetX + dx
        var result = dx
        if (x > maxScrollX) {
            result = maxScrollX - offsetX
        } else if (x < 0) {
            result = -offsetX
        }
        offsetX += result
        setPageIndex(getPageIndexByOffset(), true)
        offsetChildrenHorizontal(-result)
        if (result > 0) {
            recyclerAddFillItems(recycler, state, true)
        } else {
            recyclerAddFillItems(recycler, state, false)
        }
        return result
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val y = offsetY + dy
        var result = dy
        if (y > maxScrollY) {
            result = maxScrollY - offsetY
        } else if (y < 0) {
            result = -offsetY
        }
        offsetY += result
        setPageIndex(getPageIndexByOffset(), true)
        offsetChildrenHorizontal(-result)
        if (result > 0) {
            recyclerAddFillItems(recycler, state, true)
        } else {
            recyclerAddFillItems(recycler, state, false)
        }
        return result
    }

    override fun onScrollStateChanged(state: Int) {
        scrollState = state
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            setPageIndex(getPageIndexByOffset(), false)
        }
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        val pointF = PointF()
        val pos = getSnapOffset(targetPosition)
        pointF.x = pos[0].toFloat()
        pointF.y = pos[1].toFloat()
        return pointF
    }

    fun getSnapOffset(targetPosition: Int): IntArray {
        val offset = IntArray(2)
        val pos = getPageLeftTopByPosition(targetPosition)
        offset[0] = pos[0] - offsetX
        offset[1] = pos[1] - offsetY
        return offset
    }

    private fun getPageLeftTopByPosition(targetPosition: Int): IntArray {
        val leftTop = IntArray(2)
        val pos = getPageIndexByPos(targetPosition)
        if (canScrollHorizontally()) {
            leftTop[0] = getUsableWidth() * pos
            leftTop[1] = 0
        } else {
            leftTop[0] = 0
            leftTop[1] = getUsableHeight() * pos
        }
        return leftTop
    }

    private fun getPageIndexByPos(targetPosition: Int): Int {
        return targetPosition / onePageSize
    }

    override fun canScrollHorizontally(): Boolean {
        return orientation == HORIZONTAL
    }

    override fun canScrollVertically(): Boolean {
        return orientation == VERTICAL
    }

    private fun getUsableWidth(): Int {
        Log.i("tag", "w----${width - paddingStart - paddingEnd}")
        return width - paddingStart - paddingEnd
    }

    private fun getUsableHeight(): Int {
        Log.i("tag", "h----${height - paddingTop - paddingBottom}")
        return height - paddingTop - paddingBottom
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val pageIndex = getPageIndexByPos(position)
        smoothScrollToPage(pageIndex)
    }

    private fun smoothScrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= lastPageCount) {
            return
        }
        if (null == recyclerView) {
            return
        }
        val currentPageInt = getPageIndexByOffset()
        if (abs(pageIndex - currentPageInt) > 3) {
            if (pageIndex > currentPageInt) {
                scrollToPage(pageIndex - 3)
            } else if (pageIndex < currentPageInt) {
                scrollToPage(pageIndex + 3)
            }
        }
//        val smoothScroller = LinearSmoothScroller(recyclerView!!.context)
        val smoothScroller = PagerGridSmoothScroller(recyclerView!!)
        smoothScroller.targetPosition = onePageSize * pageIndex
        startSmoothScroll(smoothScroller)
    }

    private fun scrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= lastPageCount) {
            return
        }
        if (null == recyclerView) {
            return
        }

        var targetOffsetXBy = 0
        var targetOffsetYBy = 0
        if (canScrollVertically()) {
            targetOffsetXBy = 0
            targetOffsetYBy = pageIndex * getUsableHeight() - offsetY
        } else {
            targetOffsetXBy = pageIndex * getUsableWidth() - offsetX
            targetOffsetYBy = 0
        }
        recyclerView?.scrollBy(targetOffsetXBy, targetOffsetYBy)
        setPageIndex(pageIndex, false)

    }

    override fun scrollToPosition(position: Int) {
        scrollToPage(getPageIndexByPos(position))
    }

    private var pageListener: PageListener? = null
    fun setPageListener(listener: PageListener) {
        pageListener = listener
    }

    interface PageListener {
        fun onPageSizeChanged(pageSize: Int)

        fun onPageSelect(pageIndex: Int)
    }

    fun findSnapView(): View? {
        if (null != focusedChild) {
            return focusedChild as View
        }
        if (childCount > 0) {
            return null
        }
        val pos = getPageIndexByOffset() * onePageSize
        for (i in 0 until childCount) {
            val childPos = getChildAt(i)?.let { getPosition(it) }
            if (childPos == pos) {
                return getChildAt(i)
            }
        }
        return getChildAt(0)
    }

}