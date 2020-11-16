package com.qingeng.fjjdoctor.widget;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.util.DownloadUtil;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.ToastHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.DecimalFormat;

import io.reactivex.functions.Consumer;


/**
 * Created by ORIOC-MZG on 2018/7/9.
 */

public class UpgradeDialog extends Dialog implements DownloadUtil.OnDownloadListener {

    private static final String TAG = "UpgradeDialog";
    private Context context;
    private String url;



    private TextView description;
    private TextView title;
//    private ImageView top;
    private ProgressBar progress_bar;
    private TextView progress_txt;

    private ImageView close;
    private Button btn_upload;

    private LinearLayout progress_layout;
    private LinearLayout content_layout;

    private boolean showProgress = false;

    public UpgradeDialog(Context context) {
        super(context);
        this.context = context;
    }

    public UpgradeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected UpgradeDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void init(final String url,String versionName, boolean cancekable, String desc) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_upgrade_layout, null, false);
        // View layout = inflater.inflate(R.layout.dialog_upgrade_layout, null);
        description = contentView.findViewById(R.id.description);
        title = contentView.findViewById(R.id.title);
        progress_layout = contentView.findViewById(R.id.progress_layout);
        progress_bar = contentView.findViewById(R.id.progress_bar);
        progress_txt = contentView.findViewById(R.id.progress_txt);
        content_layout = contentView.findViewById(R.id.content_layout);

        close = contentView.findViewById(R.id.close);
        btn_upload = contentView.findViewById(R.id.btn_upload);

        close.setOnClickListener(this::onClick);
        btn_upload.setOnClickListener(this::onClick);

        addContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(cancekable);
        setCancelable(cancekable);
        description.setText(desc);
        description.setMovementMethod(ScrollingMovementMethod.getInstance());
        title.setText("v"+versionName);
        /*ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) description.getLayoutParams();
        layoutParams.width = top.getWidth();
        description.setLayoutParams(layoutParams);*/
        progress_txt.setText("0/0");
        progress_bar.setProgress(0);
        progress_layout.setVisibility(View.GONE);
        content_layout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);

        showProgress = false;
        this.url = url;

        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    return true;
                }else {
                    return false;
                }
            }
        });

        close.setVisibility(View.GONE);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) context);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    setCanceledOnTouchOutside(false);
                                    showProgress = true;
                                    progress_layout.setVisibility(View.VISIBLE);
                                    content_layout.setVisibility(View.GONE);
                                    close.setVisibility(View.GONE);
                                    DownloadUtil.get().download(url, MainConstant.COMMON_FILE_PATH,UpgradeDialog.this);
                                } else {
                                    ToastHelper.showToast(context, "没有文件读写权限，无法下载");
                                }
                            }


                        });
                break;
            case R.id.close:
                this.dismiss();
                break;
        }
    }

    @Override
    public void onDownloadSuccess(String filePath) {
        if (onDownloadListener!=null) onDownloadListener.onDownloadSuccess(filePath);
        this.dismiss();
    }

    @Override
    public void onDownloading(long sum,long total) {
        if (total == -1){
            DecimalFormat df=new DecimalFormat("0.00");
            progress_txt.setText("已下载"+df.format((float)sum*1.00f/1024/1024)+"M");
            progress_bar.setProgress((int) (sum/1024/1024));
        }else {
            int progress = (int) (sum * 1.0f / total * 100);
            Message message = new Message();
            message.what = 1;
            message.obj = progress;
            handler.sendMessage(message);


        }
    }

    @Override
    public void onDownloadFailed() {
        if (onDownloadListener!=null) onDownloadListener.onDownloadFailed();
        this.dismiss();
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    progress_txt.setText((int)msg.obj+"/100");
                    progress_bar.setProgress((int)msg.obj);
                    break;
            }
        }
    };


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
   */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (showProgress){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("取消下载");
                builder.setMessage("正在下载，是否取消下载");
                builder.setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UpgradeDialog.this.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else {
                UpgradeDialog.this.dismiss();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        setCanceledOnTouchOutside(true);
        DownloadUtil.get().cancel();
    }


    private DownloadUtil.OnDownloadListener onDownloadListener;

    public void setOnDownloadListener(DownloadUtil.OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }
}


