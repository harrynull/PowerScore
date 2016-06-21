//
//  AndyScrollView.m
//  测试Demo
//
//  Created by 933 on 16/3/22.
//  Copyright © 2016年 Andy. All rights reserved.
//

#import "AndyScrollView.h"
#import "LeftView.h"

#define showLeftViewMaxWidth 100 //拖拽出来的View宽

#define maxWidth 270 //可拖动最大距离

#define rightmaxWidth 100 //右视图最大距离

@interface AndyScrollView ()<UIGestureRecognizerDelegate>
{
    CGPoint initialPosition;     //初始位置
}
@property(nonatomic,strong)LeftView *leftView;
@property(nonatomic,strong)UIView *backView;//蒙版

@end

@implementation AndyScrollView
//锁定竖屏


-(UIView *)leftView
{
    if (!_leftView) {
 
        _leftView = [[LeftView alloc]initWithFrame:CGRectMake(-maxWidth, 0, maxWidth, self.frame.size.height)];
        
        
    }
    return _leftView;
}
-(UIView *)backView
{
    if (!_backView)
    {
        _backView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 2000, self.frame.size.height)];
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
        UIPanGestureRecognizer *backPan = [[UIPanGestureRecognizer alloc]initWithTarget:self action:@selector(backPanGes:)];
        [_backView addGestureRecognizer:backPan];
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(backViewTapGes:)];
        [_backView addGestureRecognizer:tap];
    }
    return _backView;
}


-(void)backViewTapGes:(UITapGestureRecognizer *)ges
{
    [UIView animateWithDuration:0.5f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        _leftView.frame = CGRectMake(-maxWidth, 0, maxWidth, self.frame.size.height);
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
    } completion:^(BOOL finished) {
        self.pan.enabled = YES;
        [_backView removeFromSuperview];
    }];
    
}
-(void)stloadleftview
{
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    [window addSubview:self.backView];
    [window addSubview:self.leftView];
    [UIView animateWithDuration:0.5f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        _leftView.frame = CGRectMake(0, 0, maxWidth, self.frame.size.height);
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0.5];
    } completion:^(BOOL finished) {
        self.pan.enabled = NO;
    }];
}
-(void)stcloseleftview
{
    
    [UIView animateWithDuration:0.1f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        _leftView.frame = CGRectMake(-maxWidth, 0, maxWidth, self.frame.size.height);
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
    } completion:^(BOOL finished) {
        self.pan.enabled = YES;
        [_leftView removeFromSuperview];
        [_backView removeFromSuperview];
        [self removeFromSuperview];
    }];
}
-(void)backPanGes:(UIPanGestureRecognizer *)ges
{
    if (ges.state == UIGestureRecognizerStateBegan) {
        //获取leftView初始位置
        initialPosition.x = self.leftView.center.x;

    }
    
    CGPoint point = [ges translationInView:self];
    

    if (point.x <= 0 && point.x <= maxWidth) {
        _leftView.center = CGPointMake(initialPosition.x + point.x , _leftView.center.y);
        CGFloat alpha = MIN(0.5, (maxWidth + point.x) / (2* maxWidth));
        _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:alpha];
    }
    
    if (ges.state == UIGestureRecognizerStateEnded)
    {
        if ( - point.x <= showLeftViewMaxWidth) {
            
        
            [UIView animateWithDuration:0.35f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
                _leftView.frame = CGRectMake(0, 0, maxWidth, self.frame.size.height);
                _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0.5];
            } completion:^(BOOL finished) {
                
            }];

        }else if ( - point.x > showLeftViewMaxWidth &&  - point.x <= maxWidth)
        {
            [UIView animateWithDuration:0.35f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
                _leftView.frame = CGRectMake(-maxWidth, 0, maxWidth, self.frame.size.height);
                _backView.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0];
            } completion:^(BOOL finished) {
                [_backView removeFromSuperview];
            }];
        }
    }

    
    
    
}

@end
