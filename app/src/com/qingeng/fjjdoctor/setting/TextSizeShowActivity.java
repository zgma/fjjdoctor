package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.DisplayUtils;
import com.qingeng.fjjdoctor.widget.fontsliderbar.FontSliderBar;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;


/**
 * Created by zsj on 2017/9/11.
 * 字体设置展示
 */

public class TextSizeShowActivity extends UI {

    private Toolbar toolbar2;


    private FontSliderBar fontSliderBar;
    private TextView tvContent1;
    private TextView tvContent2;
    private TextView tvContent3;

    private float textsize1, textsize2, textsize3;
    private float textSizef;//缩放比例
    private int currentIndex;
    private boolean isClickable = true;


    public static void start(Context context) {
        Intent intent = new Intent(context, TextSizeShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textsizeshow);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "字体大小";
        setToolBar2(R.id.toolbar, options);
        initUI();
    }


    private void initUI() {
        tvContent1 = findView(R.id.tv_chatcontent);
        tvContent2 = findView(R.id.tv_chatcontent1);
        tvContent3 = findView(R.id.tv_chatcontent3);
        fontSliderBar = findView(R.id.fontSliderBar);

        currentIndex = AppPreferences.getTextSizeSetting();
        textSizef = 1 + currentIndex * 0.1f;
        textsize1 = tvContent1.getTextSize() / textSizef;
        textsize2 = tvContent2.getTextSize() / textSizef;
        textsize3 = tvContent3.getTextSize() / textSizef;
        fontSliderBar.setTickCount(6).setTickHeight(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 15)).setBarColor(Color.GRAY)
                .setTextColor(Color.BLACK).setTextPadding(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setTextSize(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 14))
                .setThumbRadius(DisplayUtils.convertDip2Px(TextSizeShowActivity.this, 10)).setThumbColorNormal(Color.GRAY).setThumbColorPressed(Color.GRAY)
                .setOnSliderBarChangeListener(new FontSliderBar.OnSliderBarChangeListener() {
                    @Override
                    public void onIndexChanged(FontSliderBar rangeBar, int index) {
                        if (index > 5) {
                            return;
                        }
                        index = index - 1;
                        float textSizef = 1 + index * 0.1f;
                        setTextSize(textSizef);
                    }
                }).setThumbIndex(currentIndex).withAnimation(false).applay();
    }


    private void setTextSize(float textSize) {
        //改变当前页面的字体大小
        tvContent1.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize1 * textSize));
        tvContent2.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize2 * textSize));
        tvContent3.setTextSize(DisplayUtils.px2sp(TextSizeShowActivity.this, textsize3 * textSize));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentIndex != fontSliderBar.getCurrentIndex()) {
                if (isClickable) {
                    isClickable = false;
                    refresh();
                }
            } else {
//                MainActivity.start(this);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void refresh() {
        //存储标尺的下标
        int a = fontSliderBar.getCurrentIndex();
        AppPreferences.setTextSizeSetting(fontSliderBar.getCurrentIndex());
        ToastHelper.showToast(this, a+"");
        //通知主页面重启
/*        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));
        //重启mainActivity
        RxBus.getInstance().post(MainActivity.class.getSimpleName(), new MessageSocket(99, null, null, null));*/
//        showMyDialog();
        //2s后关闭  延迟执行任务 重启完主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                hideMyDialog();
                finish();
            }
        }, 2000);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void setToolBar2(int toolBarId, ToolBarOptions options) {
        toolbar2 = findViewById(toolBarId);
        if (options.titleId != 0) {
            toolbar2.setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            toolbar2.setTitle(options.titleString);
        }
        if (options.logoId != 0) {
            // toolbar.setLogo(options.logoId);
        }
        setSupportActionBar(toolbar2);

        if (options.isNeedNavigate) {
            toolbar2.setNavigationIcon(options.navigateId);
            toolbar2.setContentInsetStartWithNavigation(0);
            toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fontSliderBar.getCurrentIndex() != currentIndex) {
                        if (isClickable) {
                            isClickable = false;
                            refresh();
                        }
                    } else {
//                        MainActivity.start(TextSizeShowActivity.this);
                        finish();
                    }
                }
            });
        }
    }


}
