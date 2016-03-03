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
        shortReason=reason.length() > 6 ? reason.substring(0, 6) + "…" : reason;
        shortNames=names.length() > 10 ? names.substring(0, 10) + "…" : names;
        scoreWithSign=(score > 0 ? "+" : "") + (score / 10.0);
        dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        shortDateStr=new SimpleDateFormat("MM-dd").format(date);
    }

    public final Date date;
    public final int score;
    public final String names;
    public final String reason;

    public final String shortReason;
    public final String shortNames;
    public final String scoreWithSign;

    private final String dateStr;
    private final String shortDateStr;

    public String getScore(){
        return (score>0?"+":"")+(score / 10.0);
    }
    public String getDate(boolean showAll){
        if(showAll) return dateStr;
        else return shortDateStr;
    }
}
