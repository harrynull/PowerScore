package com.github.hitgif.powerscore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by 123 on 02/19/2016.
 */
public class login extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
    }
}
