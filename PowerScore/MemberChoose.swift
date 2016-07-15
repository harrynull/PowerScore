//
//  MemberChoose.swift
//  PowerScore
//
//  Created by Harry on 16/7/15.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

import Foundation

class MemberChooseHelper: NSObject {
    
    //生成数据源
    static func getData(classes:Dictionary<String,ClassData>)->Dictionary<String,[String]>{
        var ret=Dictionary<String,Array<String>>()
        for c in classes{
            ret[c.1.name]=c.1.members;
        }
        return ret
    }
    
}
