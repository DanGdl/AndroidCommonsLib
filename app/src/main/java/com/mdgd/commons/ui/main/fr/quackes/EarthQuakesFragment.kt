package com.mdgd.commons.ui.main.fr.quackes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.TimePicker
import com.mdgd.commons.R
import com.mdgd.commons.components.Injection
import com.mdgd.commons.databinding.FragmentRecyclerBinding
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.recycler.CommonRecyclerAdapter
import com.mdgd.commons.support.v7.fragment.recycler.SwipeRecyclerFragment
import java.util.*

/**
 * Created by Max
 * on 01-May-17.
 */
class EarthQuakesFragment : SwipeRecyclerFragment<QuakesFragmentContract.IPresenter, QuakesFragmentContract.IHost, Quake>(),
        QuakesFragmentContract.IView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var binding: FragmentRecyclerBinding? = null
    private var currentPage: Int = 0

    companion object {

        private const val TRANSLATE = -500f

        fun newInstance(): EarthQuakesFragment {
            return EarthQuakesFragment()
        }
    }

    override fun getPresenter(): QuakesFragmentContract.IPresenter {
        return Injection.getQuackesPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler, container, false)
        swipe = binding?.swipeRefresh
        recycler = binding?.recycler
        swipe.setOnRefreshListener(this)
        adapter = getAdapter()
        recycler.adapter = adapter
        setHasProgress(true)
        return binding?.root
    }

    override fun getAdapter(): CommonRecyclerAdapter<Quake> {
        return EarthQuakesAdapter(activity as Context, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.swipeRefresh?.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
        binding?.recycler?.layoutManager = LinearLayoutManager(activity)
        binding?.recycler?.addOnScrollListener(object : EndlessScrollListener() {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                this@EarthQuakesFragment.onLoadMore(page, totalItemsCount, view)
            }
        })

        binding?.toolbarInc?.searchBtn?.setOnClickListener(this)
        binding?.toolbarInc?.extensionToggle?.setOnCheckedChangeListener(this)

        binding?.searchParams?.fromTime?.setOnClickListener(this)
        binding?.searchParams?.fromDate?.setOnClickListener(this)
        binding?.searchParams?.toTime?.setOnClickListener(this)
        binding?.searchParams?.toDate?.setOnClickListener(this)

        binding?.searchParams?.root?.animate()?.translationYBy(TRANSLATE)?.setDuration(1)?.start()

        binding?.swipeRefresh?.isRefreshing = true
        onRefresh()
    }

    fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
        currentPage++
        presenter?.getNextBulk((adapter as EarthQuakesAdapter).lastDate)
    }

    override fun updateEarthQuakes(quakes: List<Quake>) {
        binding?.toolbarInc?.toolbarIcon?.requestFocus()
        if(currentPage == 0) adapter?.setItems(quakes)
        else adapter?.addItems(quakes)
    }

    override fun onRefresh() {
        currentPage = 0
        presenter.checkNewEarthQuakes()
    }

    override fun onItemClicked(item: Quake, position: Int) {
        val dialog = QuakeDialog(activity as Context)
        dialog.setQuake(item)
        dialog.show()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.searchBtn) {
//                SearchDTO searchData = SearchDTO(
//                        binding!!.toolbarInc!!.search.getText().toString(),
//                        binding!!.searchParams!!.fromTime.getText().toString(),
//                        binding!!.searchParams!!.fromDate.getText().toString(),
//                        binding!!.searchParams!!.fromMagnitude.getText().toString(),
//                        binding!!.searchParams!!.toTime.getText().toString(),
//                        binding!!.searchParams!!.toDate.getText().toString(),
//                        binding!!.searchParams!!.toMagnitude.getText().toString())
//                presenter.getEarthQuakes(searchData);
        } else if (id == R.id.fromTime) {
            TimePickerDialog(activity, { _: TimePicker, h: Int, m: Int -> binding?.searchParams?.fromTime?.text = String.format(Locale.getDefault(), "%1$2d : %2$2d", h, m) },
                    0, 0, true).show()
        } else if (id == R.id.toTime) {
            TimePickerDialog(activity, { _: TimePicker, h: Int, m: Int -> binding?.searchParams?.toTime?.text = String.format(Locale.getDefault(), "%1$2d : %2$2d", h, m) },
                    0, 0, true).show()
        } else if (id == R.id.fromDate) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(activity, { _: DatePicker, y: Int, m: Int, d: Int -> binding?.searchParams?.fromDate?.text = String.format(Locale.getDefault(), "%1$2d.%2$2d.%3$4d", d, m, y) },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        } else if (id == R.id.toDate) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(activity, { _: DatePicker, y: Int, m: Int, d: Int -> binding?.searchParams?.toDate?.text = String.format(Locale.getDefault(), "%1$2d.%2$2d.%3$4d", d, m, y) },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        val translateDist = if (b) -1 * TRANSLATE else TRANSLATE
        binding?.searchParams?.root?.animate()?.translationYBy(translateDist)?.setDuration(300)?.start()
    }
}
