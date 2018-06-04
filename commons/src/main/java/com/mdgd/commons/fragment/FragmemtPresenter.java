package com.mdgd.commons.fragment;

import com.mdgd.commons.contract.fragment.FragmentContract;

import java.lang.ref.WeakReference;

/**
 * Created by Dan
 * on 08/10/2017.
 */

public abstract class FragmemtPresenter<T extends FragmentContract.IView, X extends FragmentContract.IHost> implements FragmentContract.IPresenter {
    protected final T view;
    protected WeakReference<X> hostRef;

    public FragmemtPresenter(T view){
        this.view = view;
    }
}
