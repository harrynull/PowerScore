//
//  History.swift
//  PowerScore
//
//  Created by Harry on 16/7/7.
//  Copyright © 2016年 Harry Null. All rights reserved.
//

import Foundation

class History : NSObject{
    init(_score:Int, _names:String, _reason:String, _date:Date, _oper:String) {
        score = _score
        names = _names
        reason = _reason
        oper=_oper
        date=_date
        shortReason = reason.characters.count > 6 ? reason.substringWithRange(Range<String.Index>(start:reason.startIndex,end:reason.startIndex.advancedBy(6))) + "…" : reason
        shortNames = names.characters.count > 10 ? names.substringWithRange(Range<String.Index>(start:reason.startIndex,end:reason.startIndex.advancedBy(10))) + "…" : names
        scoreWithSign = (score > 0 ? "+" : "-") + String(Double(score) / 10.0)
        dateStr = date.stringWithFormat("yyyy-MM-dd HH:mm:ss:SS")
        shortDateStr = date.stringWithFormat("MM-dd")
    }
    func encodeWithCoder(aCoder: NSCoder){
        aCoder.encodeObject(self.date.timeInterval, forKey: "timeInterval")
        aCoder.encodeObject(self.score, forKey: "score")
        aCoder.encodeObject(self.names, forKey: "names")
        aCoder.encodeObject(self.reason, forKey: "reason")
        aCoder.encodeObject(self.oper, forKey: "oper")
    }
    
    required init(coder aDecoder: NSCoder) {
        let dateInterval = aDecoder.decodeObjectForKey("timeInterval") as! NSTimeInterval
        self.date=Date(dateInterval)
        self.score = aDecoder.decodeObjectForKey("score") as! Int
        self.names = aDecoder.decodeObjectForKey("names") as! String
        self.reason = aDecoder.decodeObjectForKey("reason") as! String
        self.oper = aDecoder.decodeObjectForKey("oper") as! String
        
        shortReason = reason.characters.count > 6 ? reason.substringWithRange(Range<String.Index>(start:reason.startIndex,end:reason.startIndex.advancedBy(6))) + "…" : reason
        shortNames = names.characters.count > 10 ? names.substringWithRange(Range<String.Index>(start:reason.startIndex,end:reason.startIndex.advancedBy(10))) + "…" : names
        scoreWithSign = (score > 0 ? "+" : "-") + String(Double(score) / 10.0)
        dateStr = date.stringWithFormat("yyyy-MM-dd HH:mm:ss:SS")
        shortDateStr = date.stringWithFormat("MM-dd")
    }
    let date:Date
    let score:Int
    let names:String
    let reason:String
    let oper:String
    
    let shortReason:String
    let shortNames:String
    let scoreWithSign:String
    
    let dateStr:String
    let shortDateStr:String
    
    func getRealScore()->String{
        return scoreWithSign
    }
    func getDateFast(showAll:Bool)->String{
        if(showAll){return dateStr}
        else {return shortDateStr}
    }
}
