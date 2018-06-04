package com.mdgd.commons.support.v7.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdgd.commons.contract.fragment.FragmentContract;

/**
 * Created by Dan
 * on 19/07/2017.
 */

public abstract class HostedFragment<X extends FragmentContract.IPresenter, Y extends FragmentContract.IHost> extends Fragment
        implements FragmentContract.IFragment, FragmentContract.IView {
    private boolean hasProgress = false;
    protected final X presenter;
    protected Y host;

    public HostedFragment(){
        presenter = getPresenter();
    }

    protected abstract X getPresenter();

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (Y)context;
        presenter.setHost(host);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResId(), container, false);
        initViews(v);
        return v;
    }

    protected abstract int getLayoutResId();

    protected abstract void initViews(View v);

    @Override
    public boolean hasProgress() {
        return hasProgress;
    }

    public void setHasProgress(boolean hasProgress) {
        this.hasProgress = hasProgress;
    }

    @Override
    public void showProgress() {}

    @Override
    public void hideProgress() {}
}
