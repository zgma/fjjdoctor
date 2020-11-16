package com.qingeng.fjjdoctor.main.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.viewholder.AbsContactViewHolder;
import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.activity.RobotListActivity;
import com.qingeng.fjjdoctor.main.activity.SystemMessageActivity2;
import com.qingeng.fjjdoctor.main.activity.TeamListActivity;
import com.qingeng.fjjdoctor.main.helper.SystemMessageUnreadManager;
import com.qingeng.fjjdoctor.main.reminder.ReminderId;
import com.qingeng.fjjdoctor.main.reminder.ReminderItem;
import com.qingeng.fjjdoctor.main.reminder.ReminderManager;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.user.MyBlackListActivity;
import com.qingeng.fjjdoctor.user.MyPhoneActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FuncViewHolder extends AbsContactViewHolder<FuncViewHolder.FuncItem> implements ReminderManager.UnreadNumChangedCallback {

    private static ArrayList<WeakReference<ReminderManager.UnreadNumChangedCallback>> sUnreadCallbackRefs = new ArrayList<>();

    private ImageView image;
    private TextView funcName;
    private TextView unreadNum;
    private Set<ReminderManager.UnreadNumChangedCallback> callbacks = new HashSet<>();


    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.func_contacts_item, null);
        this.image = view.findViewById(R.id.img_head);
        this.funcName = view.findViewById(R.id.tv_func_name);
        this.unreadNum = view.findViewById(R.id.tab_new_msg_label);
        return view;
    }

    @Override
    public void refresh(ContactDataAdapter contactAdapter, int position, FuncItem item) {
        if (item == FuncItem.VERIFY) {
            funcName.setText("新的朋友");
            image.setImageResource(R.mipmap.head_img_new_friends);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
            updateUnreadNum(unreadCount);
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
            sUnreadCallbackRefs.add(new WeakReference<ReminderManager.UnreadNumChangedCallback>(this));
        } else if (item == FuncItem.ROBOT) {
            funcName.setText("智能机器人");
            image.setImageResource(R.drawable.ic_robot);
        } else if (item == FuncItem.NORMAL_TEAM) {
            funcName.setText("讨论组");
            image.setImageResource(R.mipmap.head_img_group_chat);
        } else if (item == FuncItem.ADVANCED_TEAM) {
            funcName.setText("群聊");
            image.setImageResource(R.mipmap.head_img_group_chat);
        } else if (item == FuncItem.BLACK_LIST) {
            funcName.setText("黑名单");
            image.setImageResource(R.mipmap.head_img_group_chat);
        } else if (item == FuncItem.MY_COMPUTER) {
            funcName.setText("我的电脑");
            image.setImageResource(R.drawable.ic_my_computer);
        }else if (item == FuncItem.PHONE) {
            funcName.setText("手机通讯录");
            image.setImageResource(R.mipmap.head_img_phone_book);
        }

        if (item != FuncItem.VERIFY) {
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            unreadNum.setVisibility(View.GONE);
        }
    }


    private void updateUnreadNum(int unreadCount) {
        // 2.*版本viewholder复用问题
        if (unreadCount > 0 && funcName.getText().toString().equals("验证提醒")) {
            unreadNum.setVisibility(View.VISIBLE);
            unreadNum.setText("" + unreadCount);
        } else {
            unreadNum.setVisibility(View.GONE);
        }

        if (dataChangeLinstenter != null) dataChangeLinstenter.updateUnreadNum(unreadCount);
    }

    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        if (item.getId() != ReminderId.CONTACT) {
            return;
        }
        updateUnreadNum(item.getUnread());
    }

    public static void unRegisterUnreadNumChangedCallback() {
        Iterator<WeakReference<ReminderManager.UnreadNumChangedCallback>> iter = sUnreadCallbackRefs.iterator();
        while (iter.hasNext()) {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(iter.next().get());
            iter.remove();
        }
    }


    public final static class FuncItem extends AbsContactItem {
        static final FuncItem VERIFY = new FuncItem();
        static final FuncItem ROBOT = new FuncItem();
        static final FuncItem NORMAL_TEAM = new FuncItem();
        static final FuncItem ADVANCED_TEAM = new FuncItem();
        static final FuncItem BLACK_LIST = new FuncItem();
        static final FuncItem MY_COMPUTER = new FuncItem();
        static final FuncItem PHONE = new FuncItem();

        @Override
        public int getItemType() {
            return ItemTypes.FUNC;
        }

        @Override
        public String belongsGroup() {
            return null;
        }


        public static List<AbsContactItem> provide() {
            List<AbsContactItem> items = new ArrayList<>();
            items.add(VERIFY);
            //items.add(ROBOT);
//            items.add(NORMAL_TEAM);
            items.add(ADVANCED_TEAM);
//            items.add(BLACK_LIST);
//            items.add(MY_COMPUTER);
            items.add(PHONE);

            return items;
        }

        public static void handle(Context context, AbsContactItem item) {
            if (item == VERIFY) {
//                SystemMessageActivity.start(context);
                SystemMessageActivity2.start(context);
            } else if (item == ROBOT) {
                RobotListActivity.start(context);
            } else if (item == NORMAL_TEAM) {
                TeamListActivity.start(context, ItemTypes.TEAMS.NORMAL_TEAM);
            } else if (item == ADVANCED_TEAM) {
                TeamListActivity.start(context, ItemTypes.TEAMS.ADVANCED_TEAM);
            } else if (item == MY_COMPUTER) {
                SessionHelper.startP2PSession(context, DemoCache.getAccount());
            } else if (item == BLACK_LIST) {
//                BlackListActivity.start(context);
                MyBlackListActivity.start(context);

            }else if (item == PHONE) {
//                BlackListActivity.start(context);
                MyPhoneActivity.start(context);

            }
        }
    }
}
