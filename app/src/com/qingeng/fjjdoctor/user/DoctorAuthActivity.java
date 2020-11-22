package com.qingeng.fjjdoctor.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.media.imagepicker.Constants;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.model.GLImage;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.UserProfileSettingActivity;
import com.qingeng.fjjdoctor.util.AssetsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DoctorAuthActivity extends UI implements View.OnClickListener, HttpInterface {
    private final String TAG = UserProfileSettingActivity.class.getSimpleName();

    private static final int IMAGE_IDENTITY_CARD = 100;
    private static final int IMAGE_EMPLOYEE_CARD = 101;
    private static final int IMAGE_LICENSE = 102;

    @BindView(R.id.edit_realName)
    EditText edit_realName;
    @BindView(R.id.edit_workInfo)
    EditText edit_workInfo;
    @BindView(R.id.edit_sectionType)
    EditText edit_sectionType;
    @BindView(R.id.edit_jobTitle)
    EditText edit_jobTitle;
    @BindView(R.id.edit_adeptDesc)
    EditText edit_adeptDesc;
    @BindView(R.id.edit_askPrice)
    EditText edit_askPrice;
    @BindView(R.id.edit_riwPrice)
    EditText edit_riwPrice;

    @BindView(R.id.tv_workAddress)
    TextView tv_workAddress;
    @BindView(R.id.radio_male)
    RadioButton radio_male;
    @BindView(R.id.radio_female)
    RadioButton radio_female;

    @BindView(R.id.iv_identityCard)
    ImageView iv_identityCard;
    @BindView(R.id.iv_employeeCard)
    ImageView iv_employeeCard;
    @BindView(R.id.iv_license)
    ImageView iv_license;

    @BindView(R.id.btn_next)
    Button btn_next;

    private String fileUrlIdentityCard;
    private String fileUrlEmployeeCard;
    private String fileUrlLicense;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DoctorAuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_auth);
        ButterKnife.bind(this);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "医生认证";
        setToolBar(R.id.toolbar, options);

        findViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initJsonData();
    }

    private void findViews() {
        edit_askPrice.addTextChangedListener(textWatcher);
        edit_riwPrice.addTextChangedListener(textWatcher);

        tv_workAddress.setOnClickListener(this);
        iv_identityCard.setOnClickListener(this);
        iv_employeeCard.setOnClickListener(this);
        iv_license.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    private String realName;
    private String workAddress;
    private String workInfo;
    private String sectionType;
    private String jobTitle;
    private String adeptDesc;
    private String askPrice;
    private String riwPrice;

    private String workAddrArea;
    private String workAddrCity;
    private String workAddrProvince;
    private int gender;

    private boolean verify() {
        boolean verifyPass = true;

        realName = edit_realName.getText().toString().trim();
        workAddress = tv_workAddress.getText().toString().trim();
        workInfo = edit_workInfo.getText().toString().trim();
        sectionType = edit_sectionType.getText().toString().trim();
        jobTitle = edit_jobTitle.getText().toString().trim();
        adeptDesc = edit_adeptDesc.getText().toString().trim();
        askPrice = edit_askPrice.getText().toString().trim();
        riwPrice = edit_riwPrice.getText().toString().trim();

        if (TextUtils.isEmpty(realName)) {
            verifyPass = false;
        }
        if (TextUtils.isEmpty(workAddress)) {
            verifyPass = false;
        }
        if (TextUtils.isEmpty(workInfo)) {
            verifyPass = false;
        }
        if (TextUtils.isEmpty(sectionType)) {
            verifyPass = false;
        }
        if (TextUtils.isEmpty(jobTitle)) {
            verifyPass = false;
        }
        if (TextUtils.isEmpty(adeptDesc)) {
            verifyPass = false;
        }

        if (TextUtils.isEmpty(askPrice)) {
            verifyPass = false;
        }

        if (TextUtils.isEmpty(riwPrice)) {
            verifyPass = false;
        }
        try {
            Double money_askPrice = Double.parseDouble(askPrice);
            Double money_riwPrice = Double.parseDouble(riwPrice);
        } catch (Exception e) {
            verifyPass = false;
        }


        if (!radio_male.isChecked() && !radio_female.isChecked()) {
            verifyPass = false;
        }

        if (imgEmployeeCard == null || imgLicense == null) {
            verifyPass = false;
        }
        gender = radio_male.isChecked() ? 1 : 2;

        if (!verifyPass) {
            ToastHelper.showToast(this, "请完善信息");
        }

        return verifyPass;
    }

    private List<AreaBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    String nativePlace = "";
    int nativePlacePosition1 = 0;
    int nativePlacePosition2 = 0;
    int nativePlacePosition3 = 0;

    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = AssetsUtil.read(this, "json/province.json");
        List<AreaBean> jsonBean = JSON.parseArray(JsonData, AreaBean.class);
        ;//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCity().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCity().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getCity().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
    }

    private void showLocalPickerView() {// 弹出选择器
        try {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String opt1tx = options1Items.size() > 0 ?
                            options1Items.get(options1).getPickerViewText() : "";

                    String opt2tx = options2Items.size() > 0
                            && options2Items.get(options1).size() > 0 ?
                            options2Items.get(options1).get(options2) : "";

                    String opt3tx = options2Items.size() > 0
                            && options3Items.get(options1).size() > 0
                            && options3Items.get(options1).get(options2).size() > 0 ?
                            options3Items.get(options1).get(options2).get(options3) : "";

                    nativePlacePosition1 = options1;
                    nativePlacePosition2 = options2;
                    nativePlacePosition3 = options3;
                    workAddrArea = opt3tx;
                    workAddrCity = opt2tx;
                    workAddrProvince = opt1tx;
                    nativePlace = workAddrArea + "," + workAddrCity + "," + workAddrProvince;
                    tv_workAddress.setText(nativePlace);
                }
            })

                    .setTitleText("工作地区选择")
                    .setDividerColor(Color.BLACK)
                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                    .setContentTextSize(20)
                    .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
            pvOptions.setSelectOptions(nativePlacePosition1, nativePlacePosition2, nativePlacePosition3);
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            pvOptions.show();
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showToast(this, "籍贯数据加载异常，请稍后重试");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (verify()) {
                    uploadFile(IMAGE_IDENTITY_CARD);
                }
                break;
            case R.id.tv_workAddress:
                showLocalPickerView();
                break;
            case R.id.iv_identityCard:
                pickPic(IMAGE_IDENTITY_CARD);
                break;
            case R.id.iv_employeeCard:
                pickPic(IMAGE_EMPLOYEE_CARD);
                break;
            case R.id.iv_license:
                pickPic(IMAGE_LICENSE);
                break;
        }
    }

    private void pickPic(int Type) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(1);
        ImagePickerLauncher.selectImage(this, Type, option);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_IDENTITY_CARD:
                onPicked(data, IMAGE_IDENTITY_CARD);
                break;
            case IMAGE_EMPLOYEE_CARD:
                onPicked(data, IMAGE_EMPLOYEE_CARD);
                break;
            case IMAGE_LICENSE:
                onPicked(data, IMAGE_LICENSE);
                break;
        }
    }

    private File imgIdentityCard;
    private File imgEmployeeCard;
    private File imgLicense;

    private void onPicked(Intent data, int type) {
        if (data == null) {
            return;
        }
        ArrayList<GLImage> images = (ArrayList<GLImage>) data.getSerializableExtra(Constants.EXTRA_RESULT_ITEMS);
        if (images == null || images.isEmpty()) {
            return;
        }
        GLImage image = images.get(0);
        if (type == IMAGE_IDENTITY_CARD) {
            imgIdentityCard = new File(image.getPath());
            Glide.with(this).asBitmap().load(Uri.fromFile(new File(imgIdentityCard.getAbsolutePath())))
                    .into(iv_identityCard);
        }
        if (type == IMAGE_EMPLOYEE_CARD) {
            imgEmployeeCard = new File(image.getPath());
            Glide.with(this).asBitmap().load(Uri.fromFile(new File(imgEmployeeCard.getAbsolutePath())))
                    .into(iv_employeeCard);
        }
        if (type == IMAGE_LICENSE) {
            imgLicense = new File(image.getPath());
            Glide.with(this).asBitmap().load(Uri.fromFile(new File(imgLicense.getAbsolutePath())))
                    .into(iv_license);
        }

    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case IMAGE_IDENTITY_CARD:
                fileUrlIdentityCard = baseResponseData.getUrl();
                uploadFile(IMAGE_EMPLOYEE_CARD);
                break;
            case IMAGE_EMPLOYEE_CARD:
                fileUrlEmployeeCard = baseResponseData.getUrl();
                uploadFile(IMAGE_LICENSE);
                break;
            case IMAGE_LICENSE:
                fileUrlLicense = baseResponseData.getUrl();
                sendData();
                break;
            case RequestCommandCode.ADD_SUGGEST:
                ToastHelper.showToast(this, baseResponseData.getMsg());
                finish();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, "失败 " + message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editStr = editable.toString().trim();
            int posDot = editStr.indexOf(".");
            //不允许输入3位小数,超过三位就删掉
            if (posDot < 0) {
                return;
            }
            if (editStr.length() - posDot - 1 > 2) {
                editable.delete(posDot + 3, posDot + 4);
            }

        }
    };

    private void sendData() {
        DialogMaker.showProgressDialog(this, "提交中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("realName", realName);
        baseRequestBean.addParams("adeptDesc", adeptDesc);
        baseRequestBean.addParams("jobTitle", jobTitle);
        baseRequestBean.addParams("gender", gender);

        baseRequestBean.addParams("workAddrInfo", workInfo);
        baseRequestBean.addParams("workAddrArea", workAddrArea);
        baseRequestBean.addParams("workAddrCity", workAddrCity);
        baseRequestBean.addParams("workAddrProvince", workAddrProvince);

        Double money_askPrice = Double.parseDouble(askPrice);
        Double money_riwPrice = Double.parseDouble(riwPrice);

        baseRequestBean.addParams("askPrice", money_askPrice);
        baseRequestBean.addParams("riwPrice", money_riwPrice);

        baseRequestBean.addParams("identityCard", fileUrlIdentityCard);
        baseRequestBean.addParams("employeeCard", fileUrlEmployeeCard);
        baseRequestBean.addParams("license", fileUrlLicense);
        HttpClient.doctorSubmitAuth(baseRequestBean, this, RequestCommandCode.ADD_SUGGEST);

    }

    private void uploadFile(int type) {
        DialogMaker.showProgressDialog(this, "上传照片中...");

        if (type == IMAGE_IDENTITY_CARD && imgIdentityCard != null) {
            HttpClient.uploadFile(imgIdentityCard, this, IMAGE_IDENTITY_CARD);
        }
        if (type == IMAGE_EMPLOYEE_CARD && imgEmployeeCard != null) {
            HttpClient.uploadFile(imgEmployeeCard, this, IMAGE_EMPLOYEE_CARD);
        }
        if (type == IMAGE_LICENSE && imgLicense != null) {
            HttpClient.uploadFile(imgLicense, this, IMAGE_LICENSE);
        }
    }


}

