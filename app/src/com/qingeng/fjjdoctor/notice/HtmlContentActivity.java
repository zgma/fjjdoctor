package com.qingeng.fjjdoctor.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.HtmlUtils;


public class HtmlContentActivity extends UI {

    private static final String EXTRA_WEB_CONTENT = "extra_web_content";
    private static final String EXTRA_WEB_TITLE = "extra_web_title";
    WebView webView;

    String title = "";
    String data = "";

    public static void start(Context context, String webData, String title) {
        Intent intent = new Intent(context, HtmlContentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_WEB_CONTENT, webData);
        intent.putExtra(EXTRA_WEB_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ToolBarOptions options = new NimToolBarOptions();

        title = getIntent().getStringExtra(EXTRA_WEB_TITLE);
        data = getIntent().getStringExtra(EXTRA_WEB_CONTENT);

        options.titleString = title;
        setToolBar(R.id.toolbar, options);
        initUI();
    }


    private void initUI() {
        webView = findViewById(R.id.webView);

        title = getIntent().getStringExtra(EXTRA_WEB_TITLE);
        data = getIntent().getStringExtra(EXTRA_WEB_CONTENT);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webView.loadDataWithBaseURL(null, HtmlUtils.getNewContent(data), "text/html", "utf-8", null);
//        webView.loadUrl(ActivityBean.getActurl());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

