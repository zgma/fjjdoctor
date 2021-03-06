package com.qingeng.fjjdoctor.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.session.search.DisplayMessageActivity;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItemFilter;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.item.MsgItem;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.business.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.business.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.business.contact.core.viewholder.ContactHolder;
import com.netease.nim.uikit.business.contact.core.viewholder.LabelHolder;
import com.netease.nim.uikit.business.contact.core.viewholder.MsgHolder;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;

/**
 * 全局搜索页面
 * 支持通讯录搜索、消息全文检索
 * <p/>
 * Created by huangjun on 2015/4/13.
 */
public class HistorySearchActivity extends UI implements OnItemClickListener {

    private static final String EXTRA_ACCID = "EXTRA_ACCID";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private ContactDataAdapter adapter;

    private String userName;
    private String userAccid;


    private ListView lvContacts;

    private SearchView searchView;

    private EditText input;
    private ImageView clear_input;

    public static final void start(Context context,String accid,String name) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ACCID, accid);
        intent.putExtra(EXTRA_NAME, name);
        intent.setClass(context, HistorySearchActivity.class);
        context.startActivity(intent);
    }

    /*public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.global_search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                MenuItemCompat.expandActionView(item);
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                finish();

                return false;
            }
        });

        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                showKeyboard(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (StringUtil.isEmpty(query)) {
                    lvContacts.setVisibility(View.GONE);
                } else {
                    lvContacts.setVisibility(View.VISIBLE);
                }
                adapter.query(query);
                return true;
            }
        });searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                showKeyboard(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (StringUtil.isEmpty(query)) {
                    lvContacts.setVisibility(View.GONE);
                } else {
                    lvContacts.setVisibility(View.VISIBLE);
                }
                adapter.query(query);
                return true;
            }
        });
        return true;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.global_search_result);

        userAccid = getIntent().getStringExtra(EXTRA_ACCID);
        userName = getIntent().getStringExtra(EXTRA_NAME);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString="查找与"+userName+"的聊天记录";
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.searchResultList);
        lvContacts.setVisibility(View.GONE);
        SearchGroupStrategy searchGroupStrategy = new SearchGroupStrategy();
        IContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FRIEND, ItemTypes.TEAM, ItemTypes.MSG);

        adapter = new ContactDataAdapter(this, searchGroupStrategy, dataProvider);
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);
        adapter.addViewHolder(ItemTypes.MSG, MsgHolder.class);

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        findViewById(R.id.global_search_root).setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    finish();
                    return true;
                }
                return false;
            }
        });

        lvContacts.setVisibility(View.VISIBLE);

        clear_input = findView(R.id.clear_input);
        input = findView(R.id.input);
        input.addTextChangedListener(watcherInput);
        clear_input.setVisibility(View.GONE);
        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });


    }


    private TextWatcher watcherInput = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                clear_input.setVisibility(View.VISIBLE);
                ContactItemFilter contactItemFilter = new ContactItemFilter() {
                    @Override
                    public boolean filter(AbsContactItem item) {
                        boolean filter = false;
                        if (item instanceof MsgItem){
                            MsgItem msgItem = (MsgItem) item;
                            String msgUserId = msgItem.getRecord().getRecord().id.replace("0_","").replace("1_","");
                            if (userAccid.equals(msgUserId)){
                                filter = true;
                            }
                        }
                        return !filter; // 消息;
                    }
                };
                adapter.setFilter(contactItemFilter);
                adapter.query(input.getText().toString().trim());
            } else {
                clear_input.setVisibility(View.GONE);
            }
            input.setSelection(input.getText().length());//将光标移至文字末尾
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    private static class SearchGroupStrategy extends ContactGroupStrategy {
        public static final String GROUP_FRIEND = "FRIEND";
        public static final String GROUP_TEAM = "TEAM";
        public static final String GROUP_MSG = "MSG";

        SearchGroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, "");
            add(GROUP_TEAM, 1, "群组");
            add(GROUP_FRIEND, 2, "好友");
            add(GROUP_MSG, 3, "聊天记录");
        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.FRIEND:
                    return GROUP_FRIEND;
                case ItemTypes.TEAM:
                    return GROUP_TEAM;
                case ItemTypes.MSG:
                    return GROUP_MSG;
                default:
                    return null;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        switch (item.getItemType()) {
            case ItemTypes.TEAM: {
                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                break;
            }

            case ItemTypes.FRIEND: {
                SessionHelper.startP2PSession(this, ((ContactItem) item).getContact().getContactId());
                break;
            }

            case ItemTypes.MSG: {
                MsgIndexRecord msgIndexRecord = ((MsgItem) item).getRecord();
                if (msgIndexRecord.getCount() > 1) {
                    GlobalSearchDetailActivity2.start(this, msgIndexRecord);
                } else {
                    DisplayMessageActivity.start(this, msgIndexRecord.getMessage());
                }
                break;
            }

            default:
                break;
        }
    }

}
