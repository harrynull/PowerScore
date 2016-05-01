//
//  HistoryListTableViewCell.h
//  PowerScore
//
//  Created by Gustav Wang on 16/5/1.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoryListTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *datel;
@property (weak, nonatomic) IBOutlet UILabel *reasonl;
@property (weak, nonatomic) IBOutlet UILabel *meml;
@property (weak, nonatomic) IBOutlet UILabel *markl;
@property (weak, nonatomic) IBOutlet UIImageView *imagev;
@property (copy,nonatomic) UIImage *showimage;
@property (copy,nonatomic) NSString *showdate_short;
@property (copy,nonatomic) NSString *showreason;
@property (copy,nonatomic) NSString *showmembers;
@property (copy,nonatomic) NSString *showmark;
@end
