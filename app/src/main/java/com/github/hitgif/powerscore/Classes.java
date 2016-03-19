package com.github.hitgif.powerscore;

import java.util.ArrayList;

/**
 * Created by Null on 2016/2/24.
 */
public class Classes {
    public Classes(String _name){
        histories = new ArrayList<History>();
        unsyncHistories = new ArrayList<History>();
        name=_name;
    }
    public void setMembers(String _members) {
        members=_members.split(" ");
        scores=new int[members.length];
    }
    public String name;
    public String[] members;
    public ArrayList<History> histories = new ArrayList<History>();
    public ArrayList<History> unsyncHistories = new ArrayList<History>();
    public int[] scores;
}
