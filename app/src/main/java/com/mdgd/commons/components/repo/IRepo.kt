package com.mdgd.commons.components.repo

import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchParams
import com.mdgd.commons.result.ICallback

/**
 * Created by Max
 * on 23-Jun-17.
 */

interface IRepo {

    fun getPrefs(): IPrefs

    fun getEarthquakesBeforeDate(params: SearchParams, callback: ICallback<List<Quake>>)

    fun searchQuakes(params: SearchParams, callback: ICallback<List<Quake>>)

    fun save(quakes: List<Quake>)
}
