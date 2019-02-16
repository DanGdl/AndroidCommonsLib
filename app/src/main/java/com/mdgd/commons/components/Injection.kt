package com.mdgd.commons.components

import com.mdgd.commons.ui.main.SplashPresenter
import com.mdgd.commons.ui.main.SplashScreenContract
import com.mdgd.commons.ui.main.fr.quackes.QuakesFragmentContract
import com.mdgd.commons.ui.main.fr.quackes.QuakesFragmentPresenter
import com.mdgd.commons.ui.main.fr.splash.SplashFragmentContract
import com.mdgd.commons.ui.main.fr.splash.SplashFragmentPresenter

/**
 * Created by Owner
 * on 19/03/2018.
 */

object Injection {

    lateinit var provider: IComponentProvider

    fun getSplashPresenter(view: SplashScreenContract.IView): SplashScreenContract.IPresenter {
        return SplashPresenter(view)
    }

    fun getSplashFragmentPresenter(view: SplashFragmentContract.IView): SplashFragmentContract.IPresenter {
        return SplashFragmentPresenter(view)
    }

    fun getQuackesPresenter(view: QuakesFragmentContract.IView): QuakesFragmentContract.IPresenter {
        return QuakesFragmentPresenter(view, provider.getRepo())
    }
}
