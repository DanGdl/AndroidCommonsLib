package com.mdgd.commons.support_v7.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.mdgd.commons.support_v7.R;
import com.mdgd.commons.support_v7.mvp.CommonActivity;
import com.mdgd.commons.support_v7.mvp.ViewContract;

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
        container = findViewById(R.id.fragmentContainer);
        if(savedInstanceState == null) {
            setFragment(getFirstFragment());
        }
    }

    protected abstract T createPresenter();

    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    protected void setFragment(Fragment fragment) {
        setFragment(fragment, false, null);
    }

    protected void setFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    protected void setFragmentToBackStack(Fragment fragment) {
        setFragment(fragment, true, null);
    }

    protected abstract Fragment getFirstFragment();
}
