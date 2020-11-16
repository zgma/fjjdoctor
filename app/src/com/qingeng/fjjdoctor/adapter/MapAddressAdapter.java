package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.AddressBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class MapAddressAdapter extends RecyclerView.Adapter<MapAddressAdapter.ViewHolder> {

    private Context context;

    private MapAddressAdapter.Listener listener;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    List<AddressBean> addressBeans = new ArrayList<>();

    public void setAddressBeans(List<AddressBean> addressBeans) {
        this.addressBeans = addressBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_map_address, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AddressBean addressBean = addressBeans.get(i);
        viewHolder.tv_name.setText(addressBean.getName());
        viewHolder.tv_address.setText(addressBean.getAddress());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(addressBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    public interface Listener {
        void onItemClick(AddressBean addressBean);
    }



}
