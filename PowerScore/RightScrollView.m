//
//  RightScrollView.m
//  测试Demo
//
//  Created by 933 on 16/3/22.
//  Copyright © 2016年 Right. All rights reserved.
//

#import "RightScrollView.h"
#import "RightView.h"




#define rightmaxWidth 135//右视图最大距离

@interface RightScrollView ()<UIGestureRecognizerDelegate>
{
    CGPoint initialPosition;     //初始位置
}
@property(nonatomic,strong)RightView *RightView;
@property(nonatomic,strong)UIView *backView;//蒙版

@end

@implementation RightScrollView

//锁定竖屏
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
-(UIView *)RightView
{
    if (!_RightView) {
        
        _RightView = [[RightView alloc]initWithFrame:CGRectMake(self.frame.size.width, 0, rightmaxWidth, self.frame.size.height)];
        
    }
    return _RightView;
}
-(UIView *)backView
{
    if (!_backView)
    {
        _backView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 2000, self.frame.size.height)];
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(backViewTapGes:)];
        [_backView addGestureRecognizer:tap];
    }
    return _backView;
}


-(void)backViewTapGes:(UITapGestureRecognizer *)ges
{
    [UIView animateWithDuration:0.5f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        _RightView.frame = CGRectMake(self.frame.size.width, 0, rightmaxWidth, self.frame.size.height);
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
    } completion:^(BOOL finished) {
        self.pan.enabled = YES;
        [_backView removeFromSuperview];
    }];
    
}
-(void)stloadrightview
{
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    [window addSubview:self.backView];
    [window addSubview:self.RightView];
    [UIView animateWithDuration:0.5f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        _RightView.frame = CGRectMake(self.frame.size.width-rightmaxWidth, 0, rightmaxWidth, self.frame.size.height);
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0.5];
    } completion:^(BOOL finished) {
        self.pan.enabled = NO;
    }];
}

-(void)changePlus
{
    [self.RightView changeToPlus];
}
-(void)changeMinus
{
    [self.RightView changeToMinus];
}
-(void)changeAll
{
    [self.RightView changeToAll];
}
-(void)setDatetem:(NSString *)date
{
    [self.RightView setDate:date];
}
@end
