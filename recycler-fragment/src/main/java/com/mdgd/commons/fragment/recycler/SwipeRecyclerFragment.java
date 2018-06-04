package com.mdgd.commons.fragment.recycler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.mdgd.commons.R;
import com.mdgd.commons.contract.fragment.FragmentContract;

/**
 * Created by Dan
 * on 02/01/2018.
 */

public abstract class SwipeRecyclerFragment<X extends FragmentContract.IPresenter, Y extends FragmentContract.IHost, ITEM>
        extends RecyclerFragment<X, Y, ITEM> implements SwipeRefreshLayout.OnRefreshListener {

    protected SwipeRefreshLayout swipe;

    public SwipeRecyclerFragment(){
        super();
        setHasProgress(true);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_swipe_recycler;
    }

    @Override
    protected void initViews(View v) {
        super.initViews(v);
        swipe = v.findViewById(R.id.fragment_swipe);
        swipe.setOnRefreshListener(this);
    }

    @Override
    public void showProgress() {
        if(swipe != null) {
            swipe.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if(swipe != null) {
            swipe.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh(){}
}
