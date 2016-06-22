//
//  AddViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/3.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "AddViewController.h"
#import "UpLineLable.h"
#import "ChooseReasonViewController.h"

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
@property (weak, nonatomic) IBOutlet UIButton *dropdownnum;

@property (weak, nonatomic) IBOutlet UILabel *showmark;
@property (weak, nonatomic) IBOutlet UILabel *showreason;

@property (weak, nonatomic) IBOutlet UpLineLable *showmems;
@property (weak, nonatomic) IBOutlet UILabel *memlable;
@property (weak, nonatomic) IBOutlet UILabel *unsetlable;
@property (weak, nonatomic) IBOutlet UIButton *mem_bt;
@property (weak, nonatomic) IBOutlet UIButton *reason_bt;

@property (weak, nonatomic) IBOutlet UIImageView *reg1;
@property (weak, nonatomic) IBOutlet UIImageView *reg2;
@property (weak, nonatomic) IBOutlet UIImageView *reg3;
@property (weak, nonatomic) IBOutlet UIImageView *reg4;
@property (weak, nonatomic) IBOutlet UIImageView *meg1;
@property (weak, nonatomic) IBOutlet UIImageView *meg2;
@property (weak, nonatomic) IBOutlet UIImageView *meg3;
@property (weak, nonatomic) IBOutlet UIImageView *meg4;

@property (nonatomic,strong) NSString *reasonadd;

@property (weak, nonatomic) IBOutlet UIView *contain;
@property (weak, nonatomic) IBOutlet UIView *memcontain;
@property (weak, nonatomic) IBOutlet UIView *uk1;
@property (weak, nonatomic) IBOutlet UIView *uk2;
@property (weak, nonatomic) IBOutlet UIView *uk3;
@property (weak, nonatomic) IBOutlet UIView *uk4;
@property (weak, nonatomic) IBOutlet UIView *uk5;
@property (weak, nonatomic) IBOutlet UIView *ik;



@end

@implementation AddViewController

bool isplus = true;
bool caninput = true;
bool onlyzero = true;
bool caninputpoint = true;

- (void)viewWillAppear:(BOOL)animated {
    self.navigationController.toolbarHidden = YES;
}

-(void)viewDidAppear:(BOOL)animated
{
    //判断并接收返回的理由&成员
    if (![self.reasonreceive isEqual:@""])
    {
        if (![self.reasonreceive isEqual:@"&&**##NOREASONINPUT$#%"])
        {
            
            self.reasonadd = self.reasonreceive;
            self.showreason.text = self.reasonadd;
            [self turnReasonBlue];
         
       
        } else {
            
            self.showreason.text = @"理由";
            [self turnReasonWhite];
       
            
        }
    } else {
        
        self.showreason.text = @"理由";
        [self turnReasonWhite];
       
        
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.btbackspace setImage:[UIImage imageNamed:@"backspacedown"] forState:UIControlStateHighlighted];
    [self.showmems setVerticalAlignment:VerticalAlignmentTop];
    [self.mem_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:0.1]] forState:UIControlStateHighlighted];
    [self.reason_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:0.1]] forState:UIControlStateHighlighted];
    [self.reason_bt setAdjustsImageWhenHighlighted:NO];
    self.reasonreceive = @"";
    [self.dropdownnum setAdjustsImageWhenHighlighted:NO];
    [self.dropdownnum setBackgroundImage:[UIImage imageNamed:@"dropback"] forState:UIControlStateHighlighted];

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
    [self performSegueWithIdentifier:@"gotochoosereason" sender:self];
}
- (IBAction)dropdownnumOnClick:(id)sender {
    [self refreshview];
}

- (void)refreshview
{
    [NSThread sleepForTimeInterval:0.1f];
    [self.uk1 removeFromSuperview];
    [self.uk2 removeFromSuperview];
    [self.uk3 removeFromSuperview];
    [self.uk4 removeFromSuperview];
    [self.uk5 removeFromSuperview];
    [NSThread sleepForTimeInterval:0.1f];
    CGRect rect = self.contain.frame;
    CGRect rect1 = self.memcontain.frame;
    CGRect rect2 = self.meg1.frame;
    CGRect rect3 = self.meg2.frame;
    CGRect rect4 = self.showmems.frame;
    CGRect rect5 = self.unsetlable.frame;
    CGRect rect6 = self.meg4.frame;
    [NSThread sleepForTimeInterval:0.1f];
    int h = rect.size.height-self.ik.frame.size.height;
    [NSThread sleepForTimeInterval:0.1f];
    rect.origin.y += h;
    rect6.origin.y += h;
    rect.size.height = self.ik.frame.size.height;
    [NSThread sleepForTimeInterval:0.1f];
    rect1.size.height += h;
    rect2.size.height += h;
    rect3.size.height += h;
    rect4.size.height += h;
    rect5.size.height += h;
    NSLog(@"%f",rect5.size.height);
    [NSThread sleepForTimeInterval:0.1f];
    self.memcontain.frame = rect1;
    self.contain.frame = rect;
    self.meg1.frame = rect2;
    self.meg2.frame = rect3;
    self.showmems.frame = rect4;
    self.unsetlable.frame = rect5;
    self.meg4.frame = rect6;
    NSLog(@"%f",self.unsetlable.frame.size.height);
    [NSThread sleepForTimeInterval:0.1f];
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
        caninputpoint = true;
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
            onlyzero = true;
        }
    }
    caninput = true;
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
    CGRect rect = CGRectMake(0.0f, 0.0f, 0.1f, 0.1f);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

- (void)turnReasonBlue
{
    [self.reg1 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.reg2 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.reg3 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.reg4 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.showreason setTextColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
}
- (void)turnReasonWhite
{
    [self.reg1 setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
    [self.reg2 setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
    [self.reg3 setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
    [self.reg4 setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
    [self.showreason setTextColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:1]];
}
- (void)turnMemberBlue
{
    [self.meg1 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.meg2 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.meg3 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.meg4 setBackgroundColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    [self.memlable setTextColor:[UIColor colorWithRed:20.0/255.0 green:192.0/255.0 blue:212.0/255.0 alpha:1]];
    self.unsetlable.hidden = YES;
    
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
