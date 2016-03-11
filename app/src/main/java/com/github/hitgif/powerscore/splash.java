package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;


import java.io.OutputStream;

/**
 * Created by 王安海 on 01/17/2016.
 */
public class splash extends Activity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private OutputStream os;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(R.drawable.per);
        Util.setSystemBarVisible(this, false);
        super.onCreate(savedInstanceState);
        boolean splash = getSharedPreferences("data", 0).getBoolean("splash", true);

        if(!splash){
            startActivity(new Intent(getApplication(), MainActivity.class));
            splash.this.finish();
            return;
        }

        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);

        setContentView(R.layout.splash);


    }
    class splashhandler implements Runnable{

        public void run() {

            preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
            int count = preferences.getInt("count", 0);
            //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
            if (count == 0) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),LG.class);
                startActivity(intent);
                splash.this.finish();
            }
            else {
                if(!getSharedPreferences("data", Activity.MODE_PRIVATE).getString("username","").isEmpty() || true)
                    startActivity(new Intent(getApplication(), MainActivity.class));
                else
                    startActivity(new Intent(getApplication(), login.class));

                splash.this.finish();
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("count", ++count);
            editor.apply();

        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                Util.setSystemBarVisible(this, !Util.isSystemBarVisible(this));
                break;
            }
        }
        return true;
    }
}
