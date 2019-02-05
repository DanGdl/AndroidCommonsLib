package com.mdgd.commons.components.repo

import com.mdgd.commons.components.repo.database.IDataBase
import com.mdgd.commons.components.repo.network.INetwork
import com.mdgd.commons.components.repo.prefs.IPrefs
import com.mdgd.commons.dto.Quake
import com.mdgd.commons.dto.SearchDTO
import com.mdgd.commons.retrofit_support.ICallback
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RepoTestJava2 {

    @Mock
    private val network: INetwork? = null
    @Mock
    private val db: IDataBase? = null
    @Mock
    private val prefs: IPrefs? = null

    private var repo: IRepo? = null

    @Before
    fun setUp() {
        repo = Repo(network!!, db!!, prefs!!)
    }

    private fun verifyNoInteraction() {
        Mockito.verifyNoMoreInteractions(network)
        Mockito.verifyNoMoreInteractions(prefs)
        Mockito.verifyNoMoreInteractions(db)
    }

    @Test
    fun getEarthquakes() {
        val end = Date()
        val callback = Mockito.mock(ICallback::class.java) as ICallback<List<Quake>>
        val endCaptor = ArgumentCaptor.forClass(Date::class.java)
        val startCaptor = ArgumentCaptor.forClass(Date::class.java)
        val captor = ArgumentCaptor.forClass(ICallback::class.java) as ArgumentCaptor<ICallback<List<Quake>>>
        // val captor = ArgumentCaptor.forClass<ICallback<List<Quake>>, ICallback<*>>(ICallback<*>::class.java)

        repo!!.getEarthquakes(end, callback)

        Mockito.verify<INetwork>(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture())

        verifyNoInteraction()
    }

    @Test
    fun checkNewEarthquakes() {
        val callback = Mockito.mock(ICallback::class.java) as ICallback<List<Quake>>
        val captor = ArgumentCaptor.forClass<ICallback<List<Quake>>, ICallback<List<Quake>>>(ICallback::class.java)
        // val captor = ArgumentCaptor.forClass(ICallback::class.java) as ArgumentCaptor<ICallback<List<Quake>>>
        val timeCaptor = ArgumentCaptor.forClass(Long::class.java)

        repo!!.checkNewEarthquakes(callback)

        Mockito.verify<INetwork>(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture())
        Mockito.verify<IPrefs>(prefs, Mockito.times(1)).lastUpdateDate
        verifyNoInteraction()
    }

    @Test
    fun getAllQuakes() {
        val params = SearchDTO("", "", "", "", "", "", "")

        repo!!.getAllQuakes(params)

        verifyNoInteraction()
    }

    @Test
    fun save() {
        val quakes = ArrayList<Quake>()

        repo!!.save(quakes)

        Mockito.verify<IDataBase>(db, Mockito.times(1)).saveQuakes(quakes)
        verifyNoInteraction()
    }
}
