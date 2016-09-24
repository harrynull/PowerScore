package com.github.hitgif.powerscore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Null on 2016/2/24.
 */
public class ClassData {
    public ClassData(String _name){
        histories = new ArrayList<History>();
        unsyncedHistories = new ArrayList<History>();
        name=_name;
    }
    public void loadMembers(JSONArray _members) {
        scores = new int[_members.length()];
        ArrayList<String> mems=new ArrayList<String>();
        try {
            for(int i=0;i!=_members.length();++i){
                mems.add(_members.getJSONObject(i).getString("name"));
                scores[i]=_members.getJSONObject(i).getInt("score");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        members = mems.toArray(new String[mems.size()]);
    }
    public JSONArray exportMembers() throws JSONException {
        JSONArray json=new JSONArray();
        if(members==null) return json;
        for(int i=0;i!=members.length;++i){
            JSONObject obj = new JSONObject();
            obj.put("name",members[i]);
            obj.put("score",scores[i]);
            json.put(obj);
        }
        return json;
    }
    public void loadHistories(JSONArray _his) throws JSONException {
        histories.clear();
        for (int j = 0; j < _his.length(); ++j) {
            histories.add(new History(_his.getJSONObject(j)));
        }
    }
    public void loadUnsyncedHistories(JSONArray _his) throws JSONException {
        unsyncedHistories.clear();
        for (int j = 0; j < _his.length(); ++j) {
            unsyncedHistories.add(new History(_his.getJSONObject(j)));
        }
    }
    public String name;
    public String[] members;
    public ArrayList<History> histories = new ArrayList<History>();
    public ArrayList<History> unsyncedHistories = new ArrayList<History>();
    public int[] scores;
}
