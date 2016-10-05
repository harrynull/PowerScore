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
        Util.setTranslucent_white(this);
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://powerscore.duapp.com/login.php")));
            }
        });
        findViewById(R.id.textView23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("请联系管理员");
            }
        });
    }

    public void loginAction(final String username, final String password){
        new Thread(new AccessNetwork(true,
                "http://powerscore.duapp.com/api/is_login.php",
                "username="+ username + "&password=" + password, new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.obj.toString().replace("\n","").charAt(0)){
                            case '0':
                                showToast("登陆失败，用户名或密码错误");
                                break;
                            case '1':
                                SharedPreferences.Editor spEditor = getSharedPreferences("data", Activity.MODE_PRIVATE).edit();
                                spEditor.putString("username", username);
                                spEditor.putString("password", password);
                                spEditor.apply();
                                startActivity(new Intent(getApplication(), MainActivity.class));
                                login.this.finish();
                                break;
                            default:
                                showToast("网络异常，请检查网络连接");
                                break;
                        }
                    }
        })).start();
    }

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
