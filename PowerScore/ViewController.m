//
//  ViewController.m
//  测试Demo
//
//  Created by 933 on 16/3/21.
//  Copyright © 2016年 Andy. All rights reserved.
//

#import "ViewController.h"
#import "AndyScrollView.h"


@interface ViewController ()
<UIScrollViewDelegate,UIGestureRecognizerDelegate>
{
    //UIPanGestureRecognizer *pan;

    __weak IBOutlet UIButton *flit;
    
    __weak IBOutlet UIButton *class_bt;

    __weak IBOutlet UIButton *person_bt;
    
    __weak IBOutlet UIButton *person_false;
    
    __weak IBOutlet UIButton *openleft_bt;
}
@property(nonatomic,strong)AndyScrollView *scroll;
@property(nonatomic,strong)UIView *leftView;


@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self initUI];
    
    
}

-(void)initUI
{
    
    [self.view addSubview:self.scroll];
    [self.navigationController.navigationBar setBarTintColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    [flit setBackgroundImage:[UIImage imageNamed:@"flitback"] forState:UIControlStateHighlighted];
    if ([self.navigationController.navigationBar respondsToSelector:@selector( setBackgroundImage:forBarMetrics:)]){
        NSArray *list=self.navigationController.navigationBar.subviews;
        for (id obj in list) {
            if ([obj isKindOfClass:[UIImageView class]]) {
                UIImageView *imageView=(UIImageView *)obj;
                NSArray *list2=imageView.subviews;
                for (id obj2 in list2) {
                    if ([obj2 isKindOfClass:[UIImageView class]]) {
                        UIImageView *imageView2=(UIImageView *)obj2;
                        imageView2.hidden=YES;
                    }
                }
            }
        }
    }

    
       // self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;
}

- (IBAction)openleftOnclick:(id)sender {
    self.scroll.stloadleftview;

}

- (IBAction)classbtOnclick:(id)sender {
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
}

- (IBAction)personbtOnclick:(id)sender {
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
}


-(UIScrollView *)scroll
{
    if (!_scroll)
    {
        _scroll = [[AndyScrollView alloc]initWithFrame:CGRectMake(0, 0, 20, self.view.frame.size.height)];
        _scroll.contentSize = CGSizeMake(self.view.frame.size.width * 2, 0);
        _scroll.pagingEnabled = YES;
        _scroll.showsHorizontalScrollIndicator = NO;
        _scroll.delegate = self;
        _scroll.bounces = NO;
    }
    return _scroll;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView;
{
//    NSLog(@">>>>>>>>>%f",scrollView.contentOffset.x);
    if (scrollView.contentOffset.x <= 0) {
        self.scroll.pan.enabled = YES;
    }else if (scrollView.contentOffset.x >= self.view.frame.size.width)
    {
        self.scroll.pan.enabled = NO;
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end