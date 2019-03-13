package com.mdgd.commons.ui.main.fr.quackes;

import com.mdgd.commons.R;
import com.mdgd.commons.components.repo.IRepo;
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

/**
 * Created by Owner
 * on 05/02/2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class QuackesFragmentPresenterTestJava {
    private final String errorMsg = "Mock!";
    @Mock
    private IRepo repo;
    @Mock
    private QuakesFragmentContract.IView view;

    private QuakesFragmentPresenter presenter;

    @Before
    public void setUp() {
        presenter = new QuakesFragmentPresenter(view, repo);
    }

    private void verifyNoInteraction() {
        Mockito.verifyNoMoreInteractions(repo);
        Mockito.verifyNoMoreInteractions(view);
    }

    private void verifyError(ArgumentCaptor<ICallback<List<Quake>>> captor) {
        captor.getValue().onResult(new Result<>(new Exception(errorMsg)));
        Mockito.verify(view, Mockito.times(1)).hideProgress();
        Mockito.verify(view, Mockito.times(1)).showToast(R.string.shit, errorMsg);
        verifyNoInteraction();
    }

    private void verifySuccess(List<Quake> quakes, ArgumentCaptor<ICallback<List<Quake>>> captor, ArgumentCaptor<List<Quake>> listCaptor) {
        captor.getValue().onResult(new Result<>(quakes));
        Mockito.verify(view, Mockito.times(1)).hideProgress();
        Mockito.verify(view, Mockito.times(1)).updateEarthQuakes(listCaptor.capture());
        Assert.assertEquals(quakes, listCaptor.getValue());
        verifyNoInteraction();
    }

    @Test
    public void getEarthQuakes() {
        final SearchParams params = new SearchParams("", "", "", "", "", "", "");
        final List<Quake> quakes = new ArrayList<>();
        quakes.add(new Quake());
        quakes.add(new Quake());
        final ArgumentCaptor<SearchParams> paramCaptor = ArgumentCaptor.forClass(SearchParams.class);
        Mockito.when(repo.searchQuakes(paramCaptor.capture())).thenReturn(quakes);
        final ArgumentCaptor<List<Quake>> captor = ArgumentCaptor.forClass(List.class);

        presenter.searchQuakes(params);

        Mockito.verify(repo, Mockito.times(1)).searchQuakes(params);
        Mockito.verify(view, Mockito.times(1)).updateEarthQuakes(captor.capture());
        Assert.assertEquals(quakes, captor.getValue());
        Assert.assertEquals(params, paramCaptor.getValue());
        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthQuakes_fail() {
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);

        presenter.checkNewEarthQuakes();

        Mockito.verify(view, Mockito.times(1)).showProgress(R.string.empty, R.string.wait_please);
        Mockito.verify(repo, Mockito.times(1)).checkNewEarthquakes(captor.capture());
        verifyError(captor);
        verifyNoInteraction();
    }

    @Test
    public void checkNewEarthQuakes_success() {
        final List<Quake> quakes = new ArrayList<>();
        quakes.add(new Quake());
        quakes.add(new Quake());
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);
        final ArgumentCaptor<List<Quake>> listCaptor = ArgumentCaptor.forClass(List.class);

        presenter.checkNewEarthQuakes();

        Mockito.verify(view, Mockito.times(1)).showProgress(R.string.empty, R.string.wait_please);
        Mockito.verify(repo, Mockito.times(1)).checkNewEarthquakes(captor.capture());
        verifySuccess(quakes, captor, listCaptor);
    }

    @Test
    public void getNextBulk_do_nothing() {
        presenter.getNextBulk(-1);

        verifyNoInteraction();
    }

    @Test
    public void getNextBulk_fail() {
        final long lastDate = System.currentTimeMillis() - 5 * 3600_000;
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);

        presenter.getNextBulk(lastDate);

        Mockito.verify(view, Mockito.times(1)).showProgress(R.string.empty, R.string.wait_please);
        Mockito.verify(repo, Mockito.times(1)).getEarthquakes(endCaptor.capture(), captor.capture());
        Assert.assertEquals(lastDate, endCaptor.getValue().getTime());
        verifyError(captor);
    }

    @Test
    public void getNextBulk_success() {
        final List<Quake> quakes = new ArrayList<>();
        quakes.add(new Quake());
        quakes.add(new Quake());
        final long lastDate = System.currentTimeMillis() - 5 * 3600_000;
        final ArgumentCaptor<ICallback<List<Quake>>> captor = ArgumentCaptor.forClass(ICallback.class);
        final ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);
        final ArgumentCaptor<List<Quake>> listCaptor = ArgumentCaptor.forClass(List.class);

        presenter.getNextBulk(lastDate);

        Mockito.verify(view, Mockito.times(1)).showProgress(R.string.empty, R.string.wait_please);
        Mockito.verify(repo, Mockito.times(1)).getEarthquakes(endCaptor.capture(), captor.capture());
        Assert.assertEquals(lastDate, endCaptor.getValue().getTime());
        verifySuccess(quakes, captor, listCaptor);
    }
}
