package com.github.hitgif.powerscore;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
public class SFProgrssDialog extends Dialog {
    private static SFProgrssDialog m_progrssDialog;
    private SFProgrssDialog(Context context, int theme) {
        super(context, theme);
    }
    public static SFProgrssDialog createProgrssDialog(Context context) {
        m_progrssDialog = new SFProgrssDialog(context,
                R.style.SF_pressDialogCustom);
        m_progrssDialog.setContentView(R.layout.sf_view_custom_progress_dialog);
        m_progrssDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return m_progrssDialog;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (null == m_progrssDialog)
            return;
    }
    public SFProgrssDialog setMessage(String msg) {
        TextView loadingTextView = (TextView) m_progrssDialog
                .findViewById(R.id.sf_tv_progress_dialog_loading);
        if (!TextUtils.isEmpty(msg))
            loadingTextView.setText(msg);
        else
            loadingTextView.setText(R.string.sf_progress_dialog_image_loading);
        return m_progrssDialog;
    }
}