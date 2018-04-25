package com.mdgd.commons.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.mdgd.commons.R;
import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.mvp.CommonActivity;

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
        setContentView(getLayoutResId());
        if(presenter == null) {
            presenter = createPresenter();
        }
        container = findViewById(getFragmentContainerId());
        if(savedInstanceState == null) {
            setFragment(getFirstFragment());
        }
        else {
            List<Fragment> fragments = getFragmentManager().getFragments();
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
