package com.mdgd.commons.ui.main.fr.quackes

import com.mdgd.commons.contract.fragment.FragmentContract
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchParams
import java.util.*

/**
 * Created by max
 * on 2/2/18.
 */

class QuakesFragmentContract {

    interface IView : FragmentContract.IView {
        fun updateEarthQuakes(quakes: List<Quake>)
        fun decreasePage()
    }

    interface IHost : FragmentContract.IHost

    interface IPresenter : FragmentContract.IPresenter {
        fun searchQuakes(searchParams: SearchParams?)

        fun checkNewEarthQuakes()

        fun getNextBulk(lastDate: Date)
    }
}
