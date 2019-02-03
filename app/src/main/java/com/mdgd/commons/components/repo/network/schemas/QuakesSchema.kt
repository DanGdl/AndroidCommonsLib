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
    private var mQuakes: List<QuakeSchema>? = null

    val earthquakes: List<QuakeSchema>
        get() = if (mQuakes == null) ArrayList()
        else mQuakes!!

    fun setQuakes(mQuakes: List<QuakeSchema>) {
        this.mQuakes = mQuakes
    }
}
