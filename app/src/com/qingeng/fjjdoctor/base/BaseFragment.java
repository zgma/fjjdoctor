package com.qingeng.fjjdoctor.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    private static final String TAG = "fragment_BaseFragment";

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    public boolean mHasLoadedOnce = false;
    public boolean isPrepared = false;

    private View rootView;//缓存Fragment view

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), container, false);
            ButterKnife.bind(this,rootView);
            initView();
            isPrepared = true;
            lazyLoad();
        } else {
            final ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                new Handler().post(new Runnable() {
                    public void run() {
                        parent.removeView(rootView);
                    }
                });
            }
        }

        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            lazyLoad();
        } else {
        }
    }

    protected void lazyLoad()  {
        if (mHasLoadedOnce || !isPrepared)
            return;
        mHasLoadedOnce = true;
        loadData();
    }


    public abstract int getLayout();

    protected abstract void initView() ;

    protected abstract void loadData() ;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHasLoadedOnce = false;
        isPrepared = false;
        System.out.println(TAG + "：onDestroyView");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }



}
