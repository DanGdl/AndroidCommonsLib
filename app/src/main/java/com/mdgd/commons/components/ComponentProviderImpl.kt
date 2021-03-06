package com.mdgd.commons.components

import android.content.Context
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.components.repo.Repo
import com.mdgd.commons.components.repo.db.IDataBase
import com.mdgd.commons.components.repo.db.SQLiteManager
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.network.NetworkManager
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.components.repo.prefs.PrefsImp
import com.mdgd.commons.injection.BasicProvider
import java.lang.ref.WeakReference

class ComponentProviderImpl(val ctx: Context) : BasicProvider(), IComponentProvider {

    private var repoRef: WeakReference<IRepo>? = null
    private var prefsRef: WeakReference<IPrefs>? = null
    private var networkRef: WeakReference<INetwork>? = null
    private var dbRef: WeakReference<IDataBase>? = null

    override fun getRepo(): IRepo {
        repoRef = checkIfExists(repoRef) { Repo(getNetwork(), getQuakesDB(), getPrefs()) }
        return repoRef!!.get()!!
    }

    private fun getPrefs(): IPrefs {
        prefsRef = checkIfExists(prefsRef) { PrefsImp(ctx) }
        return prefsRef!!.get()!!
    }

    private fun getQuakesDB(): IDataBase {
        dbRef = checkIfExists(dbRef) { SQLiteManager(ctx) }
        return dbRef!!.get()!!
    }

    private fun getNetwork(): INetwork {
        networkRef = checkIfExists(networkRef) { NetworkManager() }
        return networkRef!!.get()!!
    }
}
