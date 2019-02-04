package com.mdgd.commons.components

import android.content.Context
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.components.repo.Repo
import com.mdgd.commons.components.repo.database.IDataBase
import com.mdgd.commons.components.repo.database.SQLiteManager
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.components.repo.prefs.PrefsImp
import com.mdgd.commons.injection.BasicProvider
import com.mdgd.commons.injection.IInitAction
import java.lang.ref.WeakReference

class ComponentProviderImpl(val ctx: Context): BasicProvider(), IComponentProvider {

    private var repoRef: WeakReference<IRepo>?
    private var prefsRef: WeakReference<IPrefs>?
    private var networkRef: WeakReference<INetwork>?
    private var dbRef: WeakReference<IDataBase>?

    override fun getRepo(): IRepo {
        repoRef = checkIfExists(repoRef, IInitAction { Repo(getNetwork(), getQuakesDB(), getPrefs()) })
        return repoRef.get()!!
    }

    private fun getPrefs(): IPrefs {
        prefsRef = checkIfExists(prefsRef, IInitAction { PrefsImp(ctx) })
        return prefsRef.get()!!
    }

    private fun getQuakesDB(): IDataBase {
        dbRef = checkIfExists(dbRef, IInitAction {  SQLiteManager(ctx) })
        return dbRef.get()!!
    }

    private fun getNetwork(): INetwork {
        networkRef = checkIfExists(networkRef, () -> NetworkManager())
        return networkRef.get()!!
    }
}
