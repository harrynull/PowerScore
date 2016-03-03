package com.github.hitgif.powerscore;

/**
 * Created by 王安海 on 01/17/2016.
 */
import android.app.Activity;
import android.view.View;

public class Util {
    /**
     * 设置系统栏可见性
     */
    public static void setSystemBarVisible(final Activity context,boolean visible) {
        int flag = context.getWindow().getDecorView().getSystemUiVisibility();
//		int fullScreen = View.SYSTEM_UI_FLAG_SHOW_FULLSCREEN;
        int fullScreen = 0x8;
        if(visible) {
            if((flag & fullScreen) != 0) {
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        } else {
            if((flag & fullScreen) == 0) {
                context.getWindow().getDecorView().setSystemUiVisibility(flag | fullScreen);
            }
        }
    }

    /**
     * 判断状态栏是否显示
     */
    public static boolean isSystemBarVisible(final Activity context) {
        int flag = context.getWindow().getDecorView().getSystemUiVisibility();
//		return (flag & View.SYSTEM_UI_FLAG_SHOW_FULLSCREEN) != 0;
        return (flag & 0x8) == 0;
    }
}
