package com.github.hitgif.powerscore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 王安海 on 12/23/2015.
 */
public class History {
    public History(int _score, String _names, String _reason, Date _date){
        score=_score;
        names =_names;
        reason =_reason;
        //score_now=_score_now;
        date=_date;
    }

    public Date date=new Date();
    public int score;
    public String names = new String();
    public String reason = new String();

    public String getScore(){
        return (score>0?"+":"")+(score / 10.0);
    }
    public String getDate(){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	return sdf.format(date);  
    }
}
