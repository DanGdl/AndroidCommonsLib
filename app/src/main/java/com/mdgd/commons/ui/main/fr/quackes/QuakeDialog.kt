package com.mdgd.commons.ui.main.fr.quackes

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.mdgd.commons.R
import com.mdgd.commons.dto.Quake
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Max
 * on 02-May-17.
 */

class QuakeDialog internal constructor(context: Context) : Dialog(context) {
    private var mDetails: TextView? = null

    init {
        init()
    }

    private fun init() {
        setContentView(R.layout.dialog_quake)

        setTitle(R.string.quake_time)
        mDetails = findViewById(R.id.quakeDetails)
        setCanceledOnTouchOutside(true)
    }

    fun setQuake(quake: Quake) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dateString = sdf.format(quake.date)

        setTitle(dateString)
        mDetails?.text = context.resources.getString(R.string.quake_details, quake.magnitude, quake.title, quake.link)
    }
}
