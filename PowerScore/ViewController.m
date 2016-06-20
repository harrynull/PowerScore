//
//  ViewController.m
//  测试Demo
//
//  Created by 933 on 16/3/21.
//  Copyright © 2016年 Andy. All rights reserved.
//

#import "ViewController.h"
#import "AndyScrollView.h"
#import "RightScrollView.h"
#import "History.h"

@interface ViewController ()
<UIScrollViewDelegate,UIGestureRecognizerDelegate,UITableViewDataSource,UITableViewDelegate>
{
    //UIPanGestureRecognizer *pan;

    __weak IBOutlet UIButton *flit;
    
    __weak IBOutlet UIButton *class_bt;

    __weak IBOutlet UIButton *person_bt;
    
    __weak IBOutlet UIButton *person_false;
    
    __weak IBOutlet UIButton *openleft_bt;
    
    __weak IBOutlet UILabel *toolback;
    
    __weak IBOutlet UILabel *movinglable;
    
    __weak IBOutlet UIButton *add_bt;
    
    __weak IBOutlet UITableView *tableview;
    
    __weak IBOutlet UIButton *class_or_student;
    
    NSArray *_histories;

}
@property(nonatomic,strong)AndyScrollView *scroll;
@property(nonatomic,strong)RightScrollView *rscroll;
@property(nonatomic,strong)UIView *leftView;
@property(nonatomic,strong)UIView *rightView;


@end

@implementation ViewController


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [_histories count];
}


-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{

    static NSString * identifier = @"HistoryIdentifier";
    UITableViewCell * cell;
    cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HistoryTableViewCell" owner:self options:nil];
    if ([nib count]>0)
    {
        self.HistoryCell = [nib objectAtIndex:0];
        cell = self.HistoryCell;
    }
    History *history = [_histories objectAtIndex:indexPath.row];
    UILabel *reason = (UILabel *)[cell.contentView viewWithTag:1];
    UILabel *date_short = (UILabel *)[cell.contentView viewWithTag:2];
    UILabel *mark = (UILabel *)[cell.contentView viewWithTag:3];
    UILabel *members = (UILabel *)[cell.contentView viewWithTag:4];
    
    reason.text = history.reason;
    date_short.text = history.date_short;
    mark.text = history.mark;
    members.text = history.members;
    return cell;
}

- (void)viewWillAppear:(BOOL)animated {
    self.navigationController.toolbarHidden = YES;
}


- (void)viewDidLoad {
    
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self initUI];
    [self loadHistory];
    
}
-(void)loadHistory
{
    History *h1 = [[History alloc] init];
    h1.reason = @"讲话";
    h1.date_short = @"06-07";
    h1.mark = @"-1";
    h1.members = @"张三,李四";
    
    History *h2 = [[History alloc] init];
    h2.reason = @"迟到";
    h2.date_short = @"06-08";
    h2.mark = @"-1";
    h2.members = @"王五";
    
    _histories = [NSArray arrayWithObjects:h1, h2, nil];
}
    
-(void)initUI
{
    
    [self.view addSubview:self.scroll];
    [self.view addSubview:self.rscroll];
    [add_bt setBackgroundImage:[UIImage imageNamed:@"add_press"] forState:UIControlStateHighlighted];
    self.navigationController.toolbarHidden = YES;
    [self.navigationController.navigationBar setBarTintColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor],UITextAttributeTextColor, nil]];
    
    [class_or_student setBackgroundImage:[UIImage imageNamed:@"light_blue"] forState:UIControlStateHighlighted];
    class_or_student.adjustsImageWhenHighlighted = NO;
    [class_or_student setTitleEdgeInsets:UIEdgeInsetsMake(0, -10, 0, 10)];
    [class_or_student setImageEdgeInsets:UIEdgeInsetsMake(0, class_or_student.titleLabel.bounds.size.width+5, 0, -class_or_student.titleLabel.bounds.size.width-5)];
    [toolback setBackgroundColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
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
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopen:)name:@"toopen" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopenov:)name:@"toopenov" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopensr:)name:@"toopensr" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopensg:)name:@"toopensg" object:nil];
    // self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;
   
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}

-(void)toopen:(NSNotification *)sender
{
    [self vc_openabouteee];
}
-(void)toopenov:(NSNotification *)sender
{
    [self vc_openoverview];
}
-(void)toopensr:(NSNotification *)sender
{
    [self vc_opensetreason];
}
-(void)toopensg:(NSNotification *)sender
{
    [self vc_opensetgroup];
}
- (IBAction)openleftOnclick:(id)sender {
    self.scroll.stloadleftview;

    

}
- (IBAction)flitOnclick:(id)sender {
    self.rscroll.stloadrightview;
}

- (IBAction)classbtOnclick:(id)sender {
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
    
    [UIView animateWithDuration:0.4f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        movinglable.frame = CGRectMake(0, 41, 90, 3);
    } completion:^(BOOL finished) {
    }];
}

- (IBAction)personbtOnclick:(id)sender {
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
    [UIView animateWithDuration:0.4f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        movinglable.frame = CGRectMake(90, 41, 90, 3);
    } completion:^(BOOL finished) {
    }];
}

- (IBAction)add_btOnclick:(id)sender {
    [self performSegueWithIdentifier:@"gotoadd" sender:self];
}
- (IBAction)class_or_studentOnClick:(id)sender {
    [self performSegueWithIdentifier:@"gotochoosemem" sender:self];
}

-(void)vc_openabout
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopen" object:nil];
}
-(void)vc_openov
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopenov" object:nil];
}
-(void)vc_opensr
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopensr" object:nil];
}
-(void)vc_opensg
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopensg" object:nil];
}
-(void)vc_openabouteee
{
    self.scroll.stcloseleftview;
    [self performSegueWithIdentifier:@"gotoabout" sender:self];
}
-(void)vc_openoverview
{
    self.scroll.stcloseleftview;
    [self performSegueWithIdentifier:@"gotooverview" sender:self];
}
-(void)vc_opensetreason
{
    self.scroll.stcloseleftview;
    [self performSegueWithIdentifier:@"gotosetreason" sender:self];
}
-(void)vc_opensetgroup
{
    self.scroll.stcloseleftview;
    [self performSegueWithIdentifier:@"gotosetgroup" sender:self];
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
-(UIScrollView *)rscroll
{
    if (!_rscroll)
    {
        _rscroll = [[RightScrollView alloc]initWithFrame:CGRectMake(self.view.frame.size.width, 0, self.view.frame.size.width, self.view.frame.size.height)];
        _rscroll.contentSize = CGSizeMake(self.view.frame.size.width * 2, 0);
        _rscroll.pagingEnabled = YES;
        _rscroll.showsHorizontalScrollIndicator = NO;
        _rscroll.delegate = self;
        _rscroll.bounces = NO;
    }
    return _rscroll;
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
