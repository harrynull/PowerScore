//
//  OverviewHeader.m
//  PowerScore
//
//  Created by Carbonylgroup on 8/14/16.
//  Copyright © 2016 Gustav Wang. All rights reserved.
//

#import "OverviewHeader.h"

@implementation OverviewHeader

- (void)awakeFromNib {
    
    self.tap = [[UITapGestureRecognizer alloc] init];
    [self addGestureRecognizer:_tap];
}

@end

