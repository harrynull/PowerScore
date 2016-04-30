//
//  NavController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/4/30.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "NavController.h"


@interface NavController ()

@end

@implementation NavController;

-(void)initUI
{
    [self.navigationController.navigationBar setBarTintColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    // self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;

}

-(void)viewDidAppear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}
@end