//
//  DetailViewController.h
//  PowerScore
//
//  Created by Carbonylgroup on 9/3/16.
//  Copyright © 2016 Gustav Wang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailViewController : UITableViewController

@property (nonatomic,strong) NSString *detailsReceived;
@property (nonatomic, strong) IBOutlet UITableViewCell *DetailCell;

@end
