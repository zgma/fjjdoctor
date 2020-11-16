package com.qingeng.fjjdoctor.find;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.activity.ContactSearchActivity;
import com.qingeng.fjjdoctor.main.activity.GroupSearchActivity;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.user.MyExtensionActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.zoom.PartnerMainActivity;
import com.qingeng.fjjdoctor.zoom.ZoomMainActivity;


public class FindFragment extends TFragment implements View.OnClickListener ,HttpInterface{

    private static final String TAG = "fragment_MineFragment";

    private ConstraintLayout layout_zoom;
    private ConstraintLayout layout_2;
    private ConstraintLayout layout_3;

    private ConstraintLayout layout_4;
    private ConstraintLayout layout_5;

    private ConstraintLayout layout_6;
    private ConstraintLayout layout_7;


    public static FindFragment getInstance() {
        FindFragment sf = new FindFragment();
        return sf;
    }

    private boolean show = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show = true;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && show) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println(TAG + "：onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();
        showUserData();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 界面初始化
        layout_zoom = findView(R.id.layout_zoom);
        layout_2 = findView(R.id.layout_2);
        layout_3 = findView(R.id.layout_3);
        layout_4 = findView(R.id.layout_4);
        layout_5 = findView(R.id.layout_5);
        layout_6 = findView(R.id.layout_6);
        layout_7 = findView(R.id.layout_7);

        layout_zoom.setOnClickListener(this);
        layout_2.setOnClickListener(this);
        layout_3.setOnClickListener(this);
        layout_4.setOnClickListener(this);
        layout_5.setOnClickListener(this);
        layout_6.setOnClickListener(this);
        layout_7.setOnClickListener(this);
        // 注册观察者

        // 加载本地数据
        showUserData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_zoom:
                ZoomMainActivity.start(getActivity());
                break;
            case R.id.layout_2:
                PartnerMainActivity.start(getActivity());
                break;
            case R.id.layout_3:
                MyExtensionActivity.start(getActivity());
                break;
            case R.id.layout_4:
                ContactSearchActivity.start(getActivity());
                break;
            case R.id.layout_5:
                GroupSearchActivity.start(getActivity());
                break;
            case R.id.layout_6:
                getDefUser(1);
                break;
            case R.id.layout_7:
                getDefUser(2);

                break;
        }
    }

    private void showUserData() {
        HttpClient.getLoginUserInfo(this, RequestCommandCode.LOGIN_USER_INFO);
    }

    private void addDefFriend(String title,String content,int userId){
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(getActivity(), title    ,
                content, true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(getActivity(), "", true);
                        HttpClient.addFriends(userId + "", "加我吧", FindFragment.this, RequestCommandCode.ADD_FRIENDS);
                    }
                });
        dialog.show();
    }

    private void getDefUser(int type){
        String title = type==1?"广告专线":"APP专线";
        String content = type==1?"是否要添加“广告专线”为好友？":"是否要添加“APP专线”为好友？";
        HttpClient.getSystemUser(type, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                UserBean userBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), UserBean.class);
                addDefFriend(title, content, userBean.getUser().getUserId());
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(getActivity(), message);
            }

            @Override
            public void onComplete() {

            }
        }, 1);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.ADD_FRIENDS:
                ToastHelper.showToast(getActivity(), "请求已发送");
                break;
            case RequestCommandCode.LOGIN_USER_INFO:
/*                LocalDataUtils.saveLocalUser(baseResponseData.getData());
                MainActivity.saveUserInfoToPreferences();*/
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(getActivity(), message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }
}
