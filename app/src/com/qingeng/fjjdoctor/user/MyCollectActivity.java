package com.qingeng.fjjdoctor.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.CollectBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.CollectAdapter;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.activity.WatchVideoActivity2;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyCollectActivity extends UI implements CollectAdapter.Listener, HttpInterface {

    private static final String TAG = "SystemMessageActivity2";
    private static final String ISSENDCOLLECT = "ISSENDCOLLECT";

    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果


    // view
    // view
    private RecyclerView recyclerView;

    private View emptyBg;
    private boolean msgLoaded = false;
    // adapter
    private CollectAdapter adapter;
    private List<CollectBean> items = new ArrayList<>();
    private boolean isSendCollect = false;


    public static void start(Context context) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(ISSENDCOLLECT,false);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(ISSENDCOLLECT,true);
        intent.setClass(context, MyCollectActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apply_list);
        isSendCollect = getIntent().getBooleanExtra(ISSENDCOLLECT,false);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = isSendCollect?"选择收藏":"我的收藏";
        setToolBar(R.id.toolbar, options);
        initAdapter();
        loadMessages(); // load old data
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


    private void initAdapter() {
        adapter = new CollectAdapter(this);
        adapter.setListener(this);
        adapter.setCollectBeans(items);
        adapter.setSendCollect(isSendCollect);


        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        emptyBg = findView(R.id.emptyBg);
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        HttpClient.myFavorite(this, RequestCommandCode.MY_FAVORITE);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.MY_FAVORITE:
                items.clear();
                items = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), CollectBean.class);
                refresh();
                break;
            case RequestCommandCode.REMOVE_FA:
                ToastHelper.showToast(this, "删除成功");
                loadMessages(); // load old data
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {

    }

    private void refresh() {
        adapter.setCollectBeans(items);
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(CollectBean collectBean) {
//        SessionHelper.startTeamSession(this,groupBean.getTid());
//        if (collectBean.getTypeDesc().equals("语音")) {
//            startAudio(collectBean.getContent());
//        }
        if (isSendCollect){
            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA, collectBean);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }else {
            if (collectBean.getTypeDesc().equals("视频")){
                WatchVideoActivity2.start(this, collectBean.getContent());
            }
        }
    }

    @Override
    public void onDelete(CollectBean collectBean) {
        HttpClient.removeFavorite(collectBean.getId() + "", this, RequestCommandCode.REMOVE_FA);
    }

    public void onSelected(ArrayList<String> selects, ArrayList<String> selectedNames) {
//        Intent intent = new Intent();
//        intent.putStringArrayListExtra(RESULT_DATA, selects);
//        intent.putStringArrayListExtra(Extras.RESULT_NAME, selectedNames);
//        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }




//    private void play() {
//        if (animationView.getBackground() instanceof AnimationDrawable) {
//            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
//            animation.start();
//        }
//    }
//
//    private void stop() {
//        if (animationView.getBackground() instanceof AnimationDrawable) {
//            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
//            animation.stop();
//
//            endPlayAnim();
//        }
//    }


}
