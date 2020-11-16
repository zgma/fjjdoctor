package com.qingeng.fjjdoctor.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.DoctorInfoBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.enums.EditEnum;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.setting.EditUserInfoActivity;
import com.qingeng.fjjdoctor.setting.SettingMain2Activity;
import com.qingeng.fjjdoctor.user.appointment.MyAppointmentDateActivity;
import com.qingeng.fjjdoctor.user.appointment.MyAppointmentRecordActivity;
import com.qingeng.fjjdoctor.user.consult.MyConsultRecordActivity;
import com.qingeng.fjjdoctor.user.wallet.WithdrawalActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MineFragment extends TFragment implements View.OnClickListener, LSettingItem.OnLSettingItemClick {

    private static final String TAG = "fragment_MineFragment";


    @BindView(R.id.user_photo)
    HeadImageView user_photo;
    @BindView(R.id.mine_user_name)
    TextView mine_user_name;
    @BindView(R.id.mine_user_name2)
    TextView mine_user_name2;
    @BindView(R.id.mine_user_name3)
    TextView mine_user_name3;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;

    @BindView(R.id.item_1)
    LSettingItem item_1;
    @BindView(R.id.item_2)
    LSettingItem item_2;
    @BindView(R.id.item_3)
    LSettingItem item_3;
    @BindView(R.id.item_4)
    LSettingItem item_4;
    @BindView(R.id.item_5)
    LSettingItem item_5;


    public static MineFragment getInstance() {
        MineFragment sf = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iv_setting.setOnClickListener(this);
        // 加载本地数据
        item_1.setmOnLSettingItemClick(this);
        item_2.setmOnLSettingItemClick(this);
        item_3.setmOnLSettingItemClick(this);
        item_4.setmOnLSettingItemClick(this);
        item_5.setmOnLSettingItemClick(this);
        showUserData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_my_code:
                MyCodeActivity.start(getActivity(), AppPreferences.getAccId());
                break;
            case R.id.iv_setting:
                SettingMain2Activity.start(getContext());
                break;

        }
    }

    private void showUserData() {
        showUserDataToUI();
        HttpClient.getDoctorInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                MainActivity.saveUserInfoToPreferences();
                showUserDataToUI();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(getContext(), "更新用户信息失败 " + message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.LOGIN_USER_INFO);

    }

    private void showUserDataToUI() {
        UserInfoBean userBean = LocalDataUtils.getUserInfo();
        if (userBean != null) {
            user_photo.loadImgForUrl(userBean.getAvatar());
        }
        DoctorInfoBean doctorInfoBean = LocalDataUtils.getDoctorInfo();
        if (doctorInfoBean != null) {
            mine_user_name.setText(doctorInfoBean.getRealName());
            mine_user_name2.setText(doctorInfoBean.getWorkAddress());
            mine_user_name3.setText(doctorInfoBean.getJobTitle());
        }

    }

    @Override
    public void click(int id, boolean isChecked) {
        switch (id) {
            case R.id.item_1:
                MyAppointmentRecordActivity.start(getContext());
                break;
            case R.id.item_2:
                MyConsultRecordActivity.start(getContext());
                break;
            case R.id.item_3:
                MyCode2Activity.start(getContext());
                break;
            case R.id.item_5:
                WithdrawalActivity.start(getContext());
                break;
        }
    }
}
