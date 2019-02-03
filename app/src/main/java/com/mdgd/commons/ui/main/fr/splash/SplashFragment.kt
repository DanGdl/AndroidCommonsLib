package com.mdgd.commons.ui.main.fr.splash

import com.mdgd.commons.R
import com.mdgd.commons.components.Injection
import com.mdgd.commons.support.v7.fragment.HostedFragment

/**
 * Created by Max
 * on 01-May-17.
 */

class SplashFragment : HostedFragment<SplashFragmentContract.IPresenter, SplashFragmentContract.IHost>(),
        SplashFragmentContract.IView {

    companion object {

        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

    override fun getPresenter(): SplashFragmentContract.IPresenter {
        return Injection.getSplashFragmentPresenter(this)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_splash
    }
}
