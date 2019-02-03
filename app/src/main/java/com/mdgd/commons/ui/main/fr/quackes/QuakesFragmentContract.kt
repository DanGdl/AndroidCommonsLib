package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.contract.fragment.FragmentContract
import com.mdgd.commons.dto.Quake

/**
 * Created by max
 * on 2/2/18.
 */

class QuakesFragmentContract {

    interface IView: FragmentContract.IView {
        fun updateEarthQuakes(quakes: List<Quake>)
    }

    interface IHost: FragmentContract.IHost

    interface IPresenter: FragmentContract.IPresenter {
        fun getEarthQuakes(searchParams: SearchDTO?)

        fun checkNewEarthQuakes()

        fun getNextBulk(lastDate: Long)
    }
}
