package com.mdgd.commons.components.repo

import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchDTO
import com.mdgd.commons.result.ICallback
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

interface IRepo {

    fun getEarthquakesBeforeDate(end: Date, callback: ICallback<List<Quake>>)

    fun checkNewEarthquakes(callback: ICallback<List<Quake>>)

    fun getAllQuakes(searchParams: SearchDTO?): List<Quake>

    fun save(quakes: List<Quake>)
}
