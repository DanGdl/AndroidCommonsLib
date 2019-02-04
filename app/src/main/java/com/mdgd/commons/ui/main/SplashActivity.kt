package com.mdgd.commons.ui.main

import android.support.v4.app.Fragment
import com.mdgd.commons.components.Injection
import com.mdgd.commons.support.v7.fragment.HostActivity
import com.mdgd.commons.ui.main.fr.quackes.EarthQuakesFragment
import com.mdgd.commons.ui.main.fr.splash.SplashFragment

/**
 * Created by Max
 * on 01-May-17.
 */

class SplashActivity : HostActivity<SplashScreenContract.IPresenter>(), SplashScreenContract.IView {

    override fun getPresenter(): SplashScreenContract.IPresenter {
        return Injection.getSplashPresenter(this)
    }

    override fun getFirstFragment(): Fragment {
        container.postDelayed({ proceedFromSplash() }, 600L)
        return SplashFragment.newInstance()
    }

    private fun proceedFromSplash() {
        replaceFragment(EarthQuakesFragment.newInstance())
    }
}
