package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.R
import com.mdgd.commons.components.repo.IRepo
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.retrofit_support.ICallback
import com.mdgd.commons.retrofit_support.Result
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
        repo.checkNewEarthquakes(object : ICallback<List<Quake>> {
            override fun onResult(result: Result<List<Quake>>) {
                if(result.isFail()){
                    view.hideProgress()
                    view.showToast(R.string.shit, result.error?.message)
                } else {
                    repo.saveLastUpdate(Date().time)
                    repo.save(result.data!!)
                    view.hideProgress()
                    view.updateEarthQuakes(result.data!!)
                }
            }
        })
    }

    override fun getNextBulk(lastDate: Long) {
        if (lastDate != -1L && query?.isEmpty!!) {
            view.showProgress(R.string.empty, R.string.wait_please)
            val end = Date(lastDate)
            val start = Date(lastDate)
            start.date = start.date - 1
            repo.getEarthquakes(start, end, object : ICallback<List<Quake>> {
                override fun onResult(result: Result<List<Quake>>) {
                    if(result.isFail()){
                        view.hideProgress()
                        view.showToast(R.string.shit, result.error?.message)
                    } else {
                        repo.save(result.data!!)
                        view.hideProgress()
                        view.updateEarthQuakes(result.data!!)
                    }
                }
            })
        }
    }
}
