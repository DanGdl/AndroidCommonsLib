package com.mdgd.commons.components.repo.network

import com.mdgd.commons.dto.Quake
import com.mdgd.commons.result.ICallback
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

interface INetwork {
    fun getEarthquakes(start: Date, end: Date, listener: ICallback<List<Quake>>)

    fun checkNewEarthquakes(lastUpdate: Date, listener: ICallback<List<Quake>>)
}
