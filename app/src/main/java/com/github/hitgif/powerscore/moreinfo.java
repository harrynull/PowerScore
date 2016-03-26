package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 123 on 03/02/2016.
 */
public class moreinfo extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Util.setTranslucent(this);
        setContentView(R.layout.moreinfo);

        Intent intent=getIntent();
        String record=intent.getStringExtra("record");
        String[] allInfo=record.split("[|]");
        String reason_s = allInfo[0];
        String class_s = allInfo[1];
        String member_s = allInfo[2];
        String score_s = allInfo[3];
        String time_s = allInfo[4];
        String operator_s = allInfo[5];

        ((TextView)findViewById(R.id.show_reason)).setText(reason_s);
        ((TextView)findViewById(R.id.show_class)).setText(class_s);
        ((TextView)findViewById(R.id.show_members)).setText(member_s);
        ((TextView)findViewById(R.id.show_mark)).setText(score_s);
        ((TextView)findViewById(R.id.show_fulltime)).setText(time_s);
        ((TextView)findViewById(R.id.show_operator)).setText(operator_s);

        findViewById(R.id.backmore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreinfo.this.finish();
            }
        });
    }
}
