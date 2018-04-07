package com.mdgd.commons.support_v7.mvp;

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
