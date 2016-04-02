package com.github.hitgif.powerscore;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class ToastCommom {

    private static ToastCommom toastCommom;

    private Toast toast;

    private ToastCommom(){
    }

    public static ToastCommom createToastConfig(){
        if (toastCommom==null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     * @param context
     * @param root
     * @param tvString
     */

    public void ToastShow(Context context,ViewGroup root,String tvString){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml,root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        //ImageView mImageView = (ImageView) layout.findViewById(R.id.iv);
        //mImageView.setBackgroundResource(R.drawable.ic_launcher);
        text.setText(tvString);
        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}