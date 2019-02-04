package com.mdgd.commons.components.repo.network.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Max
 * on 30-Apr-17.
 */

class QuakesSchema {

    @Expose
    @SerializedName("features")
    private var quakes: List<QuakeSchema>? = null

    val earthquakes: List<QuakeSchema>
        get() = if (quakes == null) ArrayList()
        else quakes!!

    fun setQuakes(mQuakes: List<QuakeSchema>) {
        this.quakes = mQuakes
    }
}
