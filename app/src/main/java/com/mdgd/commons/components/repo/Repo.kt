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

    private var tryout: Int = 0
    private val maxTryout: Int = 1

    override fun getEarthquakes(end: Date, listener: ICallback<List<Quake>>) {
        if(tryout > maxTryout){
            tryout = 0
            return
        }

        val start = Date(end.time)
        start.date = start.date - 1
        network.getEarthquakes(start, end, object: ICallback<List<Quake>>{
            override fun onResult(result: Result<List<Quake>>) {
                if (result.isSuccess()){
                    save(result.data!!)
                    queryData(listener, result.data!!)
                } else {
                    listener.onResult(result)
                    tryout += 1
                    queryData(listener, ArrayList())
                }
            }
        })
    }

    override fun checkNewEarthquakes(listener: ICallback<List<Quake>>) {
        tryout = 0
        network.checkNewEarthquakes(prefs.lastUpdateDate, object: ICallback<List<Quake>>{
            override fun onResult(result: Result<List<Quake>>) {
                if(result.isSuccess()) {
                    prefs.saveLastUpdateDate(Date().time)
                    save(result.data!!)
                    queryData(listener, result.data!!)
                } else {
                    listener.onResult(result)
                    queryData(listener, ArrayList())
                }
            }
        })
    }

    private fun queryData(callback: ICallback<List<Quake>>, data: List<Quake>) {
        if(data.size >= Constants.PAGE_SIZE) {
            callback.onResult(Result(data.subList(0, Constants.PAGE_SIZE)))
        }
        else if(data.size < Constants.PAGE_SIZE) {
            val bulk = dataBase.getQuakesBulk(data[0].date?.time!!)
            val quakes = mutableListOf<Quake>()
            quakes.addAll(data)
            quakes.addAll(bulk)
            if(quakes.size >= Constants.PAGE_SIZE) queryData(callback, quakes)
            else {
                callback.onResult(Result(quakes))
                getEarthquakes(quakes.last().date!!, callback)
            }
        }
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
