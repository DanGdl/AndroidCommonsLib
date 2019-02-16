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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quake

        if (id != other.id) return false
        if (date != other.date) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (magnitude != other.magnitude) return false
        if (link != other.link) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (magnitude?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }


}
