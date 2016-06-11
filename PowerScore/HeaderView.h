//
//  HeaderView.h
//  Test
//
//  Created by lisongrc on 15/8/27.
//  Copyright (c) 2015年 rcplatform. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HeaderView : UITableViewHeaderFooterView

@property (weak, nonatomic) IBOutlet UIButton *selectButton;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *jiantouInageView;

@property (nonatomic, strong) UITapGestureRecognizer *tap;

@end
