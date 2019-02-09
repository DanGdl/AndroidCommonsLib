package com.mdgd.commons.components.repo

import com.mdgd.commons.components.Constants
import com.mdgd.commons.components.repo.database.IDataBase
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchDTO
import com.mdgd.commons.retrofit_support.ICallback
import com.mdgd.commons.retrofit_support.Result
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Max
 * on 23-Jun-17.
 */

class Repo(private val network: INetwork, private val dataBase: IDataBase, private val prefs: IPrefs) : IRepo {

    override fun getEarthquakes(end: Date, listener: ICallback<List<Quake>>) {
        val start = Date(end.time)
        start.date = start.date - 1
        network.getEarthquakes(start, end, ICallback {
            if (it.isSuccess){
                save(it.data!!)
                queryData(listener, it.data!!, false, start.time)
            } else {
                listener.onResult(it)  // show error
                queryData(listener, ArrayList(), true, start.time)
            }
        })
    }

    override fun checkNewEarthquakes(listener: ICallback<List<Quake>>) {
        val now = System.currentTimeMillis()
        network.checkNewEarthquakes(prefs.lastUpdateDate, ICallback {
            if(it.isSuccess) {
                if(!it.data!!.isEmpty()) save(it.data!!)
                queryData(listener, it.data!!, false, prefs.lastUpdateDate)
                prefs.saveLastUpdateDate(now)
            } else {
                listener.onResult(it) // show error
                queryData(listener, ArrayList(), true, prefs.lastUpdateDate)
            }
        })
    }

    private fun queryData(callback: ICallback<List<Quake>>, data: List<Quake>, isError: Boolean, queryStartTime: Long) {
        if(data.size >= Constants.PAGE_SIZE) callback.onResult(Result(data.subList(0, Constants.PAGE_SIZE)))
        else if(data.size < Constants.PAGE_SIZE) {
            if(data.isEmpty()){
                val bulk = dataBase.getQuakesBulk(System.currentTimeMillis())
                if(isError) callback.onResult(Result(bulk))
                else checkQueryResult(bulk, callback, queryStartTime)
            } else {
                val bulk = dataBase.getQuakesBulk(data[0].date?.time!!)
                val quakes = mutableListOf<Quake>()
                quakes.addAll(data)
                quakes.addAll(bulk)
                checkQueryResult(quakes, callback, queryStartTime)
            }
        }
    }

    private fun checkQueryResult(quakes: List<Quake>, callback: ICallback<List<Quake>>, queryStartTime: Long) {
        if (quakes.size >= Constants.PAGE_SIZE) queryData(callback, quakes, false, queryStartTime)
        else if(quakes.isEmpty()) getEarthquakes(Date(queryStartTime), callback)
        else getEarthquakes(quakes.last().date!!, callback) // there is not enough in db
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
        dataBase.saveQuakes(quakes)
    }
}
