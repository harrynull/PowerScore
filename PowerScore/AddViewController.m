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

@property (weak, nonatomic) IBOutlet UIView *contain;
@property (weak, nonatomic) IBOutlet UIView *memcontain;
@property (weak, nonatomic) IBOutlet UIView *uk1;
@property (weak, nonatomic) IBOutlet UIView *uk2;
@property (weak, nonatomic) IBOutlet UIView *uk3;
@property (weak, nonatomic) IBOutlet UIView *uk4;
@property (weak, nonatomic) IBOutlet UIView *uk5;
@property (weak, nonatomic) IBOutlet UIView *ik;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *meg1bottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *meg2bottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *showmemsbottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *unsetablebottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *meg4bottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *membtbottom;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containheight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containbottom;

@property (nonatomic,strong) NSString *reasonadd;
@property CGFloat oldheight;

@end

@implementation AddViewController

bool isplus = true;
bool caninput = true;
bool onlyzero = true;
bool caninputpoint = true;
bool numisdragdown = false;

//判断箭头动画的
bool flag;

- (void)viewWillAppear:(BOOL)animated {
    self.navigationController.toolbarHidden = YES;
}

-(void)viewDidAppear:(BOOL)animated
{
    //判断并接收返回的理由&成员
    if (![self.reasonreceive isEqual:@""]&&![self.reasonreceive isEqual:@"&&**##NOREASONINPUT$#%"])
    {
        self.reasonadd = self.reasonreceive;
        self.showreason.text = self.reasonadd;
        [self turnReasonBlue];

    } else {
        self.showreason.text = @"理由";
        [self turnReasonWhite];
    }

    if(self.membersreceive!=nil&&![self.membersreceive isEqual:@""]){
        NSArray* mems=[self.membersreceive componentsSeparatedByString:@";"];
        NSString* allnames=@"";
        for(NSString* str in mems){
            if([str isEqual:@""]) continue;
            if(![allnames isEqual:@""]) allnames=[allnames stringByAppendingString:@","];
            allnames=[allnames stringByAppendingString: [[str componentsSeparatedByString:@","] objectAtIndex: 1]];
        }
        self.showmems.text=allnames;
        [self.unsetlable setText:@""];
    }
    //初始化小键盘原始高度便于计算
    self.oldheight = 291.0;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.btbackspace setImage:[UIImage imageNamed:@"backspacedown"] forState:UIControlStateHighlighted];
    [self.showmems setVerticalAlignment:VerticalAlignmentTop];
    [self.mem_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]] forState:UIControlStateHighlighted];
    [self.reason_bt setBackgroundImage:[self colorimg:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]] forState:UIControlStateHighlighted];
    [self.reason_bt setAdjustsImageWhenHighlighted:NO];
    self.reasonreceive = @"";
    [self.dropdownnum setAdjustsImageWhenHighlighted:NO];
    [self.dropdownnum setBackgroundImage:[UIImage imageNamed:@"dropback"] forState:UIControlStateHighlighted];
    flag = YES;
    numisdragdown = false;
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
    [self rotate];
    [self refreshview];
}



//小键盘伸缩
- (void)refreshview
{
    if (numisdragdown) {

        int h = self.oldheight-self.ik.frame.size.height;
        CGFloat addheight = h-self.oldheight+self.ik.frame.size.height;

        //通过改变约束值而不是frame避免调用view时重新加载导致复原
        self.containheight.constant += h;
        self.meg1bottom.constant += addheight;
        self.meg2bottom.constant += addheight;
        self.meg4bottom.constant += addheight;
        self.showmemsbottom.constant += addheight;
        self.unsetablebottom.constant += addheight;
        self.membtbottom.constant += addheight;

        self.uk1.hidden = NO;
        self.uk2.hidden = NO;
        self.uk3.hidden = NO;
        self.uk4.hidden = NO;
        self.uk5.hidden = NO;

        numisdragdown = false;
   
    } else {
 
        int h = self.oldheight-self.ik.frame.size.height;
        CGFloat addheight = h-self.oldheight+self.ik.frame.size.height;
        
        //通过改变约束值而不是frame避免调用view时重新加载导致复原
        self.containheight.constant -= h;
        self.meg1bottom.constant -= addheight;
        self.meg2bottom.constant -= addheight;
        self.meg4bottom.constant -= addheight;
        self.showmemsbottom.constant -= addheight;
        self.unsetablebottom.constant -= addheight;
        self.membtbottom.constant -= addheight;
        
        self.uk1.hidden = YES;
        self.uk2.hidden = YES;
        self.uk3.hidden = YES;
        self.uk4.hidden = YES;
        self.uk5.hidden = YES;
        
        numisdragdown = true;
        
    }
 
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
    CGRect rect = CGRectMake(0.0f, 0.0f, 1.0f, 1.0f);
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

- (void)rotate
{
    if (flag) {
        [UIView animateWithDuration:0.5 animations:^{
            self.dropdownnum.transform = CGAffineTransformMakeRotation(M_PI);
        } completion:^(BOOL finished) {
            flag = NO;
        }];
    }
    else {
        [UIView animateWithDuration:0.5 animations:^{
            self.dropdownnum.transform = CGAffineTransformMakeRotation(0);
        } completion:^(BOOL finished) {
            flag = YES;
        }];
    }
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
