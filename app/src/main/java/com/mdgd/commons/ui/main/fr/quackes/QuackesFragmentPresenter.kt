package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.R
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.dto.SearchDTO
import com.mdgd.commons.retrofit_support.ICallback
import com.mdgd.commons.support.v7.fragment.FragmentPresenter
import java.util.*

class QuackesFragmentPresenter(view: QuakesFragmentContract.IView, private val repo: IRepo) :
        FragmentPresenter<QuakesFragmentContract.IView>(view), QuakesFragmentContract.IPresenter {

    private var query: SearchDTO? = null

    override fun getEarthQuakes(searchParams: SearchDTO?) {
        this.query = searchParams
        val quakes = repo.getAllQuakes(searchParams)
        view.updateEarthQuakes(quakes)
    }

    override fun checkNewEarthQuakes() {
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.checkNewEarthquakes(ICallback {
            view.hideProgress()
            if (it.isFail) view.showToast(R.string.shit, it.error?.message)
            else view.updateEarthQuakes(it.data!!)
        })
    }

    override fun getNextBulk(lastDate: Long) {
        if (lastDate != -1L && (query == null || query?.isEmpty!!)) {
            view.showProgress(R.string.empty, R.string.wait_please)
            val end = Date(lastDate)
            repo.getEarthquakes(end, ICallback {
                view.hideProgress()
                if (it.isFail) view.showToast(R.string.shit, it.error?.message)
                else view.updateEarthQuakes(it.data!!)
            })
        }
    }
}
