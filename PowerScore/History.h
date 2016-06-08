//
//  History.h
//  PowerScore
//
//  Created by Harry on 16/5/4.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface History : NSObject
@property (nonatomic, strong) NSString *date_short;
@property (nonatomic, strong) NSString *reason;
@property (nonatomic, strong) NSString *members;
@property (nonatomic, strong) NSString *mark;
@property  BOOL *ispositive;

@end
