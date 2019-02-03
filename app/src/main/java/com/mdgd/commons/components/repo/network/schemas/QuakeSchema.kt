package com.mdgd.commons.components.repo.network.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mdgd.commons.dto.Quake
import java.util.*

/**
 * Created by Max
 * on 01-May-17.
 */

class QuakeSchema {

    @Expose
    @SerializedName("id")
    var id: String? = null

    @Expose
    @SerializedName("properties")
    var properties: PropertiesSchema? = null

    @Expose
    @SerializedName("geometry")
    var geometry: GeometrySchema? = null

    fun fillQuake(q: Quake): Quake {
        q.title = properties!!.title
        q.date = Date(properties!!.time)
        q.id = id
        q.link = properties!!.url
        q.magnitude = "" + properties!!.magnitude

        val coordinates = geometry!!.coordinates
        q.latitude = coordinates!![1]
        q.longitude = coordinates[0]
        return q
    }
}
