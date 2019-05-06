package com.mdgd.commons.ui.main.fr.quackes

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by hanan
 * on 08-Dec-16.
 */

abstract class EndlessScrollListener : RecyclerView.OnScrollListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 5
    // The current offset index of data you have loaded
    private var currentPage = 0
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0
    // True if we are still waiting for the last set of data to load.
    private var loading = true
    // Sets the starting page index
    private val startingPageIndex = 0

    private val reverseDirection: Boolean

    constructor() {
        reverseDirection = false
    }

    constructor(reverseDirection: Boolean) {
        this.reverseDirection = reverseDirection
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        // only trigger action if scrolling down or if there is no enough items to fill screen
        val isTrigger = if (currentPage == 0) {
            if (reverseDirection) dy <= 0 else dy >= 0
        } else {
            if (reverseDirection) dy < 0 else dy > 0
        }

        if (!isTrigger) return
        val lm = recyclerView.layoutManager
        lm ?: return
        val totalItemCount = lm.itemCount
        val lastVisibleItemPosition = (lm as LinearLayoutManager).findLastVisibleItemPosition()

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) this.loading = true
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            loading = false
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, recyclerView)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        this.previousTotalItemCount = 0
        this.currentPage = 0
        this.loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)
}
