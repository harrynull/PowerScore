//
//  HistoryListTableViewCell.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/1.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "HistoryListTableViewCell.h"


@implementation HistoryListTableViewCell

@synthesize showimage;
@synthesize showdate_short;
@synthesize showreason;
@synthesize showmembers;
@synthesize showmark;

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

//重写set函数
- (void)setImage:(UIImage *)img
{
    if(![img isEqual:showimage])
    {
        showimage = [img copy];
        self.imagev.image = showimage;
    }
    
}
- (void)setDate:(NSString *)dat
{
    if(![dat isEqual:showdate_short])
    {
        showdate_short = [dat copy];
        self.datel.text = showdate_short;
    }
    
}
- (void)setReason:(NSString *)rea
{
    if(![rea isEqual:showreason])
    {
        showreason = [rea copy];
        self.reasonl.text = showreason;
    }
    
}
- (void)setMenbers:(NSString *)mem
{
    if(![mem isEqual:showmembers])
    {
        showmembers = [mem copy];
        self.meml.text = showmembers;
    }
    
}
- (void)setMark:(NSString *)mar
{
    if(![mar isEqual:showmark])
    {
        showmark = [mar copy];
        self.markl.text = showmark;
    }
    
}
@end
