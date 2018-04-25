package com.mdgd.commons.support_v7.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mdgd.commons.support_v7.R;
import com.mdgd.commons.support_v7.mvp.CommonActivity;
import com.mdgd.commons.support_v7.mvp.ViewContract;

import java.util.List;

/**
 * Created by Dan
 * on 25/07/2017.
 */

public abstract class HostActivity<T extends ViewContract.IPresenter> extends CommonActivity<T> implements  FragmentContract.IHost {

    protected View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        if(presenter == null) {
            presenter = createPresenter();
        }
        container = findViewById(getFragmentContainerId());
        if(savedInstanceState == null) {
            setFragment(getFirstFragment());
        }
        else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if(fragments == null || fragments.isEmpty()){
                setFragment(getFirstFragment());
            }
            else {
                restoreFragments(fragments);
            }
        }
    }

    protected abstract void restoreFragments(List<Fragment> fragments);

    protected abstract T createPresenter();

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }


    protected abstract Fragment getFirstFragment();
}
