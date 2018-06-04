package com.mdgd.commons.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.mvp.CommonActivity;
import com.mdgd.commons.resources.R;

import java.util.List;

/**
 * Created by Dan
 * on 25/07/2017.
 */

public abstract class HostActivity<T extends ViewContract.IPresenter> extends CommonActivity<T> implements FragmentContract.IHost {

    protected View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container = findViewById(getFragmentContainerId());
        if(savedInstanceState == null) {
            addFragment(getFirstFragment());
        }
        else {
            List<Fragment> fragments = getFragmentManager().getFragments();
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
