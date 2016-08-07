package com.github.hitgif.powerscore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 王安海 on 12/23/2015.
 */
public class History {
    public History(int _score, String _names, String _reason, Date _date, String _oper){
        score=_score;
        names =_names;
        reason =_reason;
        oper=_oper;
        //score_now=_score_now;
        date=_date;
        shortReason=reason.length() > 6 ? reason.substring(0, 6) + "…" : reason;
        shortNames=names.length() > 25 ? names.substring(0, 25) + "…" : names;
        scoreWithSign=(score > 0 ? "+" : "") + (score / 10.0);
        dateStr= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.CHINA).format(date);
        shortDateStr=new SimpleDateFormat("MM-dd", Locale.CHINA).format(date);
    }
    public History(Date _date){
        score=0;
        names ="";
        reason ="";
        oper="";
        date=_date;
        shortReason="";
        shortNames="";
        scoreWithSign="";
        dateStr= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.CHINA).format(date);
        shortDateStr="";
    }
    public final Date date;
    public final int score;
    public final String names;
    public final String reason;
    public final String oper;

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
