package com.mdgd.commons.support.v7.mvp;

import com.mdgd.commons.contract.mvp.ViewContract;

/**
 * Created by Dan
 * on 08/10/2017.
 */

public abstract class Presenter<T extends ViewContract.IView> implements ViewContract.IPresenter {
    protected final T view;

    public Presenter(T view){
        this.view = view;
    }
}
