//
//  HeaderView.m
//  Test
//
//  Created by lisongrc on 15/8/27.
//  Copyright (c) 2015年 rcplatform. All rights reserved.
//

#import "HeaderView.h"

@implementation HeaderView

- (void)awakeFromNib {
    
    self.tap = [[UITapGestureRecognizer alloc] init];
    [self addGestureRecognizer:_tap];
}

@end
