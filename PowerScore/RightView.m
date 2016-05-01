//
//  RightView.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/1.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "RightView.h"
#import "ViewController.h"

@interface ViewController ()
@property(nonatomic,strong)ViewController *VC;
@end

@implementation RightView
//锁定竖屏
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
- (void)choosedateOnclick
{
    //筛选日期
}

- (void)chooseplusOnclick
{
    //筛选加减分
}

-(id)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0];
        
        CGRect recti = CGRectMake(0,35, 135, 60);
        UIButton *choosedate = [[UIButton alloc]initWithFrame:recti];
        [choosedate setBackgroundImage:[UIImage imageNamed:@"light_blue"] forState:UIControlStateHighlighted];
        [choosedate addTarget:self action:@selector(choosedateOnclick) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:choosedate];
        
        CGRect recti2 = CGRectMake(0,120, 135, 60);
        UIButton *chooseplus = [[UIButton alloc]initWithFrame:recti2];
        [chooseplus setBackgroundImage:[UIImage imageNamed:@"light_blue"] forState:UIControlStateHighlighted];
        [chooseplus addTarget:self action:@selector(chooseplusOnclick) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:chooseplus];
        
        /////////////////////////////////////////////////

        CGRect rect = CGRectMake(18,35, 135, 30);
        UILabel *timelable = [[UILabel alloc]initWithFrame:rect];
        [timelable setText:@"时间"];
        [timelable setFont:[UIFont fontWithName:@"Helvetica" size:15.0]];
        [timelable setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
        [self addSubview:timelable];
        
        CGRect rect1 = CGRectMake(18,60, 135, 30);
        UILabel *showdate = [[UILabel alloc]initWithFrame:rect1];
        [showdate setText:@"不限"];
        [showdate setTextColor:[UIColor whiteColor]];
        [showdate setFont:[UIFont fontWithName:@"Helvetica" size:20.0]];
        [self addSubview:showdate];
        
        CGRect rect2 = CGRectMake(70,35, 135, 30);
        UILabel *showyear = [[UILabel alloc]initWithFrame:rect2];
        [showyear setText:@"2016年"];
        [showyear setTextColor:[UIColor whiteColor]];
        [showyear setFont:[UIFont fontWithName:@"Helvetica" size:15.0]];
        [self addSubview:showyear];
        
        CGRect rect3 = CGRectMake(102,70, 12 , 12);
        UIImageView *downdate = [[UIImageView alloc]initWithFrame:rect3];
        [downdate setImage:[UIImage imageNamed:@"trangle"]];
        [self addSubview:downdate];
        
        CGRect rect4 = CGRectMake(7.5,105, 120 , 5);
        UIImageView *right_divide = [[UIImageView alloc]initWithFrame:rect4];
        [right_divide setImage:[UIImage imageNamed:@"right_divide"]];
        [self addSubview:right_divide];
        
        ///////////////////////////////
        
        CGRect rect5 = CGRectMake(18,120, 135, 30);
        UILabel *pluslable = [[UILabel alloc]initWithFrame:rect5];
        [pluslable setText:@"加／减分"];
        [pluslable setFont:[UIFont fontWithName:@"Helvetica" size:15.0]];
        [pluslable setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
        [self addSubview:pluslable];
        
        CGRect rect6= CGRectMake(18,145, 135, 30);
        UILabel *showplus = [[UILabel alloc]initWithFrame:rect6];
        [showplus setText:@"不限"];
        [showplus setTextColor:[UIColor whiteColor]];
        [showplus setFont:[UIFont fontWithName:@"Helvetica" size:20.0]];
        [self addSubview:showplus];
        
        CGRect rect7 = CGRectMake(102,155, 12 , 12);
        UIImageView *downplus = [[UIImageView alloc]initWithFrame:rect7];
        [downplus setImage:[UIImage imageNamed:@"trangle"]];
        [self addSubview:downplus];
        
            }
    return self;
}
-(instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if(self)
    {
        
    }
    return self;
}

@end
