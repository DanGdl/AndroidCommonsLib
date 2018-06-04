package com.mdgd.commons.support.v7.fragment.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.recycler.CommonRecyclerAdapter;
import com.mdgd.commons.resources.R;
import com.mdgd.commons.support.v7.fragment.HostedFragment;

/**
 * Created by Dan
 * on 02/01/2018.
 */

public abstract class RecyclerFragment<X extends FragmentContract.IPresenter, Y extends FragmentContract.IHost, ITEM>
        extends HostedFragment<X, Y> implements CommonRecyclerAdapter.IOnItemClickListener<ITEM> {

    protected CommonRecyclerAdapter<ITEM> adapter;
    protected RecyclerView recycler;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initViews(View v) {
        recycler = v.findViewById(R.id.fragment_recycler);
        adapter = getAdapter();
        recycler.setAdapter(adapter);
    }

    protected abstract CommonRecyclerAdapter<ITEM> getAdapter();
}
