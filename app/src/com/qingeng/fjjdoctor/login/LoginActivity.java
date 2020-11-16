package com.qingeng.fjjdoctor.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.AuthTask;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.EventBusMessage;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.config.preference.UserPreferences;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.RegularUtils;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.widget.TopBar;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录/注册界面
 * <p/>
 */
public class LoginActivity extends UI implements View.OnClickListener, HttpInterface {

    private static final String KICK_OUT = "KICK_OUT";


    @BindView(R.id.top_bar)
    TopBar top_bar;


    @BindView(R.id.tv_login_by_password)
    TextView tv_login_by_password;
    @BindView(R.id.tv_login_by_code)
    TextView tv_login_by_code;

    @BindView(R.id.login_layout_password)
    LinearLayout login_layout_password;
    @BindView(R.id.login_layout_code)
    LinearLayout login_layout_code;

    @BindView(R.id.edit_login_account)
    EditText edit_login_account;
    @BindView(R.id.edit_login_password)
    EditText edit_login_password;

    @BindView(R.id.edit_login_account_code)
    EditText edit_login_account_code;
    @BindView(R.id.edit_login_code)
    EditText edit_login_code;
    @BindView(R.id.tv_login_code_send)
    TextView tv_login_code_send;

    @BindView(R.id.tv_go_register)
    TextView tv_go_register;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.tv_go_code_login)
    TextView tv_go_code_login;
    @BindView(R.id.tv_go_find_password)
    TextView tv_go_find_password;

    @BindView(R.id.iv_login_show_password)
    ImageView iv_login_show_password;

    @BindView(R.id.iv_qq_login)
    ImageView iv_qq_login;
    @BindView(R.id.iv_wx_login)
    ImageView iv_wx_login;
    @BindView(R.id.iv_ali_login)
    ImageView iv_ali_login;

    @BindView(R.id.layout_agreement)
    LinearLayout layout_agreement;
    @BindView(R.id.cb_agreement)
    CheckBox cb_agreement;

    private boolean modeByCode = false;

    private Tencent mTencent;

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);

        //透明状态栏
/*        StatusBarCompat.translucentStatusBar(this);
        StatusBarUtil.setStatusBarDark(this, true);*/
        ButterKnife.bind(this);

        requestBasicPermission();
        onParseIntent();
        top_bar.setTitle("登录");
        top_bar.setLeftButtonListener(R.drawable.actionbar_dark_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_go_register.setOnClickListener(this);
        iv_login_show_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_go_code_login.setOnClickListener(this);
        tv_go_find_password.setOnClickListener(this);
        iv_qq_login.setOnClickListener(this);
        iv_wx_login.setOnClickListener(this);
        iv_ali_login.setOnClickListener(this);
        layout_agreement.setOnClickListener(this);

        tv_login_by_password.setOnClickListener(this);
        tv_login_by_code.setOnClickListener(this);
        tv_login_code_send.setOnClickListener(this);

        if (!TextUtils.isEmpty(AppPreferences.getLoginPhone())) {
            edit_login_account.setText(AppPreferences.getLoginPhone());
            edit_login_account_code.setText(AppPreferences.getLoginPhone());
        }
        cb_agreement.setChecked(true);

        switchMode();
    }


    private void switchMode() {
        if (modeByCode) {
            tv_login_by_code.setTextColor(UiUtils.getColor(this, R.color.grey));
            tv_login_by_password.setTextColor(UiUtils.getColor(this, R.color.main_bottom_line));
            login_layout_code.setVisibility(View.VISIBLE);
            login_layout_password.setVisibility(View.GONE);
        } else {
            tv_login_by_code.setTextColor(UiUtils.getColor(this, R.color.main_bottom_line));
            tv_login_by_password.setTextColor(UiUtils.getColor(this, R.color.grey));
            login_layout_code.setVisibility(View.GONE);
            login_layout_password.setVisibility(View.VISIBLE);
        }
    }


    private void requestBasicPermission() {
    }


    private void onParseIntent() {
        if (!getIntent().getBooleanExtra(KICK_OUT, false)) {
            return;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_register:
                RegisterActivity.start(this);
                break;
            case R.id.btn_login:
                goLogin();
                break;
            case R.id.tv_go_code_login:
                //LoginByCodeActivity.start(this);
                finish();
                break;
            case R.id.tv_go_find_password:
                ResetPasswordActivity.start(this, "忘记密码");
                break;
            case R.id.iv_login_show_password:
                if (edit_login_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    edit_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_login_show_password.setBackgroundResource(R.mipmap.password_show);
                } else {
                    edit_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_login_show_password.setBackgroundResource(R.mipmap.password_hide);
                }
                break;
            case R.id.iv_qq_login:
                //QQ登录
                if (null == mTencent) {
                    mTencent = Tencent.createInstance("101858761", this.getApplicationContext());
                    if (!mTencent.isSessionValid()) {
                        mTencent.login(this, "all", loginListener);
                    }
                }
                break;
            case R.id.iv_wx_login:
                //wx登录
                wxLogin();
                break;
            case R.id.iv_ali_login:
                DialogMaker.showProgressDialog(this, "获取授权...");
                HttpClient.aliAppAuth(this, RequestCommandCode.ALI_PAY_APP_AUTH);
                break;
            case R.id.layout_agreement:
                HtmlUrlActivity.start(this);
                break;
            case R.id.tv_login_by_password:
                modeByCode = false;
                switchMode();
                break;
            case R.id.tv_login_by_code:
                modeByCode = true;
                switchMode();
                break;
            case R.id.tv_login_code_send:
                sendCode();
                break;
        }
    }

    private void goLogin() {
        if (!verifyInput()) return;
        DialogMaker.showProgressDialog(this, "登录中...");

        if (modeByCode) {
            BaseRequestBean baseRequestBean = new BaseRequestBean();
            baseRequestBean.addParams("code", edit_login_code.getText().toString().trim());
            baseRequestBean.addParams("phone", edit_login_account_code.getText().toString().trim());

            AppPreferences.saveLoginPhone(edit_login_account_code.getText().toString().trim());
            HttpClient.loginBySmsCode(baseRequestBean, this, RequestCommandCode.LOGIN_CODE);
        } else {
            BaseRequestBean baseRequestBean = new BaseRequestBean();
            baseRequestBean.addParams("password", edit_login_password.getText().toString().trim());
            baseRequestBean.addParams("phone", edit_login_account.getText().toString().trim());

            AppPreferences.saveLoginPhone(edit_login_account.getText().toString().trim());

            HttpClient.loginByPhone(baseRequestBean, this, RequestCommandCode.LOGIN_PHONE);
        }

    }


    /**
     * 验证输入信息
     *
     * @return
     */
    private boolean verifyInput() {
        if (modeByCode) {
            if (!RegularUtils.isMobileNO(edit_login_account_code.getText().toString().trim())) {
                ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
                return false;
            }

            if (TextUtils.isEmpty(edit_login_code.getText().toString().trim())) {
                ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_code));
                return false;
            }
        } else {
            if (!RegularUtils.isMobileNO(edit_login_account.getText().toString().trim())) {
                ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
                return false;
            }

            if (TextUtils.isEmpty(edit_login_password.getText().toString().trim())) {
                ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_password));
                return false;
            }
        }

        if (!cb_agreement.isChecked()) {
            ToastHelper.showToast(this, "请同意用户协议");
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.LOGIN_PHONE:
            case RequestCommandCode.QQ_APP_LOGIN:
            case RequestCommandCode.WX_APP_LOGIN:
            case RequestCommandCode.ALI_PAY_LOGIN:
            case RequestCommandCode.LOGIN_CODE:
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()));
                if (jsonObject != null && jsonObject.containsKey("token")) {
                    AppPreferences.saveHttpToken(jsonObject.getString("token"));
                    ToastHelper.showToast(this, "登录成功");
                    getUserInfo();
                } else {
                    ToastHelper.showToast(this, "获取token失败");
                }
                break;
            case RequestCommandCode.GET_USER_INFO:
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                UserInfoBean userBean = LocalDataUtils.getUserInfo();

                DemoCache.setAccount(userBean.getAccid());
                saveLoginInfo(userBean.getAccid(), userBean.getToken());
                // 初始化消息提醒配置
                initNotificationConfig();
                doLogin(userBean);
                break;
            case RequestCommandCode.ALI_PAY_APP_AUTH:
                aliAuthV2((String) baseResponseData.getData());
                break;
        }
    }


    @Override
    public void onFailure(int requestCode, String message) {
        if (requestCode == RequestCommandCode.GET_USER_INFO) {
            ToastHelper.showToast(this, "获取医生信息失败");
            return;
        }
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


    private void saveLoginInfo(final String account, final String token) {
        AppPreferences.saveAccId(account);
        AppPreferences.saveImToken(token);
    }

    public void doLogin(UserInfoBean userBean) {
        LoginInfo info = new LoginInfo(userBean.getAccid(), userBean.getToken(), readAppKey(LoginActivity.this));
        // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        System.out.println("doLogin---------" + loginInfo.getAccount());
                    }

                    @Override
                    public void onFailed(int i) {
                        System.out.println("doLogin---------" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        System.out.println("doLogin---------" + throwable.getMessage());
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);

        // 进入主界面
        MainActivity.start(LoginActivity.this, null);
        finish();
    }


    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    String accessToken;
    String expires_in;
    String openId;
    //授权登录监听（最下面是返回结果）
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            try {
                openId = ((org.json.JSONObject) o).getString("openid"); //QQ的openid
                accessToken = ((org.json.JSONObject) o).getString("access_token");
                expires_in = ((org.json.JSONObject) o).getString("expires_in");
                //在这里直接可以处理登录
                DialogMaker.showProgressDialog(LoginActivity.this, "登录中...");
                HttpClient.qqAppLogin(accessToken, openId, LoginActivity.this, RequestCommandCode.QQ_APP_LOGIN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                mTencent.handleResultData(data, loginListener);
            }
        }
    }


    IWXAPI msgApi = null;

    public void wxLogin() {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(this, MainConstant.WX_APP_ID, true);
            msgApi.registerApp(MainConstant.WX_APP_ID);// 将该app注册到微信
        }
        if (!msgApi.isWXAppInstalled()) {
            Toast.makeText(this, "手机中没有安装微信客户端!", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        msgApi.sendReq(req);
    }

    private void goLoginByWx(String code) {
        DialogMaker.showProgressDialog(this, "登录中...");
        HttpClient.wxAppLogin(code, this, RequestCommandCode.WX_APP_LOGIN);
    }


    @Subscribe
    public void onSidOut(EventBusMessage messageBean) {
        if (messageBean.getCode().equals(MainConstant.LOGIN_SUCCESS_WX)) {
            String wxCode = (String) messageBean.getData();
            System.out.println(wxCode);
            goLoginByWx(wxCode);
        }
    }


    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2021001144603284";
    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2021001144603284";
    /**
     * 支付宝账户登录授权业务：入参target_id值 可自定义，保证唯一性即可
     */
    public static final String TARGET_ID = "4564615615616516516516";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "填你自己的应用私钥，要和配置应用时的应用公钥对应";

    public static final String RSA_PRIVATE = "";

    private static final int SDK_AUTH_FLAG = 2;


    /**
     * 支付宝账户授权业务
     */
    public void aliAuthV2(String authInfo) {

        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(LoginActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG: {
                    // @SuppressWarnings("unchecked")
                    Map<String, String> resultMap = (Map<String, String>) msg.obj;
                    String resultStatus = resultMap.get("resultStatus");
                    String result = resultMap.get("result");
                    String memo = resultMap.get("memo");
                    DialogMaker.showProgressDialog(LoginActivity.this, "登录中...");
                    HttpClient.aliAppLogin(result, LoginActivity.this, RequestCommandCode.ALI_PAY_LOGIN);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void getUserInfo() {
        if (!TextUtils.isEmpty(AppPreferences.getHttpToken())) {
            HttpClient.getDoctorInfo(this, RequestCommandCode.GET_USER_INFO);
        }
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }


    private boolean sendCDing = false;

    public void sendCode() {
        if (sendCDing) return;
        if (!checkPhone()) return;
        String phone = edit_login_account_code.getText().toString().trim();

        ToastHelper.showToast(this, "已发送");
        HttpClient.sendCode("login", phone, this, RequestCommandCode.SEND_CODE);

        sendCDing = true;
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_login_code_send.setText(millisUntilFinished / 1000 + "S重新获取");
            }

            @Override
            public void onFinish() {
                tv_login_code_send.setText("重新获取");
                sendCDing = false;
            }
        };
        countDownTimer.start();
    }

    private boolean checkPhone() {
        if (!RegularUtils.isMobileNO(edit_login_account_code.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
            return false;
        }
        return true;
    }


}
