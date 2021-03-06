package com.example.testdemo.recyclerviewpage;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author : huanzi
 * @date : 2019/11/25
 * desc : recyclerView整页滑动的manager
 */
public class PageLayoutManager extends RecyclerView.LayoutManager{

    /**
     * 滑动距离
     */
    private int offsetX = 0;
    private int offsetY = 0;

    /**
     * 可滚动的最大值
     */
    private int totalWidth = 0;
    private int totalHeight = 0;
    /**
     * 每个Item的平均宽高
     */
    private int itemWidth = 0;
    private int itemHeight = 0;

    /**
     * 每页的可用宽高
     */
    private int itemWidthUsed = 0;
    private int itemHeightUsed = 0;

    /**
     * 总页数
     */
    private int pageSize = 0;
    /**
     * 每页总个数
     */
    private int onePageSize;

    /**
     * 行数
     */
    private int rows;
    /**
     * 列数
     */
    private int columns;
    /**
     * 横向滑动还是竖向滑动
     */
    private int orientation;

    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    public PageLayoutManager(int rows, int columns, int orientation) {
        this.rows = rows;
        this.columns = columns;
        this.onePageSize = rows * columns;
        this.orientation = orientation;
    }

    private int getUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * 计算总页数
     * @param state state
     */
    private void computePageSize(RecyclerView.State state) {
        pageSize = state.getItemCount() / onePageSize + (state.getItemCount() % onePageSize == 0 ? 0 : 1);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        //获取每个Item的平均宽高
        itemWidth = getUsableWidth() / columns;
        itemHeight = getUsableHeight() / rows;

        //计算宽高已经使用的量，主要用于后期测量
        itemWidthUsed = (columns - 1) * itemWidth;
        itemHeightUsed = (rows - 1) * itemHeight;

        //计算总的页数

//        pageSize = state.getItemCount() / onePageSize + (state.getItemCount() % onePageSize == 0 ? 0 : 1);
        computePageSize(state);
        Log.i("zzz", "itemCount=" + getItemCount() + " state itemCount=" + state.getItemCount() + " pageSize=" + pageSize);
        if (orientation == LinearLayout.HORIZONTAL) {
            //计算可以滚动的最大值
            totalWidth = (pageSize - 1) * getWidth();
        }else{
            totalHeight = (pageSize - 1) * getHeight();
        }

        //分离view
        detachAndScrapAttachedViews(recycler);

        int count = getItemCount();
        for (int p = 0; p < pageSize; p++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    int index = p * onePageSize + r * columns + c;
                    if (index == count) {
                        //跳出多重循环
                        c = columns;
                        r = rows;
                        p = pageSize;
                        break;
                    }

                    View view = recycler.getViewForPosition(index);
                    addView(view);
                    //测量item
                    measureChildWithMargins(view, itemWidthUsed, itemHeightUsed);

                    int width = getDecoratedMeasuredWidth(view);
                    int height = getDecoratedMeasuredHeight(view);
                    //记录显示范围
                    Rect rect = allItemFrames.get(index);
                    if (rect == null) {
                        rect = new Rect();
                    }
                    int x;
                    int y;
                    if (orientation == LinearLayout.HORIZONTAL) {
                         x= p * getUsableWidth() + c * itemWidth;
                        y = r * itemHeight;
                    }else{
                         x =  c * itemWidth;
                         y = p * getUsableHeight() +r * itemHeight;
                    }
                    rect.set(x, y, width + x, height + y);
                    allItemFrames.put(index, rect);


                }
            }
            //每一页循环以后就回收一页的View用于下一页的使用
            removeAndRecycleAllViews(recycler);
        }

        recycleAndFillItems(recycler, state);
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        offsetX = 0;
        offsetY = 0;
    }

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }
        Rect displayRect;
        if (orientation == LinearLayout.HORIZONTAL) {
            displayRect = new Rect(getPaddingLeft() + offsetX, getPaddingTop(), getWidth() - getPaddingLeft() - getPaddingRight() + offsetX, getHeight() - getPaddingTop() - getPaddingBottom());
        }else{
            displayRect = new Rect(getPaddingLeft() , getPaddingTop()+ offsetY, getWidth() - getPaddingLeft() - getPaddingRight() , getHeight() - getPaddingTop() - getPaddingBottom()+ offsetY);
        }
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler);
            }
        }

        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, itemWidthUsed, itemHeightUsed);
                Rect rect = allItemFrames.get(i);
                if (orientation == LinearLayout.HORIZONTAL) {
                    layoutDecorated(view, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom);
                }else{
                    layoutDecorated(view, rect.left, rect.top - offsetY, rect.right, rect.bottom - offsetY);
                }
            }
        }

    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int newX = offsetX + dx;
        int result = dx;
        if (newX > totalWidth) {
            result = totalWidth - offsetX;
        } else if (newX < 0) {
            result = 0 - offsetX;
        }
        offsetX += result;
        offsetChildrenHorizontal(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }

    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally();
    }

    @Override
    public int computeHorizontalScrollExtent(@NonNull RecyclerView.State state) {
        return getWidth();
    }

    @Override
    public int computeHorizontalScrollOffset(@NonNull RecyclerView.State state) {
        return offsetX;
    }

    @Override
    public int computeHorizontalScrollRange(@NonNull RecyclerView.State state) {
        computePageSize(state);
        return pageSize * getWidth();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int newY = offsetY + dy;
        int result = dy;
        if (newY > totalHeight) {
            result = totalHeight - offsetY;
        } else if (newY < 0) {
            result = 0 - offsetY;
        }
        offsetY += result;
        offsetChildrenVertical(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically();
    }

    @Override
    public int computeVerticalScrollExtent(@NonNull RecyclerView.State state) {
        return getHeight();
    }

    @Override
    public int computeVerticalScrollOffset(@NonNull RecyclerView.State state) {
        return offsetY;
    }

    @Override
    public int computeVerticalScrollRange(@NonNull RecyclerView.State state) {
        computePageSize(state);
        return pageSize * getHeight();
    }

    public boolean isLastRow(int index) {
        if (index >= 0 && index < getItemCount()) {
            int indexOfPage = index % onePageSize;
            indexOfPage++;
            if (indexOfPage > (rows - 1) * columns && indexOfPage <= onePageSize) {
                return true;
            }
        }
        return false;
    }

    public boolean isLastColumn(int position) {
        if (position >= 0 && position < getItemCount()) {
            position++;
            if (position % columns == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isPageLast(int position) {
        position++;
        return position % onePageSize == 0;
    }

    public int getPageIndex() {
        return pageSize;
    }
}
