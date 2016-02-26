package com.github.hitgif.powerscore;

import com.github.hitgif.powerscore.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import java.lang.reflect.Field;

public class personal extends Activity{
    private Context context;
    private Dialog dialog;
    private Display display;
    private ImageView back;
    private int sbarf;



    public personal(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

    }

    public personal builder(int sbar) {
        sbarf = sbar;
        Point size = new Point();

        View view = LayoutInflater.from(context).inflate(
                R.layout.personal, null);
        view.setMinimumWidth(display.getWidth());
        back=(ImageView) view.findViewById(R.id.backp);
        dialog = new Dialog(context, R.style.personalStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        display.getSize(size);

        dialogWindow.setLayout(display.getWidth() / 10 * 7, size.y-sbarf*2);

        return this;
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public personal setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public personal setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }


}
