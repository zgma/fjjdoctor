package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.ApplyBean;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder> {

    private Context context;

    private ApplyAdapter.ApplyListener listener;

    public void setListener(ApplyAdapter.ApplyListener listener) {
        this.listener = listener;
    }

    public ApplyAdapter(Context context) {
        this.context = context;
    }

    private List<ApplyBean> applyBeanList;

    public void setApplyBeanList(List<ApplyBean> applyBeanList) {
        this.applyBeanList = applyBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_system_notification_view_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ApplyBean applyBean = applyBeanList.get(i);
        viewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongPressed(applyBean);
                }

                return true;
            }
        });
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(applyBean);
                }
            }
        });
        viewHolder.agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReplySending(viewHolder);
                listener.onAgree(applyBean);
            }
        });
        viewHolder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReplySending(viewHolder);
                listener.onReject(applyBean);
            }
        });

        viewHolder.headImageView.loadImgForUrl(applyBean.getHeadImage());
        viewHolder.fromAccountText.setText(applyBean.getUsername());
        viewHolder.contentText.setText(applyBean.getMsg());
        viewHolder.timeText.setText(TimeUtil.getTimeShowString(TimeUtil.getDateFromFormatString(applyBean.getCreateDate(),TimeUtil.DATAFORMATSTRING_yyyyMMddHHmmss), false));
        viewHolder.top_line.setVisibility(i==0?View.GONE:View.GONE);

        if (applyBean.getType() == 3) {
            viewHolder.agreeButton.setVisibility(View.GONE);
            viewHolder.rejectButton.setVisibility(View.GONE);
            viewHolder.operatorResultText.setVisibility(View.VISIBLE);
            viewHolder.operatorResultText.setText("已同意");
        } else {
            viewHolder.agreeButton.setVisibility(View.VISIBLE);
            viewHolder.rejectButton.setVisibility(View.VISIBLE);
            viewHolder.operatorResultText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return applyBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ApplyBean applyBean;
        private HeadImageView headImageView;
        private TextView fromAccountText;
        private TextView timeText;
        private TextView contentText;
        private View operatorLayout;
        private Button agreeButton;
        private Button rejectButton;
        private TextView operatorResultText;
        private LinearLayout root;
        private View top_line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headImageView = (HeadImageView) itemView.findViewById(R.id.from_account_head_image);
            fromAccountText = (TextView) itemView.findViewById(R.id.from_account_text);
            contentText = (TextView) itemView.findViewById(R.id.content_text);
            timeText = (TextView) itemView.findViewById(R.id.notification_time);
            operatorLayout = itemView.findViewById(R.id.operator_layout);
            agreeButton = (Button) itemView.findViewById(R.id.agree);
            rejectButton = (Button) itemView.findViewById(R.id.reject);
            operatorResultText = (TextView) itemView.findViewById(R.id.operator_result);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            top_line =  itemView.findViewById(R.id.top_line);
        }
    }

    /**
     * 等待服务器返回状态设置
     */
    private void setReplySending(ViewHolder viewHolder) {
        viewHolder.agreeButton.setVisibility(View.GONE);
        viewHolder.rejectButton.setVisibility(View.GONE);
        viewHolder.operatorResultText.setVisibility(View.VISIBLE);
        viewHolder.operatorResultText.setText(R.string.team_apply_sending);
    }


    public interface ApplyListener {
        void onAgree(ApplyBean message);

        void onReject(ApplyBean message);

        void onLongPressed(ApplyBean message);

        void onClick(ApplyBean message);
    }
}
