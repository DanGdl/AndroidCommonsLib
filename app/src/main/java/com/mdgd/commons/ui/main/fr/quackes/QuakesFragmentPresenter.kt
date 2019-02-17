package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.R
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.dto.SearchParams
import com.mdgd.commons.result.ICallback
import com.mdgd.commons.support.v7.fragment.FragmentPresenter
import java.util.*

class QuakesFragmentPresenter(view: QuakesFragmentContract.IView, private val repo: IRepo) :
        FragmentPresenter<QuakesFragmentContract.IView>(view), QuakesFragmentContract.IPresenter {

    override fun searchQuakes(searchParams: SearchParams?) {
        if (searchParams == null) {
            checkNewEarthQuakes()
            return
        }
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.searchQuakes(searchParams, ICallback {
            view.hideProgress()
            if (it.isFail) view.showToast(R.string.shit, it.error?.message)
            else view.updateEarthQuakes(it.data!!)
        })
    }

    override fun checkNewEarthQuakes() {
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.searchQuakes(SearchParams("", repo.getPrefs().lastUpdateDate), ICallback {
            view.hideProgress()
            if (it.isFail) view.showToast(R.string.shit, it.error?.message)
            else view.updateEarthQuakes(it.data!!)
        })
    }

    override fun getNextBulk(lastDate: Date) {
        view.showProgress(R.string.empty, R.string.wait_please)
        repo.getEarthquakesBeforeDate(SearchParams("", lastDate.time), ICallback {
            view.hideProgress()
            if (it.isFail) view.showToast(R.string.shit, it.error?.message)
            else view.updateEarthQuakes(it.data!!)
        })
        // view.showProgress(R.string.empty, R.string.wait_please)
        // repo.getEarthquakesBeforeDate(SearchParams("", lastDate.time), ICallback {
        //     view.hideProgress()
        //     if (it.isFail) view.showToast(R.string.shit, it.error?.message)
        //     else view.updateEarthQuakes(it.data!!)
        // })
    }
}
