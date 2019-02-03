package com.mdgd.commons.dto

import java.util.*

class Quake {

    var id: String? = null
    var date: Date? = null
    var latitude: Double? = 0.toDouble()
    var longitude: Double? = 0.toDouble()
    var magnitude: String? = null
        set(mMagnitude) {
            field = mMagnitude
            if (field != null && field!!.length < 4) field += "0"
        }
    var link: String? = null
    var title: String? = null
}
