//
//  LaunchViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 6/23/16.
//  Copyright © 2016 Gustav Wang. All rights reserved.
//

#import "LaunchViewController.h"
#import "ViewController.h"

@interface LaunchViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *spl1;
@property (weak, nonatomic) IBOutlet UIImageView *spl2;
@property (nonatomic,strong) NSString *plistPath;
@end

@implementation LaunchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor colorWithRed:0.0/255.0 green:136.0/255.0 blue:204.0/255.0 alpha:1];
    self.spl1.hidden = YES;
    self.spl2.hidden = YES;
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"firstLaunch"])
    {
                //第一次默认显示启动页
        [self creatplist];
        
    }
    
    [self loadplist];

}

- (void)viewDidAppear:(BOOL)animated
{
    NSThread *_thread1 = [[NSThread alloc]initWithTarget:self selector:@selector(threadOneMethod) object:nil];
    NSThread *_thread2 = [[NSThread alloc]initWithTarget:self selector:@selector(threadTwoMethod) object:nil];
    NSMutableDictionary *reasondic = [[NSMutableDictionary alloc] initWithContentsOfFile: _plistPath];
    NSString *load_or_not = [reasondic objectForKey:@"key"];
    
    if([load_or_not isEqualToString:@"NO"])
        
    {
        [self presentViewController:[self.storyboard instantiateViewControllerWithIdentifier:@"NavController"] animated:YES completion:nil];
    } else {
        //显示启动页
        [_thread1 start];
        [_thread2 start];
    }
    
    


}
-(void)threadOneMethod{
    @synchronized(@"lock"){
        self.spl1.hidden = NO;
        self.spl2.hidden = NO;
    }
}
-(void)threadTwoMethod{
    @synchronized(@"lock"){
    //延时
    [NSThread sleepForTimeInterval:0.4f];
    //打开主界面
        
    [self presentViewController:[self.storyboard instantiateViewControllerWithIdentifier:@"NavController"] animated:YES completion:nil];
    }
}
- (void)creatplist
{
    NSArray *paths =NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    self.plistPath = [documentsDirectory stringByAppendingPathComponent:@"Launch_or_not.plist"];
    NSMutableDictionary *dictplist = [[NSMutableDictionary alloc ] init];
    //设置属性值
    [dictplist setObject:@"YES" forKey:@"key"];
    //写入文件
    [dictplist writeToFile:_plistPath atomically:YES];

}

-(void)loadplist
{
    NSArray *paths =NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    self.plistPath = [documentsDirectory stringByAppendingPathComponent:@"Launch_or_not.plist"];
    
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
