package com.mdgd.commons.dto

import android.text.TextUtils

/**
 * Created by max
 * on 2/2/18.
 */

class SearchParams(val query: String = "", val fromDate: Long = DEF_TIME,
                   val fromMagnitude: String = "", val toDate: Long = DEF_TIME,
                   val toMagnitude: String = "") {

    val isEmpty: Boolean
        get() = (TextUtils.isEmpty(query) && fromDate == DEF_TIME
                && TextUtils.isEmpty(fromMagnitude) && toDate == -DEF_TIME && TextUtils.isEmpty(toMagnitude))

    fun isRegular(): Boolean {
        return (TextUtils.isEmpty(query) && fromDate != DEF_TIME
                && TextUtils.isEmpty(fromMagnitude) && toDate == DEF_TIME && TextUtils.isEmpty(toMagnitude))
    }

    companion object {
        const val DEF_TIME = -1L
    }
}
