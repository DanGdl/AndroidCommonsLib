package com.mdgd.commons.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dan
 * on 01/01/2018.
 */

public abstract class CommonViewHolder<T> extends RecyclerView.ViewHolder {

    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindItem(T item, int position);
}