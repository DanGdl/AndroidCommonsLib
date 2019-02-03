package com.mdgd.commons.ui.main.fr.quackes

import android.text.TextUtils

/**
 * Created by max
 * on 2/2/18.
 */

class SearchDTO(val query: String, val fromTime: String, val fromDate: String,
                val fromMagnitude: String, val toTime: String,
                val toDate: String, val toMagnitude: String) {

    val isEmpty: Boolean
        get() = (TextUtils.isEmpty(query)
                && TextUtils.isEmpty(fromTime) && TextUtils.isEmpty(fromDate)
                && TextUtils.isEmpty(fromMagnitude) && TextUtils.isEmpty(toTime)
                && TextUtils.isEmpty(toDate) && TextUtils.isEmpty(toMagnitude))
}
