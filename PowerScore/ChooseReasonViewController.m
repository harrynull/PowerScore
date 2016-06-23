//
//  ChooseReasonViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/8.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ChooseReasonViewController.h"
#import "AddViewController.h"

@interface ChooseReasonViewController ()

@property (weak, nonatomic) IBOutlet UITextField *reasoninputlable;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *okbt;
@property (nonatomic,strong) NSString *newreason;
@property (nonatomic,strong) NSString *plistPath;
@property (nonatomic,strong) NSString *reasonback;
@end

@implementation ChooseReasonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self creatplist];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)creatplist
{
    NSArray *paths =NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    self.plistPath = [documentsDirectory stringByAppendingPathComponent:@"Reasons.plist"];
    
}

- (NSMutableDictionary*)getplistdic
{
    NSMutableDictionary *reasonadic = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getplistpath]];
    return reasonadic;
}
- (NSString*)getplistpath
{
    return _plistPath;
}

-(NSUInteger)getplistcount
{
    int plistcount;
    NSMutableDictionary *reasondic = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getplistpath]];
    plistcount = reasondic.allKeys.count;
    return plistcount;
}

- (NSUInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSUInteger)section
{
    int count = [self getplistcount];
    return count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowInSection:(NSUInteger)section {
    return 50;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *ID = @"ID";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ID];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:ID];
    }
    
    NSUInteger row = [indexPath row];
    NSArray *reasonkeys = [self getplistdic].allKeys;
    NSArray *reasons = [self getplistdic].allValues;
    cell.textLabel.text = [reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSUInteger row = [indexPath row];
    NSArray *reasonkeys = [self getplistdic].allKeys;
    NSArray *reasons = [self getplistdic].allValues;
    NSString *choosedreason = [reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])];
    [self.reasoninputlable setText:choosedreason];
}
- (IBAction)okbtOnClick:(id)sender {
    if(![self.reasoninputlable.text isEqual: @""])
    {
        
        //初始化返回值为理由
        self.reasonback = self.reasoninputlable.text;
        AddViewController *receive = [self.navigationController.viewControllers objectAtIndex:self.navigationController.viewControllers.count-2];
        receive.reasonreceive = self.reasonback;
        //使用popToViewController返回并传值到上一页面
        [self.navigationController popToViewController:receive animated:true];
        
    } else {
        
        //初始化返回值为空
        self.reasonback = @"&&**##NOREASONINPUT$#%";
        AddViewController *receive = [self.navigationController.viewControllers objectAtIndex:self.navigationController.viewControllers.count-2];
        receive.reasonreceive = self.reasonback;
        //使用popToViewController返回并传值到上一页面
        [self.navigationController popToViewController:receive animated:true];

        
    }
}

@end
