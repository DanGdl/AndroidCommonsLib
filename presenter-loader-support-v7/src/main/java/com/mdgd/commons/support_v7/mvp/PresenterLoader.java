package com.mdgd.commons.support_v7.mvp;

import android.content.Context;
import android.support.v4.content.Loader;

import com.mdgd.commons.contract.mvp.PresenterFactory;
import com.mdgd.commons.contract.mvp.ViewContract;

/**
 * Created by Dan
 * on 26/04/2018.
 */

public class PresenterLoader<T extends ViewContract.IPresenter> extends Loader<T> {

    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
        }
        else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroy();
        presenter = null;
    }

    public T getPresenter() {
        return presenter;
    }
}
