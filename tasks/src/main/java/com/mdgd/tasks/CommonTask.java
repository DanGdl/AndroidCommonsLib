package com.mdgd.tasks;

import com.google.android.gms.gcm.GcmTaskService;
import com.mdgd.commons.contract.services.ServiceContract;

/**
 * Created by Max
 * on 13/06/2018.
 */
@Deprecated
public abstract class CommonTask<T extends ServiceContract.IPresenter> extends GcmTaskService implements ServiceContract.IService {

    protected final T presenter;

    public CommonTask() {
        this.presenter = getPresenter();
    }

    protected abstract T getPresenter();
}
