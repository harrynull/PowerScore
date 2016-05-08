//
//  UpLineLable.h
//  PowerScore
//
//  Created by Gustav Wang on 16/5/3.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef enum
{
    VerticalAlignmentTop = 0, // default
    VerticalAlignmentMiddle,
    VerticalAlignmentBottom,
} VerticalAlignment;
@interface UpLineLable : UILabel
{
@private
    VerticalAlignment _verticalAlignment;
}

@property (nonatomic) VerticalAlignment verticalAlignment;

@end
