package com.mdgd.commons.components.repo.network.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Max
 * on 01-May-17.
 */

class GeometrySchema {
    // first - lng, second - lat
    @Expose
    @SerializedName("coordinates")
    var coordinates: List<Double>? = null
}
