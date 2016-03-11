package com.github.hitgif.powerscore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 123 on 02/19/2016.
 */
public class login extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginAction(((EditText) findViewById(R.id.account_editor)).getText().toString(),
                        ((EditText) findViewById(R.id.password_editor)).getText().toString());
            }
        });
        findViewById(R.id.textView22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://scoremanagement.applinzi.com/login.php")));
            }
        });
        findViewById(R.id.textView23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://scoremanagement.applinzi.com/forget.php")));
            }
        });
    }

    public void loginAction(final String username, final String password){
        new Thread(new AccessNetwork("POST",
                "http://scoremanagement.applinzi.com/islogin.php",
                "username="+ username + "&password=" + password, new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch(msg.what){
                            case 0:
                                showToast("登陆失败，用户名或密码错误");
                                break;
                            case 1:
                                SharedPreferences.Editor spEditor = getSharedPreferences("data", Activity.MODE_PRIVATE).edit();
                                spEditor.putString("username", username);
                                spEditor.putString("password", password);
                                spEditor.apply();
                                startActivity(new Intent(getApplication(), MainActivity.class));
                                login.this.finish();
                                break;
                            case 2:
                                showToast("网络异常，请检查网络连接");
                                break;
                        }
                    }
        }, 0)).start();
    }

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    class AccessNetwork implements Runnable{
        private String op ;
        private String url;
        private String params;
        private Handler h;
        private int tag;

        public AccessNetwork(String op, String url, String params, Handler h,int tag) {
            super();
            this.op = op;
            this.url = url;
            this.params = params;
            this.h = h;
            this.tag=tag;
        }

        @Override
        public void run() {
            Message m = new Message();
            m.what = tag;
            if(op.compareTo("POST")==0)
                m.obj = GetPostUtil.sendPost(url, params);
            else
                m.obj = GetPostUtil.sendGet(url, params);

            if(m.obj.toString().replace("\n", "").equals("0")){
                m.what=0;
            }else if(m.obj.toString().replace("\n", "").equals("1")){
                m.what=1;
            }else m.what=2;
            h.sendMessage(m);
        }
    }
}
