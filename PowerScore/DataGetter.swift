//
//  DataGetter.swift
//  PowerScore
//
//  Created by Harry on 16/7/7.
//  Copyright © 2016年 Harry Null All rights reserved.
//

import UIKit

class DataManager: NSObject {
    
    //解析数据字符串到Class
    //data:数据字符串, name:班级名
    //返回解析好的班级数据
    static func process(data:String, name:String)->ClassData{
        let strs = data.componentsSeparatedByString("\n")
        
        //Members
        let readNow:ClassData=ClassData(_name: name,_members: strs[0])
        
        //Scores
        let strScores = strs[1].componentsSeparatedByString(" ")
        for j in 0 ..< readNow.members.count-1 {
            readNow.scores[j] = Int(strScores[j])!
        }
        
        if (strs.count < 3){
            return readNow
        }
        
        //Histories
        let histories = strs[2].componentsSeparatedByString("|")
        for var j = 0; j < histories.count; j += 5 {
            readNow.histories.append(History(_score: Int(histories[j])!, _names: histories[j + 1], _reason: histories[j + 2], _date: Date(histories[j + 3], dateFormat: "yyyy-MM-dd HH:mm:ss:SS"), _oper: histories[j + 4]))
        }
        
        if (strs.count < 4){
            return readNow
        }
        
        //Unsynced histories
        let usHistories = strs[3].componentsSeparatedByString("|")
        for var j = 0; j < usHistories.count; j += 5 {
        readNow.unsyncHistories.append(History(_score: Int(histories[j])!, _names: histories[j + 1],_reason: histories[j + 2], _date: Date(histories[j + 3], dateFormat: "yyyy-MM-dd HH:mm:ss:SS"), _oper: histories[j + 4]))
        }
        
        return readNow
    }
    
    static func loadDataFromFiles()->Dictionary<String,ClassData>{
        let documentPath = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)[0]
        let fileManager = NSFileManager.defaultManager()
        let enumerator:NSDirectoryEnumerator! = fileManager.enumeratorAtPath(documentPath)
        
        var classes=Dictionary<String,ClassData>();
        while let element = enumerator?.nextObject() as? String {
            if(element.substringToIndex(element.startIndex.advancedBy(6)) != "class_"){
                continue;
            }
            
            if let c : ClassData = NSKeyedUnarchiver.unarchiveObjectWithFile(documentPath+"/"+element) as? ClassData{
                classes[element.substringWithRange(Range(start:element.startIndex.advancedBy(6),end:element.endIndex.advancedBy(-4)))]=c
            }
        }
        return classes;
    }
    
    static func loadReasons(defaultReasons:String)->[String]{
        let filePath = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)[0]+"/reasons.plist"
        if(NSFileManager.defaultManager().fileExistsAtPath(filePath)){
            //如果存在，则直接读取
            return NSArray(contentsOfFile: filePath) as! Array as [String]
        }else{
            //如果不存在则新建
            let reasons=defaultReasons.componentsSeparatedByString(",")
            (reasons as NSArray).writeToFile(filePath, atomically: true)
            return reasons
        }
    }
}
