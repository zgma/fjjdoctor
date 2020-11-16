package com.qingeng.fjjdoctor.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qingeng.fjjdoctor.R;

public class ImageFrameUtils {

    public static void showImageToView(ImageView imageView,String Url){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_delete_pressed);

        Glide.with(imageView.getContext()).load(Url).apply(requestOptions).into(imageView);
    }


    public static void showHead(ImageView imageView,String Url){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_delete_pressed);

        Glide.with(imageView.getContext()).load(Url).apply(requestOptions).into(imageView);
    }

    public static void showImageToView_Round(ImageView imageView,String Url){

        RequestOptions myOptions = new RequestOptions()
                         .transform(new GlideRoundTransform(imageView.getContext(),4));
        Glide.with(imageView.getContext()).load(Url).apply(myOptions).into(imageView);


    }

    public static void showImageToView(ImageView imageView,int id){
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_delete_pressed);
        Glide.with(imageView.getContext()).load(id).apply(requestOptions).into(imageView);
    }

}
