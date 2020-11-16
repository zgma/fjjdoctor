package com.qingeng.fjjdoctor.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.qingeng.fjjdoctor.R;


/**
 * Created by ORIOC-MZG on 2018/7/9.
 */

public class NoteDialog extends Dialog implements View.OnClickListener {


    private Context context;

    ImageView iv_close;
    WebView webView;
    Button btn_cancel;
    Button btn_ok;


    private OnNoteDialogListener onNoteDialogListener;

    public void setOnNoteDialogListener(OnNoteDialogListener onNoteDialogListener) {
        this.onNoteDialogListener = onNoteDialogListener;
    }

    public NoteDialog(Context context) {
        super(context);
        this.context = context;
    }

    public NoteDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected NoteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void init(String url, boolean cancelAble) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_agreement_dialog, null, false);
        addContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(cancelAble);
        setCancelable(cancelAble);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        iv_close = contentView.findViewById(R.id.iv_close);
        webView = contentView.findViewById(R.id.webView);
        btn_cancel = contentView.findViewById(R.id.btn_cancel);
        btn_ok = contentView.findViewById(R.id.btn_ok);

        iv_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

//        webView.loadDataWithBaseURL(null, HtmlUtils.getNewContent(content), "text/html", "utf-8", null);
        webView.loadUrl(url);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if (onNoteDialogListener != null) {
                    onNoteDialogListener.onOk();
                    dismiss();
                }
                break;
            case R.id.btn_cancel:
                if (onNoteDialogListener != null) {
                    onNoteDialogListener.onCancel();
                }
                break;
            case R.id.iv_close:
                dismiss();
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        setCanceledOnTouchOutside(true);
    }

    public interface OnNoteDialogListener {
        void onOk();

        void onCancel();
    }

}


