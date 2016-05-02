//
//  LeftView.m
//  测试Demo
//
//  Created by 933 on 16/3/23.
//  Copyright © 2016年 Andy. All rights reserved.
//

#import "LeftView.h"
#import "ViewController.h"
@interface LeftView ()

@end

@implementation LeftView


-(void)openabout
{
    ViewController *v =[[ViewController alloc]init];
    NSLog(@"2333");
    [v vc_openabout];
}

-(id)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor colorWithRed:239.0/255.0 green:241.0/255.0 blue:242.0/255.0 alpha:1.0];
        
        NSString *accpath = [[NSBundle mainBundle] pathForResource:@"account" ofType:@"plist"];
                             NSDictionary *accdic = [[NSDictionary alloc] initWithContentsOfFile:accpath];
        NSString *usernamest = [accdic objectForKey:@"realname"];
        
        CGRect Header = CGRectMake(0, 0, 270, 185);
        UIView * headview = [[UIView alloc]initWithFrame:Header];
        headview.backgroundColor = [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0];
        [self addSubview:headview];
        
        
        CGRect recti = CGRectMake(25,40, 80, 80);
        UIImageView *headImage = [[UIImageView alloc] initWithFrame:recti];
        headImage.layer.masksToBounds = YES;
        headImage.layer.cornerRadius = 38;
        headImage.translatesAutoresizingMaskIntoConstraints = NO;
        [headImage setImage:[UIImage imageNamed:@"setting_tx"]];
        [self addSubview:headImage];
        
        
        CGRect rect = CGRectMake(30,130, 240, 30);
        UILabel *username = [[UILabel alloc]initWithFrame:rect];
        [username setText:usernamest];
        [username setTextColor:[UIColor whiteColor]];
        [username setFont:[UIFont fontWithName:@"Helvetica" size:20.0]];
        [self addSubview:username];
        
       ///////////////////////////////////////////////////////////////////////////////////
        
        CGRect rect1 = CGRectMake(0,200, 270, 50);
        UIButton *overview = [[UIButton alloc] initWithFrame:rect1];
        [overview.titleLabel setFont:[UIFont systemFontOfSize:18.0]];
        [overview setTitleColor:[UIColor colorWithRed:59.0/255.0 green:69.0/255.0 blue:75.0/255.0 alpha:1] forState:UIControlStateNormal];
        [overview setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateHighlighted];
        overview.titleLabel.textAlignment = NSTextAlignmentLeft;
        [overview setTitle:@"     学分总览                  " forState:(UIControlStateNormal)];
        [overview setImage:[UIImage imageNamed:@"overview"] forState:UIControlStateNormal];
        overview.adjustsImageWhenHighlighted = NO;
        overview.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:overview];
        
        CGRect rect2 = CGRectMake(0,255, 270, 50);
        UIButton *reason = [[UIButton alloc]initWithFrame:rect2];
        reason.titleLabel.font = [UIFont systemFontOfSize:18.0];
        reason.titleLabel.textAlignment = NSTextAlignmentLeft;
        [reason setTitleColor:[UIColor colorWithRed:59.0/255.0 green:69.0/255.0 blue:75.0/255.0 alpha:1] forState:UIControlStateNormal];
        [reason setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateHighlighted];
        [reason setTitle:@"     常用理由                  " forState:(UIControlStateNormal)];
        [reason setImage:[UIImage imageNamed:@"reason"] forState:UIControlStateNormal];
        reason.translatesAutoresizingMaskIntoConstraints = NO;
        reason.adjustsImageWhenHighlighted = NO;
        [self addSubview:reason];
        
        
        CGRect rect3 = CGRectMake(0,310, 270, 50);
        UIButton *group = [[UIButton alloc]initWithFrame:rect3];
        group.titleLabel.textAlignment = NSTextAlignmentLeft;
        group.titleLabel.font = [UIFont systemFontOfSize:18.0];
        [group setTitleColor:[UIColor colorWithRed:59.0/255.0 green:69.0/255.0 blue:75.0/255.0 alpha:1] forState:UIControlStateNormal];
        [group setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateHighlighted];
        [group setTitle:@"     成员组                     " forState:(UIControlStateNormal)];
        [group setImage:[UIImage imageNamed:@"group"] forState:UIControlStateNormal];
        group.translatesAutoresizingMaskIntoConstraints = NO;
        group.adjustsImageWhenHighlighted = NO;
        [self addSubview:group];
        
        
        CGRect rect4 = CGRectMake(0,365, 270, 50);
        UIButton *loadpage = [[UIButton alloc]initWithFrame:rect4];
        loadpage.titleLabel.textAlignment = NSTextAlignmentLeft;
        loadpage.titleLabel.font = [UIFont systemFontOfSize:18.0];
        [loadpage setTitleColor:[UIColor colorWithRed:59.0/255.0 green:69.0/255.0 blue:75.0/255.0 alpha:1] forState:UIControlStateNormal];
        [loadpage setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateHighlighted];
        [loadpage setTitle:@"     显示启动页              " forState:(UIControlStateNormal)];
        [loadpage setImage:[UIImage imageNamed:@"window"] forState:UIControlStateNormal];
        loadpage.translatesAutoresizingMaskIntoConstraints = NO;
        loadpage.adjustsImageWhenHighlighted = NO;
        [self addSubview:loadpage];
        
        ////////////////////////////////////////////////////////////////////////////////////
        CGRect rect5 = CGRectMake(0,self.frame.size.height-55, 135, 55);
        UIButton *about = [[UIButton alloc]initWithFrame:rect5];
        about.titleLabel.textAlignment = NSTextAlignmentLeft;
        about.titleLabel.font = [UIFont systemFontOfSize:16.0];
        [about setTitleColor:[UIColor colorWithRed:59.0/255.0 green:69.0/255.0 blue:75.0/255.0 alpha:1] forState:UIControlStateNormal];
        [about setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateNormal];
        [about setBackgroundImage:[UIImage imageNamed:@"grey"] forState:UIControlStateHighlighted];
        [about addTarget:self action:@selector(openabout) forControlEvents:UIControlEventTouchUpInside];
        [about setTitle:@"关于" forState:(UIControlStateNormal)];
        about.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:about];
        
        
        CGRect rect6 = CGRectMake(135,self.frame.size.height-55, 135, 55);
        UIButton *unlog = [[UIButton alloc]initWithFrame:rect6];
        unlog.titleLabel.textAlignment = NSTextAlignmentLeft;
        unlog.titleLabel.font = [UIFont systemFontOfSize:16.0];
        [unlog setTitleColor:[UIColor colorWithRed:243.0/255.0 green:32.0/255.0 blue:32.0/255.0 alpha:1] forState:UIControlStateNormal];
        [unlog setBackgroundImage:[UIImage imageNamed:@"white"] forState:UIControlStateNormal];
        [unlog setBackgroundImage:[UIImage imageNamed:@"grey"] forState:UIControlStateHighlighted];
        [unlog setTitle:@"退出登录" forState:(UIControlStateNormal)];
        unlog.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:unlog];
        
        CGRect rect7 = CGRectMake(self.frame.size.width-55,42, 40, 40);
        UIButton *home = [[UIButton alloc]initWithFrame:rect7];
        [home setImage:[UIImage imageNamed:@"home"] forState:UIControlStateNormal];
        home.translatesAutoresizingMaskIntoConstraints = NO;
        [self addSubview:home];
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
