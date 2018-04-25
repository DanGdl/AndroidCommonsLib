package com.mdgd.commons.support_v7.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdgd.commons.contarct.fragment.FragmentContract;

/**
 * Created by Dan
 * on 19/07/2017.
 */

public abstract class HostedFragment<T extends FragmentContract.IHost> extends Fragment
        implements FragmentContract.IFragment {
    protected T host;
    private boolean hasProgress = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    @SuppressWarnings("unchecked")
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        host = null;
    }

    @Override
    public void showProgress() {}

    @Override
    public void hideProgress() {}
}
