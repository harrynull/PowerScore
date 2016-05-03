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
//锁定竖屏
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
-(void)initUI
{

   
    // self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;

}

-(void)viewDidAppear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
}
@end