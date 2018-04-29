package com.mdgd.commons.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by Dan
 * on 01/01/2018.
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonViewHolder<T>> {
    protected final IOnItemClickListener<T> listener;
    protected final Context context;
    protected List<T> items;
    protected final LayoutInflater inflater;

    public CommonRecyclerAdapter(Context context, IOnItemClickListener<T> listener){
        this.listener = listener;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder<T> holder, int position) {
        if(items != null && position < items.size()) {
            holder.bindItem(items.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if(this.items == null){
            setItems(items);
        }
        else{
            int start = this.items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(start, items.size());
        }
    }

    public interface IOnItemClickListener<T> {
        void onItemClicked(T item, int position);
    }
}
