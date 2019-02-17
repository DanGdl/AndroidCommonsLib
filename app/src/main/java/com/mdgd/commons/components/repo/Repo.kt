package com.mdgd.commons.components.repo

import android.util.Log
import com.mdgd.commons.components.Constants
import com.mdgd.commons.components.repo.db.IDataBase
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchParams
import com.mdgd.commons.result.ICallback
import com.mdgd.commons.result.Result
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

class Repo(private val network: INetwork, private val dataBase: IDataBase, private val prefs: IPrefs) : IRepo {

    override fun getPrefs(): IPrefs {
        return prefs
    }

    override fun getEarthquakesBeforeDate(params: SearchParams, callback: ICallback<List<Quake>>) {
        queryData(callback, false, params, params.toDate)
    }

    override fun searchQuakes(params: SearchParams, callback: ICallback<List<Quake>>) {
        val now = if (params.toDate == SearchParams.DEF_TIME) System.currentTimeMillis() else params.toDate
        Log.d("QUERY", "S From ${Date(params.fromDate)} till ${Date()}")
        network.checkNewEarthquakes(Date(params.fromDate), ICallback {
            if (it.isSuccess) {
                save(it.data!!)
                val paramsUpd = SearchParams(params.query, params.fromDate, params.fromMagnitude, now, params.toMagnitude)
                queryData(callback, false, paramsUpd, now)
                prefs.saveLastUpdateDate(now)
            } else {
                callback.onResult(it) // show error
                queryData(callback, true, params, params.fromDate)
            }
        })
    }

    private fun queryData(callback: ICallback<List<Quake>>, isError: Boolean, params: SearchParams, initialStartDate: Long) {
        val bulk = dataBase.getQuakesBulk(initialStartDate)
        if (isError) callback.onResult(Result(bulk)) // there is problem with network
        else checkQueryResult(bulk, callback, params, initialStartDate)
    }

    private fun checkQueryResult(quakes: List<Quake>, callback: ICallback<List<Quake>>, params: SearchParams, initialStartDate: Long) {
        when (quakes.size) {
            in Constants.PAGE_SIZE..Int.MAX_VALUE -> callback.onResult(Result(quakes.subList(0, Constants.PAGE_SIZE)))
            else -> { // there is not enough in db
                val toDate = if (quakes.isEmpty()) params.fromDate else quakes.last().date!!.time // avoid duplicate requests
                val paramsUpd = SearchParams(params.query, params.fromDate, params.fromMagnitude, toDate, params.toMagnitude)
                getEarthquakes(paramsUpd, callback, initialStartDate)
            }
        }
    }

    private fun getEarthquakes(params: SearchParams, callback: ICallback<List<Quake>>, initialStartDate: Long) {
        val start = Date(params.toDate)
        start.hours = start.hours - Constants.TIME_STEP
        val end = Date(params.toDate)
        Log.d("QUERY", "U From $start till $end")
        network.getEarthquakes(start, end, ICallback {
            if (it.isSuccess) {
                save(it.data!!)
                val paramsUpd = SearchParams(params.query, start.time, params.fromMagnitude, end.time, params.toMagnitude)
                queryData(callback, false, paramsUpd, initialStartDate)
            } else callback.onResult(it)  // show error
        })
    }

    override fun save(quakes: List<Quake>) {
        if (!quakes.isEmpty()) dataBase.saveQuakes(quakes)
    }
}
