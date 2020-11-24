package com.qingeng.fjjdoctor.base;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseViewPagerAdapter
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-06-20  14:47
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {
    private String[] title;
    private List<Fragment> mFragments = new ArrayList<>();

    public BaseViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        title = titles;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
