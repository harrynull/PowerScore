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
    
    let name : String
    let members : [String]
    var histories :[History]
    var unsyncHistories :[History]
    var scores:[Int]
    
}