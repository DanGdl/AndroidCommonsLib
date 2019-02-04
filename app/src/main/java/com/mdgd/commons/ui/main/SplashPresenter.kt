package com.mdgd.commons.ui.main

import com.mdgd.commons.support.v7.mvp.Presenter

/**
 * Created by max
 * on 04/10/17.
 */

class SplashPresenter(view: SplashScreenContract.IView) : Presenter<SplashScreenContract.IView>(view),
        SplashScreenContract.IPresenter
