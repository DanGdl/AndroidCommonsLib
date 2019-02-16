package com.mdgd.commons.components.repo.db

import com.mdgd.commons.dto.Quake
import java.util.*

/**
 * Created by Max
 * on 23-Jun-17.
 */

interface IDataBase {

    fun saveQuakes(quakes: List<Quake>)

    fun getQuakesBulk(date: Date): List<Quake>
}
