package com.mdgd.commons.components.repo

import com.mdgd.commons.dto.Quake
import com.mdgd.commons.retrofit_support.ICallback
import com.mdgd.commons.ui.main.fr.quackes.SearchDTO
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

interface IRepo {

    fun getEarthquakes(start: Date, end: Date, listener: ICallback<List<Quake>>)

    fun checkNewEarthquakes(listener: ICallback<List<Quake>>)

    fun getAllQuakes(searchParams: SearchDTO?): List<Quake>

    fun saveLastUpdate(time: Long)

    fun save(quakes: List<Quake>)
}
