//
//  GlobalData.swift
//  PowerScore
//
//  Created by Harry on 16/7/26.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

import Foundation

class GlobalData:NSObject{

    static var classes=Dictionary<String,ClassData>()
    
    static var classNow:String=""
    
    static var reasons=[String]()
    
    static func getClassNow()->ClassData{
        return classes[classNow]!;
    }
}