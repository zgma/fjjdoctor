package com.qingeng.fjjdoctor.zoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.PublishAdapter;
import com.qingeng.fjjdoctor.adapter.PublishImageAdapter;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class ZoomMainActivity extends UI implements HttpInterface, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, PublishAdapter.Listener, PublishImageAdapter.Listener {


    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView rcl_list;
    private TextView load_more_text;

    private EditText input;
    private ImageView clear_input;


    List<PublishBean> publishBeans = new ArrayList<>();
    PublishAdapter publishAdapter;
    int currPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, ZoomMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);

    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, ZoomMainActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zoom_main);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "招商";
        setToolBar(R.id.toolbar, options);

        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText("发布");
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPublishActivity.start(ZoomMainActivity.this);
            }
        });


        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        rcl_list = findViewById(R.id.rcl_list);
        load_more_text = findViewById(R.id.load_more_text);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        rcl_list.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator)rcl_list.getItemAnimator()).setSupportsChangeAnimations(false);
        rcl_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (UiUtils.isSlideToBottom(recyclerView) && publishBeans.size() >= MainConstant.PAGE_NUMBER) {
                    onLoadMoreData();
                }
            }
        });
        publishAdapter = new PublishAdapter(this);
        publishAdapter.setPublishBeans(publishBeans);
        publishAdapter.setListener(this);
        publishAdapter.setImageItemListener(this);
        rcl_list.setAdapter(publishAdapter);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary);
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setRefreshing(true);


        clear_input = findView(R.id.clear_input);
        input = findView(R.id.input);
        input.addTextChangedListener(watcherInput);
        clear_input.setVisibility(View.GONE);
        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                onRefresh();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        //点击搜索的时候隐藏软键盘
                        hideKeyboard(input);
                        // 在这里写搜索的操作,一般都是网络请求数据
                        onRefresh();
                        return true;
                    }
                    // 在这里写搜索的操作,一般都是网络请求数据
                    return true;
                }
                return false;
            }
        });
        this.onRefresh();

    }

    @Override
    public void onRefresh() {
        currPage = 1;
        getListData();
    }


    private void onLoadMoreData() {
        currPage++;
        getListData();
        showload_more_text();
    }

    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("page", currPage);
        baseRequestBean.addParams("limit", MainConstant.PAGE_NUMBER);
        baseRequestBean.addParams("sidx", "create_date");
        baseRequestBean.addParams("order", "desc");
        String name = input.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) baseRequestBean.addParams("name", name);
        HttpClient.getNewsList(baseRequestBean, this, RequestCommandCode.GET_NEWS_LIST);
    }


    public void showload_more_text() {
        load_more_text.setVisibility(View.VISIBLE);
    }

    public void hideload_more_text() {
        load_more_text.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_NEWS_LIST:
                List<PublishBean> beans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), PublishBean.class);
                setData(beans);
                break;
            case 1234:
                ToastHelper.showToast(this, "点赞成功");
                currBankCardBean.setLikeCount(currBankCardBean.getLikeCount() + 1);
//                publishAdapter.notifyDataSetChanged();
                publishAdapter.notifyItemRangeChanged(currPosition,currPosition==0?1:currPosition);
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        swipe_refresh_layout.setRefreshing(false);
        hideload_more_text();
    }

    public void setData(List<PublishBean> beans) {
        if (currPage == 1) {
            publishBeans.clear();
        }
        if (beans.size() > 0) {
            publishBeans.addAll(beans);
            publishAdapter.setPublishBeans(publishBeans);
            publishAdapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                publishBeans.clear();
                publishBeans.addAll(beans);
                publishAdapter.setPublishBeans(publishBeans);
                publishAdapter.notifyDataSetChanged();
            }
            if (currPage > 1) {
//                ToastHelper.showToast(this, "没有更多数据！");
                currPage--;
            }
        }
    }


    private TextWatcher watcherInput = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                clear_input.setVisibility(View.VISIBLE);
            } else {
                clear_input.setVisibility(View.GONE);
            }
            input.setSelection(input.getText().length());//将光标移至文字末尾
        }
    };


    @Override
    public void onItemClick(PublishBean publishBean) {
        if (!TextUtils.isEmpty(publishBean.getUrl())) {
            HtmlContentActivity.start(this, publishBean.getUrl());
        }
    }

    private PublishBean currBankCardBean;
    private int currPosition;

    @Override
    public void onLikeClick(int position,PublishBean publishBean) {
        currBankCardBean = publishBean;
        currPosition = position;
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("type", "1");
        baseRequestBean.addParams("id", publishBean.getId());
        HttpClient.likeActive(baseRequestBean, this, 1234);
    }

    @Override
    public void onImageItemClick(int i, ArrayList<ImageBean> imageBeans, ImageBean imageBean, PublishBean publishBean) {
    /*    int index = imageBeans.indexOf(imageBean);
        PhotoShowActivity.start(this, imageBeans, index);*/
/*        if (!TextUtils.isEmpty(publishBean.getUrl())){
            HtmlContentActivity.start(this, publishBean.getUrl());
        }else {
            ToastHelper.showToast(this, "没有详情链接");
        }*/


    }


    @Override
    public void imageDelete(ImageBean imageBean) {
    }
}
