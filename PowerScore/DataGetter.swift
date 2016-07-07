//
//  DataGetter.swift
//  PowerScore
//
//  Created by Harry on 16/7/7.
//  Copyright © 2016年 Harry Null All rights reserved.
//

import UIKit

class DataGetter: NSObject {
    
    //解析数据字符串到Class
    //data:数据字符串, name:班级名
    //返回解析好的班级数据
    func process(data:String, name:String)->ClassData{
        let strs = data.componentsSeparatedByString("\n")
        
        //Members
        let readNow:ClassData=ClassData(_name: strs[0],_members: name)
        
        //Scores
        let strScores = strs[1].componentsSeparatedByString(" ")
        for j in 0...readNow.members.count-1 {
            readNow.scores[j] = Int(strScores[j])!
        }
        
        if (strs.count < 3){
            return readNow
        }
        
        //Histories
        let histories = strs[2].componentsSeparatedByString("|")
        for var j = 0; j < histories.count; j += 5 {
            readNow.histories.append(History(_score: Int(histories[j])!, _names: histories[j + 1], _reason: histories[j + 2], _date: Date(histories[j + 3], dateFormat: "yyyy-MM-dd HH:mm:ss"), _oper: histories[j + 4]))
        }
        
        if (strs.count < 4){
            return readNow
        }
        
        //Unsynced histories
        let usHistories = strs[3].componentsSeparatedByString("|")
        for var j = 0; j < usHistories.count; j += 5 {
        readNow.unsyncHistories.append(History(_score: Int(histories[j])!, _names: histories[j + 1],_reason: histories[j + 2], _date: Date(histories[j + 3], dateFormat: "yyyy-MM-dd HH:mm:ss"), _oper: histories[j + 4]))
        }
        
        return readNow
    }
    
}
