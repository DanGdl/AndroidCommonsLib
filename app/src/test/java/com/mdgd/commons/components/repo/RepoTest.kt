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
class RepoTest {

    @Mock
    private var network: INetwork? = null
    @Mock
    private val db: IDataBase? = null
    @Mock
    private val prefs: IPrefs? = null

    private lateinit var repo: IRepo

    @Before
    fun setUp() {
        repo = Repo(network!!, db!!, prefs!!)
    }

    private fun verifyNoInteraction(){
        Mockito.verifyNoMoreInteractions(network)
        Mockito.verifyNoMoreInteractions(prefs)
        Mockito.verifyNoMoreInteractions(db)
    }

    @Test
    fun getEarthquakes() {
        val end = Date()
        val callback = Mockito.mock(ICallback::class.java) as ICallback<List<Quake>>
        val endCaptor: ArgumentCaptor<Date> = ArgumentCaptor.forClass(Date::class.java) as ArgumentCaptor<Date>
        val startCaptor: ArgumentCaptor<Date> = ArgumentCaptor.forClass(Date::class.java) as ArgumentCaptor<Date>
        val captor: ArgumentCaptor<ICallback<List<Quake>>> = ArgumentCaptor.forClass(ICallback::class.java) as ArgumentCaptor<ICallback<List<Quake>>>

        repo.getEarthquakes(end, callback)

        Mockito.verify(network, Mockito.times(1))?.getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture())

        verifyNoInteraction()
    }

    @Test
    fun checkNewEarthquakes() {
        val callback = Mockito.mock(ICallback::class.java) as ICallback<List<Quake>>
        val captor: ArgumentCaptor<ICallback<List<Quake>>> =
                ArgumentCaptor.forClass(ICallback::class.java) as ArgumentCaptor<ICallback<List<Quake>>>
        val timeCaptor: ArgumentCaptor<Long> = ArgumentCaptor.forClass(Long::class.java) as ArgumentCaptor<Long>

        repo.checkNewEarthquakes(callback)

        Mockito.verify(network, Mockito.times(1))?.checkNewEarthquakes(timeCaptor.capture(),
                captor.capture())
        verifyNoInteraction()
    }

    @Test
    fun getAllQuakes() {
        val params = SearchDTO("", "", "", "", "", "", "")

        repo.getAllQuakes(params)

        verifyNoInteraction()
    }

    @Test
    fun save() {
        val quakes: List<Quake> = ArrayList()

        repo.save(quakes)

        Mockito.verify(db, Mockito.times(1))?.saveQuakes(quakes)
        verifyNoInteraction()
    }
}
