//
//  RightScrollView.h
//  PowerScore
//
//  Created by Gustav Wang on 16/5/1.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RightScrollView : UIScrollView
-(void) stloadrightview;
@property(nonatomic)UIPanGestureRecognizer *pan;
@end
