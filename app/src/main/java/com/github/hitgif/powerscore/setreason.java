package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by 123 on 02/15/2016.
 */
public class setreason extends Activity{
    private static String[] strsreason;
    private EditText input;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent iB = new Intent();
            iB.putExtra("reason", "NULL");
            iB.setClass(setreason.this, add.class);
            setreason.this.setResult(2, iB);
            setreason.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setreason);

        input = (EditText)findViewById(R.id.input);

        String reasons = getSharedPreferences("data", 0).getString("reasons", "上课发言 打扫卫生 小组加分 上午迟到 中午迟到 上课讲话 晚修讲话 随意下位 没有值日");
        strsreason = reasons.split(" ");
        ListView lv = (ListView) findViewById(R.id.listView2);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strsreason));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                input.setText(strsreason[position]);

            }
        });
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!input.getText().toString().matches("")) {
                    //返回并传值
                    Intent i = new Intent();
                    i.putExtra("reason", input.getText().toString());
                    i.setClass(setreason.this, add.class);
                    setreason.this.setResult(2, i);
                    setreason.this.finish();
                } else {
                    Intent iB = new Intent();
                    iB.putExtra("reason", "&&nothing");
                    iB.setClass(setreason.this, add.class);
                    setreason.this.setResult(2, iB);
                    setreason.this.finish();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iB = new Intent();
                iB.putExtra("reason", "NULL");
                iB.setClass(setreason.this, add.class);
                setreason.this.setResult(2, iB);
                setreason.this.finish();
            }
        });
        findViewById(R.id.back).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#7fffffff"));
                ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.backdown);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((TextView) findViewById(R.id.textView15)).setTextColor(Color.parseColor("#ffffff"));
                    ((ImageView) findViewById(R.id.backimc)).setImageResource(R.drawable.back);
                }
                return false;
            }
        });

    }
}
