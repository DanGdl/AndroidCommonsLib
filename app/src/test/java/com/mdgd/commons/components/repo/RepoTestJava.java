package com.mdgd.commons.components.repo;

import com.mdgd.commons.components.repo.database.IDataBase;
import com.mdgd.commons.components.repo.network.INetwork;
import com.mdgd.commons.components.repo.prefs.IPrefs;
import com.mdgd.commons.dto.Quake;
import com.mdgd.commons.dto.SearchDTO;
import com.mdgd.commons.retrofit_support.ICallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RepoTestJava {

    @Mock
    private INetwork network;
    @Mock
    private IDataBase db;
    @Mock
    private IPrefs prefs;

    private IRepo repo;

    @Before
    public void setUp() {
        repo = new Repo(network, db, prefs);
    }

    private void verifyNoInteraction(){
        Mockito.verifyNoMoreInteractions(network);
        Mockito.verifyNoMoreInteractions(prefs);
        Mockito.verifyNoMoreInteractions(db);
    }

    @Test
    public void getEarthquakes() {
        final Date end = new Date();
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.getEarthquakes(end, callback);

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes() {
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Mockito.verify(prefs, Mockito.times(1)).getLastUpdateDate();
        verifyNoInteraction();
    }

    @Test
    public void getAllQuakes() {
        final SearchDTO params = new SearchDTO("", "", "", "", "", "", "");

        repo.getAllQuakes(params);

        verifyNoInteraction();
    }

    @Test
    public void save() {
        final List<Quake> quakes = new ArrayList<>();

        repo.save(quakes);

        Mockito.verify(db, Mockito.times(1)).saveQuakes(quakes);
        verifyNoInteraction();
    }
}
