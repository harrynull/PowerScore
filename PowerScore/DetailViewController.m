
//
//  DetailViewController.m
//  PowerScore
//
//  Created by Carbonylgroup on 9/3/16.
//  Copyright © 2016 Gustav Wang. All rights reserved.
//

#import "DetailViewController.h"

@interface DetailViewController ()

@property(nonatomic,strong)NSString *reason;
@property(nonatomic,strong)NSString *m_class;
@property(nonatomic,strong)NSString *member;
@property(nonatomic,strong)NSString *score;
@property(nonatomic,strong)NSString *time;
@property(nonatomic,strong)NSString *oper;


@end

@implementation DetailViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    [self getDetail];
    self.navigationItem.title = @"详细记录";
    // Do any additional setup after loading the view.
}

-(void)getDetail
{
    NSArray *details = [_detailsReceived componentsSeparatedByString:@"|&*&|"];
    _reason = details[0];
    _m_class = details[1];
    _member = details[2];
    _score = details[3];
    _time = details[4];
    _oper = details[5];
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 6;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [self tableView:self.tableView cellForRowAtIndexPath:indexPath];
    return cell.frame.size.height;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString * identifier = @"HistoryIdentifier";
    UITableViewCell * cell;
    cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"DetailTableViewCell" owner:self options:nil];
    if ([nib count]>0)
    {
        self.DetailCell = [nib objectAtIndex:0];
        cell = self.DetailCell;
    }
    // History *history = [[self getTableViewHistories] objectAtIndex:indexPath.row];
    
    UILabel *detail = (UILabel *)[cell.contentView viewWithTag:3];
    UILabel *discrib = (UILabel *)[cell.contentView viewWithTag:2];
    UIImageView *icon = (UIImageView *)[cell.contentView viewWithTag:1];
    
    switch (indexPath.row) {
        case 0:
            discrib.text = @"理由";
            detail.text = _reason;
            [icon setImage:[UIImage imageNamed:@"reason"]];
            break;
        case 1:
            discrib.text = @"班级";
            detail.text = _m_class;
            [icon setImage:[UIImage imageNamed:@"overview"]];
            break;
        case 2:
            discrib.text = @"成员";
            detail.text = _member;
            [icon setImage:[UIImage imageNamed:@"group"]];
            break;
        case 3:
            discrib.text = @"分数";
            detail.text = _score;
            [icon setImage:[UIImage imageNamed:@"mark"]];
            break;
        case 4:
            discrib.text = @"时间";
            detail.text = _time;
            [icon setImage:[UIImage imageNamed:@"time"]];
            break;
        case 5:
            discrib.text = @"记录者";
            detail.text = _oper;
            [icon setImage:[UIImage imageNamed:@"operator"]];
            break;
            
        default:
            break;
    }
    
    
    
    CGRect frame = cell.frame;
    CGSize labelsize = [detail.text sizeWithFont:detail.font constrainedToSize:CGSizeMake(300, 5000) lineBreakMode:UILineBreakModeClip];
    detail.frame = CGRectMake(detail.frame.origin.x, detail.frame.origin.y, labelsize.width, labelsize.height);
    frame.size.height = labelsize.height + 25 + discrib.frame.size.height;
    if(frame.size.height < 70)
    {
        frame.size.height = 70;
    }
    cell.frame = frame;
    
    
    
    return cell;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
