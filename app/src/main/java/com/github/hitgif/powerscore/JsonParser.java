package com.github.hitgif.powerscore;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.TextUtils;

//import com.iflytek.speech.ErrorCode;
//import com.iflytek.speech.SpeechError;
/**
 * ���ƶ˷��ص�Json������н���
 * @author iFlytek
 * @since 20131211
 */
public class JsonParser {
	
	/**
	 * ��д�����Json��ʽ����
	 * @param json
	 * @return
	 */
	public static String parseIatResult(String json) {
		if(TextUtils.isEmpty(json))
			return "";
		
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// ��д����ʣ�Ĭ��ʹ�õ�һ�����
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				�����Ҫ���ѡ������������������ֶ�
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();
	}
	
	/**
	 * ʶ������Json��ʽ����
	 * @param json
	 * @return
	 */
	public static String parseGrammarResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for(int j = 0; j < items.length(); j++)
				{
					JSONObject obj = items.getJSONObject(j);
					if(obj.getString("w").contains("nomatch"))
					{
						ret.append("û��ƥ����.");
						return ret.toString();
					}
					ret.append("�������" + obj.getString("w"));
					ret.append("�����Ŷȡ�" + obj.getInt("sc"));
					ret.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("û��ƥ����.");
		} 
		return ret.toString();
	}
	
	/**
	 * ��������Json��ʽ����
	 * @param json
	 * @return
	 */
	public static String parseUnderstandResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			ret.append("��Ӧ���롿" + joResult.getString("rc") + "\n");
			ret.append("��תд�����" + joResult.getString("text") + "\n");
			ret.append("���������ơ�" + joResult.getString("service") + "\n");
			ret.append("���������ơ�" + joResult.getString("operation") + "\n");
			ret.append("�����������" + json);
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("û��ƥ����.");
		} 
		return ret.toString();
	}
}
