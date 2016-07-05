package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.BroadcastReceiver;
import android.widget.TextView;

import com.github.hitgif.powerscore.VoiceToWord;

public class Listener extends Activity implements OnClickListener{
    Button but = null;
    private TextView showte;
    private String textresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listener);
        but = (Button) findViewById(R.id.button1);
        but.setOnClickListener(this);
        IntentFilter filter = new IntentFilter(MyRecognizerDialogLister.action);
        registerReceiver(broadcastReceiver, filter);
        showte = (TextView) findViewById(R.id.showte);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //听写按钮
            case R.id.button1:
                VoiceToWord voice = new VoiceToWord(Listener.this,"5778772d");
                voice.GetWordFromVoice();
                showte.setText("");
                textresult = "";
                break;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            showte.setText(showte.getText() + intent.getExtras().getString("data"));
            textresult = textresult + intent.getExtras().getString("data");
        }
    };
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
    };
}