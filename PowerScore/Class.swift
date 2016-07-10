//
//  Class.swift
//  PowerScore
//
//  Created by Harry on 16/7/7.
//  Copyright © 2016年 Harry Null. All rights reserved.
//

import Foundation

class ClassData : NSObject{
    
    init(_name:String, _members:String){
        name=_name;
        members=_members.componentsSeparatedByString(" ")
        scores=[Int](count: members.count, repeatedValue: 0)
        histories=[History]()
        unsyncHistories=[History]()
    }
    
    func encodeWithCoder(aCoder: NSCoder){
        aCoder.encodeObject(self.name, forKey: "name")
        aCoder.encodeObject(self.members, forKey: "members")
        aCoder.encodeObject(self.histories, forKey: "histories")
        aCoder.encodeObject(self.unsyncHistories, forKey: "unsyncHistories")
        aCoder.encodeObject(self.scores, forKey: "scores")
    }
    
    required init(coder aDecoder: NSCoder) {
        self.name = aDecoder.decodeObjectForKey("name") as! String
        self.members = aDecoder.decodeObjectForKey("members") as! [String]
        self.histories = aDecoder.decodeObjectForKey("histories") as! [History]
        self.unsyncHistories = aDecoder.decodeObjectForKey("unsyncHistories") as! [History]
        self.scores = aDecoder.decodeObjectForKey("scores") as! [Int]
    }
    
    func getHistories()->[History]{
        return histories;
    }
    
    let name : String
    let members : [String]
    var histories :[History]
    var unsyncHistories :[History]
    var scores:[Int]
    
    func save(id:String){
        let documentPath = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)[0]
        NSKeyedArchiver.archiveRootObject(self, toFile: documentPath+"/class_"+id+".dat")
    }
}