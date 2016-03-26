package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;


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
        Util.setTranslucent_icon(this);
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        Log.d("3243",String.valueOf(screenHeight));
        if(screenHeight<1280){
            getWindow().setBackgroundDrawableResource(R.drawable.spl_1024);
        }else {
            getWindow().setBackgroundDrawableResource(R.drawable.spl_1920);
        }

        Util.setSystemBarVisible(this, false);
        boolean splash = getSharedPreferences("data", 0).getBoolean("splash", true);

        if(!splash){
            if(!getSharedPreferences("data", Activity.MODE_PRIVATE).getString("username","").isEmpty())
                startActivity(new Intent(getApplication(), login.class));
            else {
                startActivity(new Intent(getApplication(), MainActivity.class));
                splash.this.finish();
                return;
            }
        }

        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1500);

        setContentView(R.layout.splash);
    }
    class splashhandler implements Runnable{

        public void run() {

            preferences = getSharedPreferences("count",MODE_WORLD_READABLE);

                if(!getSharedPreferences("data", Activity.MODE_PRIVATE).getString("username","").isEmpty())
                    startActivity(new Intent(getApplication(), MainActivity.class));
                else
                    startActivity(new Intent(getApplication(), login.class));

                splash.this.finish();


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
