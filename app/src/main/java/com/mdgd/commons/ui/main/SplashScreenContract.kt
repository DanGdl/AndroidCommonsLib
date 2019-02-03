package com.mdgd.commons.ui.main

import com.mdgd.commons.contract.mvp.ViewContract


/**
 * Created by max
 * on 2/2/18.
 */

class SplashScreenContract {

    interface IPresenter: ViewContract.IPresenter {
        fun updateQuakes()
    }

    interface IView : ViewContract.IView {
        fun proceedFromSplash()
    }
}
