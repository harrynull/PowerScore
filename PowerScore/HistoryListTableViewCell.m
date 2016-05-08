//
//  HistoryListTableViewCell.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/1.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "HistoryListTableViewCell.h"


#define KColor(r,g,b)  [UIColor colorWithHue:r/255.0 saturation:g/255.0 brightness:b/255.0 alpha:1]
#define kStatusTableViewCellControlSpacing 10//控件间距
#define kStatusTableViewCellBackgroundColor KColor(251,251,251)
#define kStatusGrayColor KColor(50,50,50)
#define kStatusLightGrayColor KColor(120,120,120)

#define kStatusTableViewCellAvatarWidth 40 //头像宽度
#define kStatusTableViewCellAvatarHeight kStatusTableViewCellAvatarWidth
#define kStatusTableViewCellUserNameFontSize 14
#define kStatusTableViewCellMbTypeWidth 13 //会员图标宽度
#define kStatusTableViewCellMbTypeHeight kStatusTableViewCellMbTypeWidth
#define kStatusTableViewCellCreateAtFontSize 12
#define kStatusTableViewCellSourceFontSize 12
#define kStatusTableViewCellTextFontSize 14
@implementation HistoryListTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        _datel = [[UILabel alloc]init];
        [self addSubview:_datel];
        _reasonl = [[UILabel alloc]init];
        _reasonl.textColor = kStatusGrayColor;
        _reasonl.font = [UIFont systemFontOfSize:kStatusTableViewCellTextFontSize];
        [self addSubview:_reasonl];
        _meml = [[UILabel alloc]init];
        [self addSubview:_meml];
        _markl = [[UILabel alloc]init];
        _markl.textColor = kStatusGrayColor;
        _markl.font = [UIFont systemFontOfSize:kStatusTableViewCellTextFontSize];
        [_markl setText:@"233"];
        [self addSubview:_markl];
        _imagev = [[UIImageView alloc]init];
        [self addSubview:_imagev];
    }
    return self;
}


@end
