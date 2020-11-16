package com.qingeng.fjjdoctor.zoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;

public class PhotoShowActivity extends UI {


    private final static String IMAGE_URL_LIST = "IMAGE_URL_LIST";
    private final static String IMAGE_SHOW_INDEX = "IMAGE_SHOW_INDEX";

    private ArrayList<ImageBean> imageUrls = new ArrayList<>();
    private int index = 0;


    public static void start(Context context, ArrayList<ImageBean> imageBeans, int index) {
        Intent intent = new Intent(context, PhotoShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IMAGE_URL_LIST,imageBeans);
        intent.putExtra(IMAGE_SHOW_INDEX,index);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_photo);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString ="PhotoShowActivity";
        setToolBar(R.id.toolbar, options);

        imageUrls = (ArrayList<ImageBean>) getIntent().getExtras().get(IMAGE_URL_LIST);
        index = getIntent().getIntExtra(IMAGE_SHOW_INDEX,0);
        System.out.println(JSON.toJSONString(imageUrls));
        System.out.println(JSON.toJSONString(index));

    }




}
