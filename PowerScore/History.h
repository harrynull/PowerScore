//
//  History.h
//  PowerScore
//
//  Created by Harry on 16/5/4.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface History : NSObject
@property (copy,nonatomic) NSString *date_short;
@property (copy,nonatomic) NSString *reason;
@property (copy,nonatomic) NSString *members;
@property (copy,nonatomic) NSString *mark;

- (History *)initWithData: (NSString*) date_short : (NSString*) reason : (NSString*) members : (NSString*) mark;

@end
