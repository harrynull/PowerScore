package com.github.hitgif.powerscore;

/**
 * Created by 王安海 on 01/17/2016.
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Util {
    @TargetApi(19)
    static public void setTranslucent(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = a.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(a);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    static public void setTranslucent_white(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = a.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(a);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.sdwhi);//通知栏所需颜色
        }

    }

    @TargetApi(19)
    static public void setTranslucent_icon(Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = a.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(a);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.icon_b);//通知栏所需颜色
        }

    }

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
