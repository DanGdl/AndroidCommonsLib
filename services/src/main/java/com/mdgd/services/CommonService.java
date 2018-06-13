package com.mdgd.services;

import android.app.Service;

import com.mdgd.commons.contract.services.ServiceContract;

/**
 * Created by Max
 * on 13/06/2018.
 */
public abstract class CommonService<T extends ServiceContract.IPresenter> extends Service implements ServiceContract.IService {
    protected final T presenter;

    public CommonService() {
        this.presenter = getPresenter();
    }

    protected abstract T getPresenter();
}
