package com.github.hitgif.powerscore;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by 123 on 02/14/2016.
 */
public class add extends Activity{

    private Button bd;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b0;
    private TextView score;
    private ImageButton bplus;
    private ImageButton bminus;
    private ImageButton backspace;
    private boolean isplus = true;
    private boolean caninput = true;
    private boolean onlyzero = true;
    private boolean caninputpoint = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add);
        bd = (Button)findViewById(R.id.button4);
        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);
        b3 = (Button)findViewById(R.id.b3);
        b4 = (Button)findViewById(R.id.b4);
        b5 = (Button)findViewById(R.id.b5);
        b6 = (Button)findViewById(R.id.b6);
        b7 = (Button)findViewById(R.id.b7);
        b8 = (Button)findViewById(R.id.b8);
        b9 = (Button)findViewById(R.id.b9);
        b0 = (Button)findViewById(R.id.b0);
        score = (TextView)findViewById(R.id.score);
        bplus = (ImageButton) findViewById(R.id.bplus);
        bminus = (ImageButton) findViewById(R.id.bminus);
        backspace = (ImageButton) findViewById(R.id.backspace);
        ViewTreeObserver vto3 = backspace.getViewTreeObserver();
        ViewTreeObserver vto2 = b9.getViewTreeObserver();
        ViewTreeObserver vto = bd.getViewTreeObserver();

        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                b9.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bd.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bd.getLayoutParams().width = b9.getWidth();
            }
        });

        vto3.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                backspace.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                backspace.getLayoutParams().width = b9.getWidth();
            }
        });

        bplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bplus.setImageResource(R.mipmap.plus_white);
                bminus.setImageResource(R.mipmap.minus_blue);
                isplus = true;
            }
        });


        bminus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bplus.setImageResource(R.mipmap.plus_blue);
                bminus.setImageResource(R.mipmap.minus_white);
                isplus = false;
            }
        });




        ///////////////////////////////////////////////////////小键盘begin

                                                                b1.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("1");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "1");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                                onlyzero = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b2.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("2");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "2");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b3.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("3");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "3");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b4.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("4");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "4");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b4.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("4");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "4");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b5.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("5");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "5");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b6.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("6");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "6");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b7.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("7");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "7");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b8.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("8");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "8");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b9.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("9");
                                                                                onlyzero = false;
                                                                            }else {
                                                                                score.setText(score.getText() + "9");
                                                                            }

                                                                        }
                                                                        if (score.getText().toString().contains(".")) {
                                                                            if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                caninput = false;
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                                b0.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput){
                                                                            if (onlyzero){
                                                                                score.setText("0");
                                                                                onlyzero = true;
                                                                                caninputpoint = true;
                                                                            }else {
                                                                                score.setText(score.getText() + "0");
                                                                            }

                                                                            if (score.getText().toString().contains(".")) {
                                                                                if (score.getText().length() - 1 - score.getText().toString().indexOf(".") > 0) {
                                                                                    caninput = false;
                                                                                }
                                                                            }


                                                                        }
                                                                    }
                                                                });
                                                                bd.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (caninput&&caninputpoint){
                                                                                score.setText(score.getText() + ".");
                                                                                onlyzero = false;
                                                                                caninputpoint = false;
                                                                            }

                                                                    }
                                                                });
                                                                backspace.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                        if (score.getText().length() == 1) {
                                                                            score.setText("0");
                                                                            onlyzero = true;
                                                                            caninputpoint = true;
                                                                        }else {
                                                                            String s = score.getText().toString();
                                                                            s = s.substring(0,score.getText().length() - 1);
                                                                            score.setText(s);
                                                                            caninput = true;
                                                                            if (!s.contains(".")){
                                                                                caninputpoint = true;
                                                                            }
                                                                            if (s.matches("0")){
                                                                                onlyzero = true;
                                                                            }
                                                                        }

                                                                    }
                                                                });

        ///////////////////////////////////////////////////////小键盘end

    }
}
