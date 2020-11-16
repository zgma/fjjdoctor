package com.netease.nim.uikit.business.team.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.netease.nim.uikit.R;
import com.netease.nim.uikit.util.AnimateUtil;

import java.lang.ref.WeakReference;

public class PayDialog implements View.OnClickListener {

    //支付方式 //8微信 9支付宝 10余额
    public static final int PAY_MODE_BY_WX = 8;
    public static final int PAY_MODE_BY_ZFB = 9;

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    private String title;
    private String price;


    private WeakReference<Context> contextWeak;
    private ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//PayDialog 的 根View

    private ImageView iv_close;

    private TextView tv_title;
    private TextView tv_price;



    private RelativeLayout rl_pay_wx;
    private RelativeLayout rl_pay_zfb;


    private OnDialogListener onDialogListener;
    private boolean isShowing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.CENTER;

    private int payBy = PAY_MODE_BY_WX;

    private boolean isDKH = false;

    public static final int PAY_MODE_BY_DKH = 11;


    public PayDialog(Context context, String title, String priceStr) {
        this.contextWeak = new WeakReference<>(context);
        initData(title, priceStr);
        initViews();
        init();
        initEvents();
        payBy = PAY_MODE_BY_WX;
        setCancelable(true);
    }


    /**
     * 获取数据
     */
    protected void initData(String title, String price) {
        this.title = title;
        this.price = price;
    }


    protected void initViews() {
        Context context = contextWeak.get();
        if (context == null) return;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.view_dialog_background, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = rootView.findViewById(R.id.content_container);

        params.gravity = Gravity.BOTTOM;
        contentContainer.setLayoutParams(params);
        gravity = Gravity.BOTTOM;

        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.view_pay_dialog, contentContainer);

        iv_close = viewGroup.findViewById(R.id.iv_close);
        tv_title = viewGroup.findViewById(R.id.tv_title);
        tv_price = viewGroup.findViewById(R.id.tv_price);



        rl_pay_wx = viewGroup.findViewById(R.id.rl_pay_wx);
        rl_pay_zfb = viewGroup.findViewById(R.id.rl_pay_zfb);

        tv_title.setText(title);
        tv_price.setText(price);

        iv_close.setOnClickListener(this);
        rl_pay_wx.setOnClickListener(this);
        rl_pay_zfb.setOnClickListener(this);

    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    protected void initEvents() {

    }


    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        isShowing = true;
        decorView.addView(view);
        contentContainer.startAnimation(inAnim);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null && isShowing;
    }

    public void dismiss() {
        //消失动画
        outAnim.setAnimationListener(outAnimListener);
        contentContainer.startAnimation(outAnim);
    }

    public void dismissImmediately() {
        new Handler().post(new Runnable() {
            public void run() {
                decorView.removeView(rootView);
            }
        });
        isShowing = false;
        if (onDialogListener != null) {
            onDialogListener.onDismiss(this);
        }

    }

    public Animation getInAnimation() {
        Context context = contextWeak.get();
        if (context == null) return null;

        int res = AnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        Context context = contextWeak.get();
        if (context == null) return null;

        int res = AnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public PayDialog setOnDismissListener(OnDialogListener onDialogListener) {
        this.onDialogListener = onDialogListener;
        return this;
    }

    private Animation.AnimationListener outAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dismissImmediately();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        contentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_close) {
            dismiss();
        } else if (i == R.id.rl_pay_wx) {
            payBy = PAY_MODE_BY_WX;
            goPay();
        } else if (i == R.id.rl_pay_zfb) {
            payBy = PAY_MODE_BY_ZFB;
            goPay();
        }
    }

    public interface OnDialogListener {
        void onDismiss(Object o);
        void onPay(int payBy, String moneyStr);
    }

    private void goPay(){
        if (onDialogListener != null) {
            onDialogListener.onPay(payBy,tv_price.getText().toString().trim());
        }
    }


}
