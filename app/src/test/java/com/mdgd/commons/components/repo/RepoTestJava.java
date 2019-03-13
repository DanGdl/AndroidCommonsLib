package com.mdgd.commons.components.repo;

import com.mdgd.commons.DefMatcherImp;
import com.mdgd.commons.components.Constants;
import com.mdgd.commons.components.repo.db.IDataBase;
import com.mdgd.commons.components.repo.network.INetwork;
import com.mdgd.commons.components.repo.prefs.IPrefs;
import com.mdgd.commons.dto.Quake;
import com.mdgd.commons.dto.SearchParams;
import com.mdgd.commons.result.ICallback;
import com.mdgd.commons.result.Result;

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

    private void verifyNoInteraction() {
        Mockito.verifyNoMoreInteractions(network);
        Mockito.verifyNoMoreInteractions(prefs);
        Mockito.verifyNoMoreInteractions(db);
    }


    //  ------------------------------------------------------------------------------------------------
    @Test
    public void getEarthquakes_error_emptyDb() {
        final Date end = new Date(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.getEarthquakes(end, callback);

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());
        captor.getValue().onResult(new Result<>(new Exception("Mock!")));

        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        verifyNoInteraction();
    }

    @Test
    public void getEarthquakes_error_dbNotEnough() {
        final Date end = new Date(MOCK_TIME);
        final List<Quake> quakes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            quakes.add(new Quake());
        }
        Mockito.when(db.getQuakesBulk(ArgumentCaptor.forClass(Long.class).capture())).thenReturn(quakes);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.getEarthquakes(end, callback);

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());
        captor.getValue().onResult(new Result<>(new Exception("Mock!")));

        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        verifyNoInteraction();
    }

    @Test
    public void getEarthquakes_success_emptyDb() {
        final Date end = new Date(MOCK_TIME);
        Mockito.when(db.getQuakesBulk(ArgumentCaptor.forClass(Long.class).capture())).thenReturn(new ArrayList<>());
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.getEarthquakes(end, callback);

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());
        captor.getValue().onResult(new Result<>(new ArrayList<>()));

        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        Mockito.verify(network, Mockito.times(2)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());

        verifyNoInteraction();
    }


    //  ------------------------------------------------------------------------------------------------
    @Test
    public void checkNewEarthquakes_fail_emptyDb() {
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<Result<List<Quake>>> resultCaptor = ArgumentCaptor.forClass(Result.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new Exception("Mock!")));
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());
        final List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(allValues.size() - 1), new DefMatcherImp<Long>("Time not matches: ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 10;
            }
        });
        Mockito.verify(callback, Mockito.times(2)).onResult(resultCaptor.capture());
        Assert.assertEquals(quakes, resultCaptor.getValue().data);
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_fail_inDbNotEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
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
        final List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(allValues.size() - 1), new DefMatcherImp<Long>("Time not matches: ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 10;
            }
        });
        Mockito.verify(callback, Mockito.times(2)).onResult(resultCaptor.capture());
        Assert.assertEquals(quakes, resultCaptor.getValue().data);
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_successEmpty_emptyDb() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new ArrayList<>()));
        Mockito.verify(prefs, Mockito.times(1)).saveLastUpdateDate(timeCaptor.capture());
        List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(2), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 20;
            }
        });
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());

        allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(3), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 15;
            }
        });

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();
        final Date endDate = new Date(MOCK_TIME);
        Assert.assertEquals(endDate, endCaptor.getValue());
        endDate.setDate(endDate.getDate() - 1);
        Assert.assertEquals(endDate, startCaptor.getValue());

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_successEmpty_inDbNotEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final Quake quake = new Quake();
            quake.setDate(new Date(System.currentTimeMillis() - i * 3600_000));
            quakes.add(quake);
        }
        final Date lastQuakeDate = quakes.get(quakes.size() - 1).getDate();
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new ArrayList<>()));
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();
        Mockito.verify(prefs, Mockito.times(1)).saveLastUpdateDate(timeCaptor.capture());
        List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(2), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 30;
            }
        });
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());

        allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(3), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 15;
            }
        });

        Mockito.verify(network, Mockito.times(1)).getEarthquakes(startCaptor.capture(),
                endCaptor.capture(), captor.capture());
        Assert.assertEquals(lastQuakeDate, endCaptor.getValue());
        lastQuakeDate.setDate(lastQuakeDate.getDate() - 1);
        Assert.assertEquals(lastQuakeDate, startCaptor.getValue());

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_successEmpty_inDbEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        for (int i = 0; i < Constants.PAGE_SIZE; i++) {
            final Quake quake = new Quake();
            quake.setDate(new Date(System.currentTimeMillis() - i * 3600_000));
            quakes.add(quake);
        }
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(new ArrayList<>()));
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();
        Mockito.verify(prefs, Mockito.times(1)).saveLastUpdateDate(timeCaptor.capture());
        List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(2), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 30;
            }
        });
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());

        allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(3), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 15;
            }
        });

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_successNotEmpty_inDbNotEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        final List<Quake> response = new ArrayList<>();
        for (int i = 0; i < Constants.PAGE_SIZE; i++) {
            final Quake quake = new Quake();
            quake.setDate(new Date(System.currentTimeMillis() - i * 3600_000));
            if (i < Constants.PAGE_SIZE / 2) response.add(quake);
            else quakes.add(quake);
        }
        Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(response));
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();
        Mockito.verify(prefs, Mockito.times(1)).saveLastUpdateDate(timeCaptor.capture());
        List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(2), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 30;
            }
        });
        Mockito.verify(db, Mockito.times(1)).getQuakesBulk(timeCaptor.capture());

        allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(3), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                expected = System.currentTimeMillis();
                return time <= expected && time > expected - 50;
            }
        });
        Mockito.verify(db, Mockito.times(1)).saveQuakes(response);

        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthquakes_successEnough() {
        final ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        final List<Quake> quakes = new ArrayList<>();
        for (int i = 0; i < Constants.PAGE_SIZE; i++) {
            final Quake quake = new Quake();
            quake.setDate(new Date(System.currentTimeMillis() - i * 3600_000));
            quakes.add(quake);
        }
        //Mockito.when(db.getQuakesBulk(timeCaptor.capture())).thenReturn(quakes);
        Mockito.when(prefs.getLastUpdateDate()).thenReturn(MOCK_TIME);
        final ICallback<List<Quake>> callback = Mockito.mock(ICallback.class);
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        repo.checkNewEarthquakes(callback);

        Mockito.verify(network, Mockito.times(1)).checkNewEarthquakes(timeCaptor.capture(),
                captor.capture());
        Assert.assertEquals(MOCK_TIME, timeCaptor.getValue());
        captor.getValue().onResult(new Result<>(quakes));
        Mockito.verify(prefs, Mockito.times(2)).getLastUpdateDate();
        Mockito.verify(prefs, Mockito.times(1)).saveLastUpdateDate(timeCaptor.capture());
        List<Long> allValues = timeCaptor.getAllValues();
        Assert.assertThat(allValues.get(1), new DefMatcherImp<Long>("Time not matches! ") {

            @Override
            protected boolean match(Long time) {
                final long now = System.currentTimeMillis();
                return time <= now && time > now - 30;
            }
        });
        Mockito.verify(db, Mockito.times(1)).saveQuakes(quakes);

        verifyNoInteraction();
    }


    //  ------------------------------------------------------------------------------------------------
    @Test
    public void getAllQuakes() {
        final SearchParams params = new SearchParams("", "", "", "", "", "", "");

        repo.searchQuakes(params);

        verifyNoInteraction();
    }


    //  ------------------------------------------------------------------------------------------------
    @Test
    public void save() {
        final List<Quake> quakes = new ArrayList<>();
        quakes.add(new Quake());

        repo.save(quakes);

        Mockito.verify(db, Mockito.times(1)).saveQuakes(quakes);
        verifyNoInteraction();
    }
}
