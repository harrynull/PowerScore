//
//  Date.swift
//
//  Created by bujiandi(慧趣小歪) on 14/9/26.
//
//  值类型的 Date 使用方便 而且避免使用 @NSCopying 的麻烦
//  基本遵循了官方所有关于值类型的实用协议 放心使用
//

import Foundation

func ==(lhs: Date, rhs: Date) -> Bool {
    return lhs.timeInterval == rhs.timeInterval
}
func <=(lhs: Date, rhs: Date) -> Bool {
    return lhs.timeInterval <= rhs.timeInterval
}
func >=(lhs: Date, rhs: Date) -> Bool {
    return lhs.timeInterval >= rhs.timeInterval
}
func >(lhs: Date, rhs: Date) -> Bool {
    return lhs.timeInterval > rhs.timeInterval
}
func <(lhs: Date, rhs: Date) -> Bool {
    return lhs.timeInterval < rhs.timeInterval
}
func +(lhs: Date, rhs: NSTimeInterval) -> Date {
    return Date(rhs, sinceDate:lhs)
}
func -(lhs: Date, rhs: NSTimeInterval) -> Date {
    return Date(-rhs, sinceDate:lhs)
}
func +(lhs: NSTimeInterval, rhs: Date) -> Date {
    return Date(lhs, sinceDate:rhs)
}
func -(lhs: NSTimeInterval, rhs: Date) -> Date {
    return Date(-lhs, sinceDate:rhs)
}

func +=(inout lhs: Date, rhs: NSTimeInterval) {
    return lhs = Date(rhs, sinceDate:lhs)
}
func -=(inout lhs: Date, rhs: NSTimeInterval) {
    return lhs = Date(-rhs, sinceDate:lhs)
}


struct Date {
    var timeInterval:NSTimeInterval = 0
    
    init() { self.timeInterval = NSDate().timeIntervalSince1970 }
}

// MARK: - 输出
extension Date {
    func stringWithFormat(format:String = "yyyy-MM-dd HH:mm:ss") -> String {
        let formatter = NSDateFormatter()
        formatter.dateFormat = format
        return formatter.stringFromDate(NSDate(timeIntervalSince1970: timeInterval))
    }
}

// MARK: - 计算
extension Date {
    mutating func addDay(day:Int) {
        timeInterval += Double(day) * 24 * 3600
    }
    mutating func addHour(hour:Int) {
        timeInterval += Double(hour) * 3600
    }
    mutating func addMinute(minute:Int) {
        timeInterval += Double(minute) * 60
    }
    mutating func addSecond(second:Int) {
        timeInterval += Double(second)
    }
    mutating func addMonth(month m:Int) {
        let (year, month, day) = getDay()
        let (hour, minute, second) = getTime()
        let era = year / 100
        if let date = NSCalendar.currentCalendar().dateWithEra(era, year: year, month: month + m, day: day, hour: hour, minute: minute, second: second, nanosecond: 0) {
            timeInterval = date.timeIntervalSince1970
        } else {
            timeInterval += Double(m) * 30 * 24 * 3600
        }
    }
    mutating func addYear(year y:Int) {
        let (year, month, day) = getDay()
        let (hour, minute, second) = getTime()
        let era = year / 100
        if let date = NSCalendar.currentCalendar().dateWithEra(era, year: year + y, month: month, day: day, hour: hour, minute: minute, second: second, nanosecond: 0) {
            timeInterval = date.timeIntervalSince1970
        } else {
            timeInterval += Double(y) * 365 * 24 * 3600
        }
    }
}

// MARK: - 判断
extension Date {
    func between(begin:Date,_ over:Date) -> Bool {
        return (self >= begin && self <= over) || (self >= over && self <= begin)
    }
}

// MARK: - 获取 日期 或 时间
extension Date {
    
    // for example : let (year, month, day) = date.getDay()
    func getDay() -> (year:Int, month:Int, day:Int) {
        var year:Int = 0, month:Int = 0, day:Int = 0
        let date = NSDate(timeIntervalSince1970: timeInterval)
        NSCalendar.currentCalendar().getEra(nil, year: &year, month: &month, day: &day, fromDate: date)
        return (year, month, day)
    }
    
    // for example : let (hour, minute, second) = date.getTime()
    func getTime() -> (hour:Int, minute:Int, second:Int) {
        var hour:Int = 0, minute:Int = 0, second:Int = 0
        let date = NSDate(timeIntervalSince1970: timeInterval)
        NSCalendar.currentCalendar().getHour(&hour, minute: &minute, second: &second, nanosecond: nil, fromDate: date)
        return (hour, minute, second)
    }
}

// MARK: - 构造函数
extension Date {
    init(year:Int, month:Int = 1, day:Int = 1, hour:Int = 0, minute:Int = 0, second:Int = 0) {
        let era = year / 100
        if let date = NSCalendar.currentCalendar().dateWithEra(era, year: year, month: month, day: day, hour: hour, minute: minute, second: second, nanosecond: 0) {
            timeInterval = date.timeIntervalSince1970
        }
    }
}

extension Date {
    init(_ v: NSTimeInterval) { timeInterval = v }
    
    init(_ v: NSTimeInterval, sinceDate:Date) {
        let date = NSDate(timeIntervalSince1970: sinceDate.timeInterval)
        timeInterval = NSDate(timeInterval: v, sinceDate: date).timeIntervalSince1970
    }
    
    init(sinceNow: NSTimeInterval) {
        timeInterval = NSDate(timeIntervalSinceNow: sinceNow).timeIntervalSince1970
    }
    
    init(sinceReferenceDate: NSTimeInterval) {
        timeInterval = NSDate(timeIntervalSinceReferenceDate: sinceReferenceDate).timeIntervalSince1970
    }
}

extension Date {
    init(_ v: String, style: NSDateFormatterStyle = .NoStyle) {
        let formatter = NSDateFormatter()
        formatter.dateStyle = style
        if let date = formatter.dateFromString(v) {
            self.timeInterval = date.timeIntervalSince1970
        }
    }
    
    init(_ v: String, dateFormat:String = "yyyy-MM-dd HH:mm:ss") {
        let formatter = NSDateFormatter()
        formatter.dateFormat = dateFormat
        if let date = formatter.dateFromString(v) {
            self.timeInterval = date.timeIntervalSince1970
        }
    }
}

extension Date {
    init(_ v: UInt8) { timeInterval = Double(v) }
    init(_ v: Int8) { timeInterval = Double(v) }
    init(_ v: UInt16) { timeInterval = Double(v) }
    init(_ v: Int16) { timeInterval = Double(v) }
    init(_ v: UInt32) { timeInterval = Double(v) }
    init(_ v: Int32) { timeInterval = Double(v) }
    init(_ v: UInt64) { timeInterval = Double(v) }
    init(_ v: Int64) { timeInterval = Double(v) }
    init(_ v: UInt) { timeInterval = Double(v) }
    init(_ v: Int) { timeInterval = Double(v) }
}

extension Date {
    init(_ v: Float) { timeInterval = Double(v) }
    //init(_ v: Float80) { timeInterval = Double(v) }
}

// MARK: - 可以直接输出
extension Date : CustomStringConvertible {
    var description: String {
        return NSDate(timeIntervalSince1970: timeInterval).description
    }
}
extension Date : CustomDebugStringConvertible {
    var debugDescription: String {
        return NSDate(timeIntervalSince1970: timeInterval).debugDescription
    }
}

// MARK: - 可哈希
extension Date : Hashable {
    var hashValue: Int { return timeInterval.hashValue }
}

// 可以用 == 或 != 对比
extension Date : Equatable {
    
}

// MARK: - 可以用 > < >= <= 对比
extension Date : Comparable {
    
}
