package com.netease.nim.uikit.impl.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.nim.uikit.api.NimUIKit;

/**
 * Created by hzxuwen on 2015/10/21.
 */
public class UserPreferences {

    private final static String KEY_EARPHONE_MODE = "KEY_EARPHONE_MODE";

    private final static String KEY_LAST_TEAM_CAN_P2P = "KEY_LAST_TEAM_CAN_P2P";

    private final static String KEY_LAST_TEAM_REMOVE_MEMBER_NOTICE = "KEY_LAST_TEAM_REMOVE_MEMBER_NOTICE";

    public static void setEarPhoneModeEnable(boolean on) {
        saveBoolean(KEY_EARPHONE_MODE, on);
    }

    public static boolean isEarPhoneModeEnable() {
        return getBoolean(KEY_EARPHONE_MODE, true);
    }


    public static void setLastTeamCanP2P(String data) {
        saveString(KEY_LAST_TEAM_CAN_P2P, data);
    }

    public static String getLastTeamCanP2P() {
        return getString(KEY_LAST_TEAM_CAN_P2P);
    }


    public static void setLastTeamRemoveMemberNotice(String data) {
        saveString(KEY_LAST_TEAM_REMOVE_MEMBER_NOTICE, data);
    }

    public static String getLastTeamRemoveMemberNotice() {
        return getString(KEY_LAST_TEAM_REMOVE_MEMBER_NOTICE);
    }





    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    private static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return NimUIKit.getContext().getSharedPreferences("UIKit." + NimUIKit.getAccount(), Context.MODE_PRIVATE);
    }
}
