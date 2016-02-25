package com.github.hitgif.powerscore;

import java.util.ArrayList;

/**
 * Created by Null on 2016/2/24.
 */
public class Classes {
    public Classes(String _name, String _members){
        histories = new ArrayList<History>();
        scores = new ArrayList<Integer>();
        name=_name;
        members=_members.split(" ");
    }

    public String name;
    public String[] members;
    public ArrayList<History> histories = new ArrayList<History>();
    public ArrayList<Integer> scores = new ArrayList<Integer>();
}
