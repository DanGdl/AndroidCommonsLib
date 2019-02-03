package com.mdgd.commons.components.repo.network.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Max
 * on 01-May-17.
 */

class PropertiesSchema {

    @Expose
    @SerializedName("title")
    var title: String? = null

    @Expose
    @SerializedName("url")
    var url: String? = null

    @Expose
    @SerializedName("mag")
    var magnitude: Float = 0.toFloat()

    @Expose
    @SerializedName("time")
    var time: Long = 0
}
