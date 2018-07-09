package com.mdgd.commons.support.v7.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    @CallSuper
    public void showProgress() {
        if(!hasProgress() && host != null){
            host.showProgress();
        }
    }

    @Override
    @CallSuper
    public void hideProgress() {
        if(!hasProgress() && host != null){
            host.hideProgress();
        }
    }

    @Override
    public void showToast(int msgRes) {
        Context ctx = getActivity();
        if(ctx != null) {
            Toast.makeText(ctx, msgRes, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showToast(int msgRes, String query) {
        Context ctx = getActivity();
        if(ctx != null) {
            Toast.makeText(ctx, getString(msgRes, query), Toast.LENGTH_LONG).show();
        }
    }


    @TargetApi(16)
    protected boolean requestPermissionsIfNeed(int requestCode, String... permissions) {
        boolean result = true;
        Activity ctx = getActivity();
        if(ctx == null){
            return false;
        }
        for(String p : permissions){
            if(ctx.checkPermission(p, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED){
                result = false;
                askPermissions(ctx, requestCode, permissions);
            }
        }
        return result;
    }

    private void askPermissions(Activity ctx, int requestCode, String[] permissions) {
        ActivityCompat.requestPermissions(ctx, permissions, requestCode);
    }

    protected boolean areAllPermissionsGranted(int[] grantResults) {
        boolean result = false;
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
