package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;


/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class HelpContentActivity extends UI {

   private static final String EXTRA_WEB_CONTENT = "extra_web_content";
   private static final String EXTRA_WEB_TITLE = "extra_web_title";

   String title = "";
   String data = "";

   private WebView webView;

    public static void start(Context context, String webData, String title) {
        Intent intent = new Intent();
        intent.setClass(context, HelpContentActivity.class);
        intent.putExtra(EXTRA_WEB_CONTENT, webData);
        intent.putExtra(EXTRA_WEB_TITLE, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_detail);

        title = getIntent().getStringExtra(EXTRA_WEB_TITLE);
        data = getIntent().getStringExtra(EXTRA_WEB_CONTENT);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = title;
        setToolBar(R.id.toolbar, options);


        webView = findView(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webView.loadDataWithBaseURL(null, getNewContent(escapeHTML(data)), "text/html", "utf-8", null);
//        webView.loadUrl(ActivityBean.getActurl());
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmltext) {
        try {
            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                // elements.removeAttr("style");
                element.attr("width", "95%").attr("height", "auto").attr("style", "text-align:center;margin:0 auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }

    private static String escapeHTML(String data){
        return data.replace("&nbsp;"," ")
                .replace("&lt;","<")
                .replace("&gt;",">")
                .replace("&amp;","&")
                .replace("&quot;","\"")
                .replace("\\\"","\"");
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
