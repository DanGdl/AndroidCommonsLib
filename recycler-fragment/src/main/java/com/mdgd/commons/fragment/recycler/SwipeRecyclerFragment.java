package com.mdgd.commons.fragment.recycler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.mdgd.commons.R;
import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.contract.progress.IProgressView;

/**
 * Created by Max
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
    protected IProgressView createProgressView(String title, String message) {
        return new IProgressView() {
            @Override
            public void show() {
                swipe.setRefreshing(true);
            }

            @Override
            public boolean isShowing() {
                return swipe.isRefreshing();
            }

            @Override
            public void dismiss() {
                swipe.setRefreshing(false);
            }

            @Override
            public void cancel() {
                swipe.setRefreshing(false);
            }
        };
    }

    @Override
    public void onRefresh(){}
}
