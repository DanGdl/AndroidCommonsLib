package com.mdgd.commons.components.repo

import com.mdgd.commons.components.repo.database.IDataBase
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.retrofitSupport.ICallback
import com.mdgd.commons.ui.main.fr.quackes.SearchDTO
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

class Repo(private val network: INetwork, private val dataBase: IDataBase, private val prefs: IPrefs) : IRepo {

    override fun getEarthquakes(start: Date, end: Date, listener: ICallback<List<Quake>>) {
        network.getEarthquakes(start, end, listener)
    }

    override fun checkNewEarthquakes(listener: ICallback<List<Quake>>) {
        network.checkNewEarthquakes(prefs.lastUpdateDate, listener)
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

    override fun saveLastUpdate(time: Long) {
        prefs.saveLastUpdateDate(time)
    }

    override fun save(quakes: List<Quake>) {
        dataBase.saveQuakes(quakes)
    }
}
