package com.mdgd.commons.support.v7.fragment;

import com.mdgd.commons.contract.fragment.FragmentContract;

/**
 * Created by Max
 * on 08/10/2017.
 */

public abstract class FragmentPresenter<T extends FragmentContract.IView> implements FragmentContract.IPresenter {
    protected final T view;

    public FragmentPresenter(T view) {
        this.view = view;
    }
}
