package com.mdgd.commons.components

import android.content.Context
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.components.repo.Repo
import com.mdgd.commons.components.repo.database.IDataBase
import com.mdgd.commons.components.repo.database.SQLiteManager
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.network.NetworkManager
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.components.repo.prefs.PrefsImp
import com.mdgd.commons.injection.BasicProvider

class ComponentProviderImpl(val ctx: Context): BasicProvider(), IComponentProvider {

    override fun getRepo(): IRepo {
        return Repo(getNetwork(), getQuakesDB(), getPrefs())
    }

    private fun getPrefs(): IPrefs {
        return PrefsImp(ctx)
    }

    private fun getQuakesDB(): IDataBase {
        return SQLiteManager(ctx)
    }

    private fun getNetwork(): INetwork {
        return NetworkManager()
    }
}
