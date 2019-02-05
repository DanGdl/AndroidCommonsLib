package com.mdgd.commons.components.repo;

import com.mdgd.commons.DefMatcherImp;
import com.mdgd.commons.components.repo.database.IDataBase;
import com.mdgd.commons.components.repo.network.INetwork;
import com.mdgd.commons.components.repo.prefs.IPrefs;
import com.mdgd.commons.dto.Quake;
import com.mdgd.commons.dto.SearchDTO;
import com.mdgd.commons.retrofit_support.ICallback;
import com.mdgd.commons.retrofit_support.Result;

import org.junit.Assert;
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

    private static final Long MOCK_TIME = System.currentTimeMillis() - 5 * 3600_000;

    @Mock private INetwork network;
    @Mock private IDataBase db;
    @Mock private IPrefs prefs;

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
    public void checkNewEarthquakes_fail_emptyDb() {
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Result<List<Quake>>> resultCaptor = ArgumentCaptor.forClass(Result.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(prefs, Mockito.times(1)).getLastUpdateDate();
        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new Exception("Mock!")));
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        Assert.assertThat(timeCaptor.getAllValues().get(1), new DefMatcherImp<Long>("Time not matches: ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 3;
            }
        });
        Mockito.verify(callback, Mockito.times(1)).onResult(resultCaptor.capture());
        Assert.assertTrue(resultCaptor.getValue().data.isEmpty());
        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_fail_inDbNotEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            quakes.add(new Quake());
        }
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Result<List<Quake>>> resultCaptor = ArgumentCaptor.forClass(Result.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(prefs, Mockito.times(1)).getLastUpdateDate();
        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new Exception("Mock!")));
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        Assert.assertThat(timeCaptor.getAllValues().get(1), new DefMatcherImp<Long>("Time not matches: ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 3;
            }
        });
        Mockito.verify(callback, Mockito.times(1)).onResult(resultCaptor.capture());
        Assert.assertEquals(quakes, resultCaptor.getValue().data);
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
