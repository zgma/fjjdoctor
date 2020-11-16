package com.qingeng.fjjdoctor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qingeng.fjjdoctor.R;


/**
 * 作者： chengcl
 * 时间： 2016/6/21 0021.
 * 邮箱：469476881@qq.com
 * 描述：
 */
public class TopBar extends RelativeLayout {
    private Context context;

    private RelativeLayout rlBarBg;
    private TextView title;
    private ImageView leftButton;
    private TextView leftText;
    private ImageView rightButton;
    private TextView rightText;

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public TopBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init() {
/*        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_action_bar, this);
        view.setTag("layout/view_action_bar_0");
        mDataBinding = DataBindingUtil.bind(view);*/

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_action_bar, this);

        rlBarBg = view.findViewById(R.id.rl_bar_bg);
        title = view.findViewById(R.id.title);
        leftButton = view.findViewById(R.id.left_button);
        leftText = view.findViewById(R.id.left_txt);
        rightButton = view.findViewById(R.id.right_button);
        rightText = view.findViewById(R.id.right_txt);

    }


    /**
     * 设置topBar的背景颜色
     *
     * @param colorid
     */
    public void setBackGround(int colorid) {
        rlBarBg.setBackgroundResource(colorid);
    }

    /**
     * 直接传入title
     *
     * @param name
     */
    public void setTitle(String name) {
        title.setText(name);
    }

    /**
     * 传入source id String.xml 中对应的
     *
     * @param resouce
     */
    public void setTitle(int resouce) {
        title.setText(context.getResources().getString(resouce));
    }


    /**
     * 设置左边按钮显示图片和点击事件
     *
     * @param resourceid 图片id
     * @param listener   点击事件
     */
    public void setLeftButtonListener(@NonNull int resourceid, @NonNull View.OnClickListener listener) {
        leftButton.setImageResource(resourceid);
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setOnClickListener(listener);
    }

    /**
     * 设置左边按钮无图片
     */
    public void setLeftButtonNoPic() {
        leftButton.setVisibility(View.GONE);
    }

    /**
     * 设置右边按钮显示图片和点击事件
     *
     * @param resourceid 图片id
     * @param listener   点击事件
     */
    public void setRightButtonListener(@NonNull int resourceid, @NonNull View.OnClickListener listener) {
        rightButton.setImageResource(resourceid);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(listener);
    }


    /**
     * 设置右边按钮消失
     */
    public void setRightButtonGone() {
        rightButton.setVisibility(View.GONE);
    }

    /**
     * 设置右边文字按钮消失
     */
    public void setRightTextGone() {
        rightText.setVisibility(View.GONE);
    }
    /**
     * 设置右边文字按钮消失
     */
    public void setRightTextVisible() {
        rightText.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边按钮显示文本和点击事件
     *
     * @param txt
     * @param listener 点击事件
     */
    public void setRightTxtListener(@NonNull String txt, @NonNull View.OnClickListener listener) {
        rightText.setText(txt);
        rightText.setVisibility(View.VISIBLE);
        rightText.setOnClickListener(listener);
    }

    /**
     * 设置左边按钮显示文本和点击事件
     *
     * @param txt
     * @param listener 点击事件
     */
    public void setLeftTxtListener(@NonNull String txt, @NonNull View.OnClickListener listener) {
        leftText.setText(txt);
        leftText.setVisibility(View.VISIBLE);
        leftText.setOnClickListener(listener);
    }
}
