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

import com.bumptech.glide.Glide;
import com.qingeng.apilibrary.bean.ThemeBean;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private Context context;

    private ThemeAdapter.Listener listener;

    private int mySetId = 1;

    public void setListener(ThemeAdapter.Listener listener) {
        this.listener = listener;
    }

    public ThemeAdapter(Context context) {
        this.context = context;
    }

    private List<ThemeBean> themeBeanList = new ArrayList<>();

    public void setThemeBeanList(List<ThemeBean> themeBeanList) {
        this.themeBeanList = themeBeanList;
    }

    public void setMySetId(int mySetId) {
        this.mySetId = mySetId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_theme,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ThemeBean themeBean = themeBeanList.get(i);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.img.getLayoutParams();
        layoutParams.width = getImageMaxEdge();
        layoutParams.height = (int)(getImageMaxEdge()*1.5);
        viewHolder.img.setLayoutParams(layoutParams);


        if (mySetId==themeBean.getId()){
            viewHolder.set.setBackgroundResource(R.drawable.bg_theme_set_selected);
            viewHolder.set.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.set.setText("已设置");
        }else {
            viewHolder.set.setBackgroundResource(R.drawable.bg_theme_set);
            viewHolder.set.setTextColor(context.getResources().getColor(R.color.user_menu_memo));
            viewHolder.set.setText("设置");
        }
        viewHolder.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(themeBean);
                }
            }
        });

        Glide.with(context).load(themeBean.getImage()).into(viewHolder.img);
        viewHolder.name.setText(themeBean.getName());
    }

    @Override
    public int getItemCount() {
        return themeBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView img;
        private TextView set;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            img = (ImageView) itemView.findViewById(R.id.img);
            set = (TextView) itemView.findViewById(R.id.set);
        }
    }


    public interface Listener {
        void onClick(ThemeBean themeBean);
    }

    public static int getImageMaxEdge() {
        int a = (int) (0.33 * ScreenUtil.screenWidth);
        return a;
    }
}
