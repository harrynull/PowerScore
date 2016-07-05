package com.github.hitgif.powerscore;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import com.github.hitgif.powerscore.Listener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;


public class MyRecognizerDialogLister implements RecognizerDialogListener{
	private Context context;
	public static final String action = "jason.broadcast.action";
	public MyRecognizerDialogLister(Context context)
	{
		this.context = context;
	}
	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		// TODO Auto-generated method stub
		String text = JsonParser.parseIatResult(results.getResultString());
		System.out.println(text);
		//Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		//if (!text.equals("。")&&!text.equals("?")&&!text.equals("!")&&!text.equals(",")&&!text.equals("？")&&!text.equals("！")&&!text.equals("，")) {
			Intent intent = new Intent(action);
			intent.putExtra("data", text);
			context.sendBroadcast(intent);
		//}

	}

	@Override
	public void onError(SpeechError error) {
		// TODO Auto-generated method stub
		int errorCoder = error.getErrorCode();
		switch (errorCoder) {
		case 10118:
			System.out.println("user didn't speak anything");
			break;
		case 10204:
			System.out.println("can't connect to internet");
			break;
		default:
			break;
		}
	}



}
