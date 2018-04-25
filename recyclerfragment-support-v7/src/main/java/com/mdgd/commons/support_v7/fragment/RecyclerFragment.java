package com.mdgd.commons.support_v7.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.recycler.CommonRecyclerAdapter;
import com.mdgd.commons.support_v7.R;

/**
 * Created by Dan
 * on 02/01/2018.
 */

public abstract class RecyclerFragment<HOST extends FragmentContract.IHost, ITEM> extends HostedFragment<HOST>
        implements CommonRecyclerAdapter.IOnItemClickListener<ITEM> {

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
