package com.mdgd.commons.support.v7.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.resources.R;
import com.mdgd.commons.support.v7.mvp.CommonActivity;

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

        container = findViewById(getFragmentContainerId());
        if(savedInstanceState == null) {
            addFragment(getFirstFragment());
        }
        else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if(fragments == null || fragments.isEmpty()){
                addFragment(getFirstFragment());
            }
            else {
                restoreFragments(fragments);
            }
        }
    }

    protected void restoreFragments(List<Fragment> fragments){}

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    protected abstract Fragment getFirstFragment();
}
