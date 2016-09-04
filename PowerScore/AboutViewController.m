//
//  AboutViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/3.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "AboutViewController.h"

@interface AboutViewController ()
@property (weak, nonatomic) IBOutlet UIButton *click_back;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *checking_sign;
@property (weak, nonatomic) IBOutlet UILabel *show_version;

@end

@implementation AboutViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initUI];
    

}

-(void)initUI
{
    [self.click_back setBackgroundImage:[UIImage imageNamed:@"grey"] forState:UIControlStateHighlighted];
    
    NSString *version;
    version = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
    [self.show_version setText:[NSString stringWithFormat:@"v%@",version]];
    
    [self.checking_sign setHidesWhenStopped:YES];

}

-(NSString *)check_version
{
    [self.checking_sign startAnimating];
    NSString *version_back;
    
    //$检查新版本

    [self.checking_sign stopAnimating];
    return version_back;
}

- (IBAction)checkversion_Onclick:(id)sender
{
    NSString *new_version = [self check_version];
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
