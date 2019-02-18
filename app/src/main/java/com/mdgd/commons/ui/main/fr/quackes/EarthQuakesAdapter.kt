package com.mdgd.commons.ui.main.fr.quackes

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewGroup
import com.mdgd.commons.R
import com.mdgd.commons.databinding.QuakeDetailsBinding
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.recycler.CommonRecyclerAdapter
import com.mdgd.commons.recycler.CommonViewHolder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Max
 * on 01-May-17.
 */
class EarthQuakesAdapter internal constructor(context: Context, listener: CommonRecyclerAdapter.IOnItemClickListener<Quake>) : CommonRecyclerAdapter<Quake>(context, listener) {

    private val mSDF = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault())

    val lastDate: Date
        get() = if (items.isEmpty()) Date()
        else items.last().date!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<Quake> {
        val cardBinding = DataBindingUtil.inflate<QuakeDetailsBinding>(inflater, R.layout.quake_details, parent, false)
        return QuakeVH(cardBinding)
    }

    private inner class QuakeVH internal constructor(internal val mBinding: QuakeDetailsBinding) :
            CommonViewHolder<Quake>(mBinding.root), View.OnClickListener {

        init {
            mBinding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (listener == null) return
            val position = adapterPosition
            listener.onItemClicked(items[position], position)
        }

        override fun bindItem(item: Quake?, position: Int) {
            mBinding.quakeTime.text = mSDF.format(item?.date)
            mBinding.quake = item
        }
    }

}
