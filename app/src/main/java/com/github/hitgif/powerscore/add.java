package com.github.hitgif.powerscore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by 123 on 02/14/2016.
 */
public class add extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add);
    }
}
