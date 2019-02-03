package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.R
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.components.repo.network.schemas.QuakeSchema
import com.mdgd.commons.support.v7.fragment.FragmentPresenter
import java.util.*

class QuackesFragmentPresenter(view: QuakesFragmentContract.IView, val repo: IRepo):
        FragmentPresenter<QuakesFragmentContract.IView>(view), QuakesFragmentContract.IPresenter {

    private var query: SearchDTO? = null

    override fun getEarthQuakes(searchParams: SearchDTO?) {
        this.query = searchParams
        val quakes = repo.getAllQuakes(searchParams)
        view.updateEarthQuakes(quakes)
    }

    override fun checkNewEarthQuakes() {
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.checkNewEarthquakes(object : IQuakesCallbackListener {
            override fun onNetworkError(errorMessage: String, errorCode: Int) {
                view.hideProgress()
                view.showToast(R.string.shit, errorMessage)
            }

            override fun onNetworkSuccess(quakes: List<QuakeSchema>) {
                view.hideProgress()
                repo.saveLastUpdate(Date().time)
                repo.save(quakes) // real will update view automatically
            }
        })
    }

    override fun getNextBulk(lastDate: Long) {
        if (lastDate != -1L && query?.isEmpty!!) {
            view.showProgress(R.string.empty, R.string.wait_please)
            val end = Date(lastDate)
            val start = Date(lastDate)
            start.date = start.date - 1
            repo.getEarthquakes(start, end, object : IQuakesCallbackListener {
                override fun onNetworkError(errorMessage: String, errorCode: Int) {
                    view.showToast(R.string.shit, errorMessage)

                    view.hideProgress()
                }

                override fun onNetworkSuccess(quakes: List<QuakeSchema>) {
                    view.hideProgress()
                    repo.save(quakes)
                }
            })
        }
    }
}
