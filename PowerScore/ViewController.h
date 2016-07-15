//
//  ViewController.h
//  测试Demo
//
//  Created by 933 on 16/3/21.
//  Copyright © 2016年 Andy. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
-(void)vc_openabout;
-(void)vc_openov;
-(void)vc_opensr;
-(void)vc_opensg;
-(void)vc_unlog;
-(void)vc_changelaunch:(BOOL *)launch;

@property (nonatomic, strong) IBOutlet UITableViewCell *HistoryCell;
@end

