package com.mdgd.commons.components.repo.prefs

import android.content.Context
import com.mdgd.commons.prefs.BasicPrefsImpl

/**
 * Created by Max
 * on 02-May-17.
 */

class PrefsImp(ctx: Context): BasicPrefsImpl(ctx), IPrefs {

    companion object {
        private const val LAST_UPDATE = "last_update"
    }

    override fun getDefaultPrefsFileName(): String {
        return "quackes"
    }

    override val lastUpdateDate: Long
        get() = get(LAST_UPDATE, -1L)

    override fun saveLastUpdateDate(millis: Long) {
        put(LAST_UPDATE, millis)
    }
}
