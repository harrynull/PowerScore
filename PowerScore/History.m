//
//  History.m
//  PowerScore
//
//  Created by Harry on 16/5/4.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "History.h"

@implementation History
@synthesize date_short;
@synthesize reason;
@synthesize members;
@synthesize mark;

- (History *)initWithData: (NSString*) _date_short : (NSString*) _reason : (NSString*) _members : (NSString*) _mark {
    if (self  = [super init]) {
        self.date_short=_date_short;
        self.reason=_reason;
        self.members=_members;
        self.mark=_mark;
    }
    return self;
    
}

@end
