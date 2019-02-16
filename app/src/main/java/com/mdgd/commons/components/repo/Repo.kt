package com.mdgd.commons.components.repo

import android.util.Log
import com.mdgd.commons.components.Constants
import com.mdgd.commons.components.repo.db.IDataBase
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchDTO
import com.mdgd.commons.result.ICallback
import com.mdgd.commons.result.Result
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Max
 * on 23-Jun-17.
 */

class Repo(private val network: INetwork, private val dataBase: IDataBase, private val prefs: IPrefs) : IRepo {

    override fun getEarthquakesBeforeDate(end: Date, callback: ICallback<List<Quake>>) {
        queryData(callback, false, end, end)
    }

    override fun checkNewEarthquakes(callback: ICallback<List<Quake>>) {
        val now = Date()
        val lastDate = Date(prefs.lastUpdateDate)
        Log.d("Repo", "New from $lastDate, till $now")
        network.checkNewEarthquakes(lastDate, ICallback {
            if (it.isSuccess) {
                save(it.data!!)
                queryData(callback, false, now, now)
                prefs.saveLastUpdateDate(now.time)
            } else {
                callback.onResult(it) // show error
                queryData(callback, true, lastDate, lastDate)
            }
        })
    }

    private fun queryData(callback: ICallback<List<Quake>>, isError: Boolean, queryStartTime: Date, initialStartDate: Date) {
        Log.d("Repo", "DB from $queryStartTime")
        val bulk = dataBase.getQuakesBulk(initialStartDate)
        if (isError) callback.onResult(Result(bulk)) // there is problem with network
        else checkQueryResult(bulk, callback, queryStartTime, initialStartDate)
    }

    private fun checkQueryResult(quakes: List<Quake>, callback: ICallback<List<Quake>>, queryStartTime: Date, initialStartDate: Date) {
        when (quakes.size) {
            in Constants.PAGE_SIZE..Int.MAX_VALUE -> callback.onResult(Result(quakes.subList(0, Constants.PAGE_SIZE)))
            else -> getEarthquakes(if (quakes.isEmpty()) queryStartTime else quakes.last().date!!, callback, initialStartDate) // there is not enough in db
        }
    }

    private fun getEarthquakes(end: Date, callback: ICallback<List<Quake>>, initialStartDate: Date) {
        val start = Date(end.time)
        start.hours = start.hours - Constants.TIME_STEP
        Log.d("Repo", "Other from $start, till $end")
        network.getEarthquakes(start, end, ICallback {
            if (it.isSuccess) {
                save(it.data!!)
                queryData(callback, false, start, initialStartDate)
            } else callback.onResult(it)  // show error
        })
    }

    override fun getAllQuakes(searchParams: SearchDTO?): List<Quake> {
//        RealmQuery<Quake> q = dataBase.where(Quake.class);
//        // todo Dan: impl search by other fields
//        if(!TextUtils.isEmpty(searchParams.query)){
//            q.contains("title", searchParams.query);
//        }
//        return q.findAllSorted("date", Sort.DESCENDING);
        return ArrayList()
    }

    override fun save(quakes: List<Quake>) {
        if (!quakes.isEmpty()) dataBase.saveQuakes(quakes)
    }
}
