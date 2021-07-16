package com.mdgd.commons.dto

import android.text.TextUtils

/**
 * Created by max
 * on 2/2/18.
 */

class SearchParams(val query: String = "", val stDate: Long = DEF_TIME,
                   val stMag: String = "", val endDate: Long = DEF_TIME,
                   val endMag: String = "") {

    val isEmpty: Boolean
        get() = (TextUtils.isEmpty(query) && stDate == DEF_TIME
                && TextUtils.isEmpty(stMag) && endDate == -DEF_TIME && TextUtils.isEmpty(endMag))

    fun isRegular(): Boolean {
        return (TextUtils.isEmpty(query) && stDate != DEF_TIME
                && TextUtils.isEmpty(stMag) && endDate == DEF_TIME && TextUtils.isEmpty(endMag))
    }

    companion object {
        const val DEF_TIME = -1L
    }
}
