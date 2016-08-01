package com.example.su.zhihuribao.UI.Custom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.example.su.zhihuribao.R;


public class ChangeTheme {

    public static int getThemeValue(Context context){
        // 从SharedPreferences读取主题的值，如果为日间主题，返回0，夜间主题返回1
        SharedPreferences sp = context.getSharedPreferences("user_settings",Context.MODE_PRIVATE);
        return sp.getInt("theme",0);
    }

    /**
     * 储存主题对应的int值
     * @param context 上下文
     * @param themeValue 主题对应int值 0为日间主题，1为夜间主题
     */
    public static void setThemeValue(Context context,int themeValue){
        SharedPreferences sp = context.getSharedPreferences("user_settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("theme",themeValue);
        editor.apply();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setTheme(Activity activity){
        if ( ChangeTheme.getThemeValue(activity) == 0){
            activity.setTheme(R.style.DayTheme);
        } else {
            activity.setTheme(R.style.NightTheme);
        }
//            Window window = activity.getWindow();
//            // clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            // finally change the color
//            if (ChangeTheme.getThemeValue(activity) == 0){
//                window.setStatusBarColor(activity.getResources().getColor(R.color.day_toolbar));
//            } else {
//                window.setStatusBarColor(activity.getResources().getColor(R.color.night_toolbar));
//            }

        }
}
