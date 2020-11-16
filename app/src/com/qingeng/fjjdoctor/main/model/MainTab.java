package com.qingeng.fjjdoctor.main.model;

import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.fragment.ContactListFragment;
import com.qingeng.fjjdoctor.main.fragment.FindHomeFragment;
import com.qingeng.fjjdoctor.main.fragment.MainTabFragment;
import com.qingeng.fjjdoctor.main.fragment.MineHomeFragment;
import com.qingeng.fjjdoctor.main.fragment.SessionListFragment;
import com.qingeng.fjjdoctor.main.reminder.ReminderId;

public enum MainTab {
    RECENT_CONTACTS(0, ReminderId.SESSION, SessionListFragment.class, R.string.main_tab_home, R.layout.session_list,R.mipmap.tab_home_0,R.mipmap.tab_home_1),

    CONTACT(1, ReminderId.CONTACT, ContactListFragment.class, R.string.main_tab_patient, R.layout.contacts_list,R.mipmap.tab_plus_0,R.mipmap.tab_plus_1),

   // FIND(2, ReminderId.FIND, FindHomeFragment.class, R.string.main_tab_find, R.layout.find_home,R.drawable.tab_btn_find_sel,R.drawable.tab_btn_find_def),

    ME(2, ReminderId.ME, MineHomeFragment.class, R.string.main_tab_me, R.layout.mine_home,R.mipmap.tab_mine_0,R.mipmap.tab_mine_1);


    public final int tabIndex;

    public final int reminderId;

    public final Class<? extends MainTabFragment> clazz;

    public final int resId;

    public final int fragmentId;

    public final int unCheckedIcon;
    public final int checkedIcon;

    public final int layoutId;

    MainTab(int index, int reminderId, Class<? extends MainTabFragment> clazz, int resId, int layoutId,int unCheckedIcon ,int checkedIcon) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
        this.unCheckedIcon = unCheckedIcon;
        this.checkedIcon = checkedIcon;
        this.layoutId = layoutId;
    }

    public static final MainTab fromReminderId(int reminderId) {
        for (MainTab value : MainTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }

        return null;
    }

    public static final MainTab fromTabIndex(int tabIndex) {
        for (MainTab value : MainTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}
