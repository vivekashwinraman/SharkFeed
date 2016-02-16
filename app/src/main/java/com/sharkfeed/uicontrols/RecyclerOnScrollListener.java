package com.sharkfeed.uicontrols;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by vraman on 2/14/16.
 */
public abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static String TAG = "RecyclerOnScrollListener";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItemPosition, childCount, itemCount;
    private int current_page = 1;
    private GridLayoutManager gridLayoutManager;

    /************************************************************************************************/
    public RecyclerOnScrollListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    /************************************************************************************************/
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        childCount = gridLayoutManager.getChildCount();
        itemCount = gridLayoutManager.getItemCount();
        firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            if (itemCount > previousTotal) {
                loading = false;
                previousTotal = itemCount;
            }
        }
        if (!loading && (itemCount - childCount)
                <= (firstVisibleItemPosition + visibleThreshold)) {
            current_page++;
            onLoadMore(current_page);
            loading = true;
        }
    }

    /************************************************************************************************/
    public abstract void onLoadMore(int current_page);
    /************************************************************************************************/
}
