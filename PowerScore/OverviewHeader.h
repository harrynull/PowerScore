//
//  OverviewHeader.h
//  PowerScore
//
//  Created by Carbonylgroup on 8/14/16.
//  Copyright © 2016 Gustav Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OverviewHeader : UITableViewHeaderFooterView

@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *jiantouInageView;

@property (nonatomic, strong) UITapGestureRecognizer *tap;

@end