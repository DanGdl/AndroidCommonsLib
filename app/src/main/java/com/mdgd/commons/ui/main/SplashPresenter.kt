package com.mdgd.commons.ui.main

import com.mdgd.commons.R
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.components.repo.network.schemas.QuakeSchema
import com.mdgd.commons.support.v7.mvp.Presenter
import java.util.*

/**
 * Created by max
 * on 04/10/17.
 */

class SplashPresenter(view: SplashScreenContract.IView, val repo: IRepo) : Presenter<SplashScreenContract.IView>(view),
        SplashScreenContract.IPresenter {

    override fun updateQuakes() {
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.checkNewEarthquakes(object : IQuakesCallbackListener {
            override fun onNetworkError(errorMessage: String, errorCode: Int) {
                view.hideProgress()
                view.showToast(R.string.shit, errorMessage)
                view.proceedFromSplash()
            }

            override fun onNetworkSuccess(quakes: List<QuakeSchema>) {
                view.hideProgress()
                repo.saveLastUpdate(Date().time)
                repo.save(quakes)
                view.proceedFromSplash()
            }
        })
    }
}
