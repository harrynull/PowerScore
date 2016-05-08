//
//  AddViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/3.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "AddViewController.h"
#import "UpLineLable.h"

@interface AddViewController ()


@property (weak, nonatomic) IBOutlet UIButton *plus_bt;
@property (weak, nonatomic) IBOutlet UIButton *minus_bt;

@property (weak, nonatomic) IBOutlet UIButton *bt1;
@property (weak, nonatomic) IBOutlet UIButton *bt2;
@property (weak, nonatomic) IBOutlet UIButton *bt3;
@property (weak, nonatomic) IBOutlet UIButton *bt4;
@property (weak, nonatomic) IBOutlet UIButton *bt5;
@property (weak, nonatomic) IBOutlet UIButton *bt6;
@property (weak, nonatomic) IBOutlet UIButton *bt7;
@property (weak, nonatomic) IBOutlet UIButton *bt8;
@property (weak, nonatomic) IBOutlet UIButton *bt9;
@property (weak, nonatomic) IBOutlet UIButton *bt0;
@property (weak, nonatomic) IBOutlet UIButton *btdoc;
@property (weak, nonatomic) IBOutlet UIButton *btbackspace;

@property (weak, nonatomic) IBOutlet UILabel *showmark;

@property (weak, nonatomic) IBOutlet UpLineLable *showmems;
@property (weak, nonatomic) IBOutlet UIButton *mem_bt;
@property (weak, nonatomic) IBOutlet UIButton *reason_bt;

@end

@implementation AddViewController

bool isplus = true;
bool caninput = true;
bool onlyzero = true;
bool caninputpoint = true;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.btbackspace setImage:[UIImage imageNamed:@"backspacedown"] forState:UIControlStateHighlighted];
    [self.showmems setVerticalAlignment:VerticalAlignmentTop];
    [self.mem_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]] forState:UIControlStateHighlighted];
    [self.reason_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]] forState:UIControlStateHighlighted];
    
    // Do any additional setup after loading the view.
}
- (IBAction)plusOnclick:(id)sender {
    [self.plus_bt setImage:[UIImage imageNamed:@"plus_on"] forState:UIControlStateNormal];
    [self.minus_bt setImage:[UIImage imageNamed:@"minus_off"] forState:UIControlStateNormal];
    isplus = true;
}
- (IBAction)minusOnclick:(id)sender {
    [self.plus_bt setImage:[UIImage imageNamed:@"plus_off"] forState:UIControlStateNormal];
    [self.minus_bt setImage:[UIImage imageNamed:@"minus_on"] forState:UIControlStateNormal];
    isplus = false;
}
- (IBAction)membtOnclick:(id)sender {
    [self performSegueWithIdentifier:@"gotomem" sender:self];
}

- (IBAction)reasonOnclick:(id)sender {
}

- (IBAction)bt1Onclick:(id)sender {
    [self keyboardcallback:@"1"];
    
}
- (IBAction)bt2Onclick:(id)sender {
    [self keyboardcallback:@"2"];
}
- (IBAction)bt3Onclick:(id)sender {
    [self keyboardcallback:@"3"];
}
- (IBAction)bt4Onclick:(id)sender {
    [self keyboardcallback:@"4"];
}
- (IBAction)bt5Onclick:(id)sender {
    [self keyboardcallback:@"5"];
}
- (IBAction)bt6Onclick:(id)sender {
    [self keyboardcallback:@"6"];
}
- (IBAction)bt7Onclick:(id)sender {
    [self keyboardcallback:@"7"];
}
- (IBAction)bt8Onclick:(id)sender {
    [self keyboardcallback:@"8"];
}
- (IBAction)bt9Onclick:(id)sender {
    [self keyboardcallback:@"9"];
}
- (IBAction)bt0Onclick:(id)sender {
    if (caninput)
    {
        if (onlyzero)
        {
            [self.showmark setText:@"0"];
            onlyzero = true;
            caninputpoint = true;
        } else {
            [self.showmark setText:[NSString stringWithFormat:@"%@%@",self.showmark.text,@"0"]];
        }
        NSRange range;
        range = [self.showmark.text rangeOfString:@"."];
        if (range.location != NSNotFound) {
            
            if (self.showmark.text.length - 1 - range.location > 0)
            {
                caninput = false;
            }
        }
    }
}
- (IBAction)btdocOnclick:(id)sender {
    if (caninput&&caninputpoint)
    {
        [self.showmark setText:[NSString stringWithFormat:@"%@%@",self.showmark.text,@"."]];
        onlyzero = false;
        caninputpoint = false;
    }
}
- (IBAction)btbackspaceOnclick:(id)sender {
    if (self.showmark.text.length == 1) {
        [self.showmark setText:@"0"];
            onlyzero = true;
    } else {
        NSString *s = self.showmark.text;
        s = [s substringToIndex:self.showmark.text.length - 1];
        [self.showmark setText:s];
        NSRange range;
        range = [s rangeOfString:@"."];
        if (range.location == NSNotFound)
        {
            caninputpoint = true;
        }
        if ([s isEqualToString: @"0"])
        {
            NSLog(@"8700");
            onlyzero = true;
        }
    }
}

-(void)keyboardcallback:(NSString *)num
{
    
    if(caninput)
    {
        if (onlyzero)
        {
            [self.showmark setText:num];
            onlyzero = false;
        } else {
            [self.showmark setText:[NSString stringWithFormat:@"%@%@",self.showmark.text,num]];
        }
        NSRange range;
        range = [self.showmark.text rangeOfString:@"."];
        if (range.location != NSNotFound) {
            
            if (self.showmark.text.length - 1 - range.location > 0)
            {
                caninput = false;
                onlyzero = false;
            }
        }
    }
}

- (UIImage *)colorimg:(UIColor *)color
{
    CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
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
