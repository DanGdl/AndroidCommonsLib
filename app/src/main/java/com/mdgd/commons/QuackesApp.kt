package com.mdgd.commons

import android.app.Application
import com.mdgd.commons.components.ComponentProviderImpl
import com.mdgd.commons.components.Injection

/**
 * Created by Max
 * on 30-Apr-17.
 */

class QuackesApp : Application() {

    companion object {
        var instance: QuackesApp? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Injection.provider = ComponentProviderImpl(this)
    }
}
