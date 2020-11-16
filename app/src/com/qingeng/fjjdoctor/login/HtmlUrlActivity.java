package com.qingeng.fjjdoctor.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.widget.TopBar;

import butterknife.BindView;


/**
 */
public class HtmlUrlActivity extends UI {
    @BindView(R.id.top_bar)
    TopBar top_bar;
    @BindView(R.id.webView)
    WebView webView;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HtmlUrlActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail2);

        top_bar.setTitle("用户协议与隐私政策");
        top_bar.setLeftButtonListener(R.drawable.actionbar_dark_back_icon, v->finish());

        //支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webView.loadUrl("http://wap.feijiejieguanjia.com/protocol?protocolPage=01");

    }

}
