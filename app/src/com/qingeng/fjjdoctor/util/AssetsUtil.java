package com.qingeng.fjjdoctor.util;

import android.content.Context;

import org.xutils.common.util.IOUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by CoderMario on 2018-01-18.
 */
public class AssetsUtil {

    public static String read(Context context, String fileName){
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        if (context == null || fileName == null || 0 >= fileName.length()) {
            return null;
        }
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            if (null == inputStream) {
                return null;
            }
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(inputStreamReader);
            IOUtil.closeQuietly(bufferedReader);
            IOUtil.closeQuietly(inputStream);
        }
        return builder.toString();
    }
}
