package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.BankCardBean;
import com.qingeng.fjjdoctor.R;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class BankCardAdapter extends RecyclerView.Adapter<BankCardAdapter.ViewHolder> {

    private Context context;

    private BankCardAdapter.Listener listener;


    public void setListener(BankCardAdapter.Listener listener) {
        this.listener = listener;
    }

    public BankCardAdapter(Context context) {
        this.context = context;
    }

    private List<BankCardBean> bankCardBeans;

    public void setBankCardBeans(List<BankCardBean> bankCardBeans) {
        this.bankCardBeans = bankCardBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bank_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BankCardBean bankCardBean = bankCardBeans.get(i);

        if (i == getItemCount() - 1) {
            viewHolder.add_card.setVisibility(View.VISIBLE);
            viewHolder.root.setVisibility(View.GONE);
        } else {
            viewHolder.add_card.setVisibility(View.GONE);
            viewHolder.root.setVisibility(View.VISIBLE);
           /* if ((i + 1) % 3 == 0) {
                viewHolder.root.setBackgroundResource(R.drawable.bg_bank_card_item_blue);
            } else if ((i + 1) % 2 == 0) {
                viewHolder.root.setBackgroundResource(R.drawable.bg_bank_card_item_orange);
            } else {
                viewHolder.root.setBackgroundResource(R.drawable.bg_bank_card_item_red);
            }*/
            viewHolder.tv_bank_name.setText(bankCardBean.getOpenBank());
            viewHolder.tv_card_number.setText(bankCardBean.getCardNumber());
        }


        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(bankCardBean);
            }
        });
        viewHolder.add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.addCard();
            }
        });

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.deleteCard(bankCardBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bankCardBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_card_number;
        private TextView tv_bank_name;
        private ImageView iv_icon;
        private ImageView iv_delete;
        private LinearLayout root;
        private LinearLayout add_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_card_number = (TextView) itemView.findViewById(R.id.tv_card_number);
            tv_bank_name = (TextView) itemView.findViewById(R.id.tv_bank_name);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            add_card = (LinearLayout) itemView.findViewById(R.id.add_card);
        }
    }

    public interface Listener {
        void onItemClick(BankCardBean bankCardBean);

        void addCard();
        void deleteCard(BankCardBean bankCardBean);
    }


}
