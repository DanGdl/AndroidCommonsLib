package com.mdgd.commons.support.v7.fragment;

import com.mdgd.commons.contract.fragment.FragmentContract;

/**
 * Created by Dan
 * on 08/10/2017.
 */

public abstract class FragmemtPresenter<T extends FragmentContract.IView> implements FragmentContract.IPresenter {
    protected final T view;

    public FragmemtPresenter(T view){
        this.view = view;
    }
}
