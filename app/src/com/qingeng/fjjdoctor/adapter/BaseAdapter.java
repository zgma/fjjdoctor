package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    public Context mContext;
    public List<T> mList;
    public LayoutInflater mInflater;

    public BaseAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return onCreateVH(viewGroup,viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindVH(holder,position);
    }

    protected abstract VH onCreateVH(ViewGroup viewGroup, int viewType);

    protected abstract void onBindVH(VH holder, int position);

    public void refreshData(List<T> data){
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public void loadMoreData(List<T> data){
        mList.addAll(data);
        notifyDataSetChanged();
    }
}
