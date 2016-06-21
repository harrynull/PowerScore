//
//  ReasonSettingViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/6/7.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ReasonSettingViewController.h"

@interface ReasonSettingViewController ()
@property (nonatomic,strong) NSString *newreason;
@property (nonatomic,strong) NSString *plistPath;
@property (weak, nonatomic) IBOutlet UITableView *tableview;
@end

@implementation ReasonSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self creatplist];
}

-(void)creatplist
{
        NSArray *paths =NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        self.plistPath = [documentsDirectory stringByAppendingPathComponent:@"Reasons.plist"];
 
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)addreasonOnClick:(id)sender {
    UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:@"添加理由" message:@"添加您常用的理由" preferredStyle:UIAlertControllerStyleAlert];
    [alertcontroller addTextFieldWithConfigurationHandler:^(UITextField *inputreason){
        inputreason.placeholder = @"键入新理由";
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(alertTextFieldDidChange:) name:UITextFieldTextDidChangeNotification object:inputreason];
    }];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        UITextField *reason = alertcontroller.textFields.firstObject;
        self.newreason = reason.text;
        NSLog(self.newreason);
        NSString *path = [self getplistpath];
        NSMutableDictionary *newdic = [[[NSMutableDictionary alloc]initWithContentsOfFile:path]mutableCopy];
        NSInteger totalnum = [self getplistcount]+1;
        NSString *total = [NSString stringWithFormat:@"%ld",(long)totalnum];
        NSLog(total);
        [newdic setObject:self.newreason forKey:total];
        [newdic writeToFile:[self getplistpath] atomically:YES];
        
        [self.tableview reloadData];
        
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:nil];
    }];
    okAction.enabled = NO;
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    
    [alertcontroller addAction:cancelAction];
    [alertcontroller addAction:okAction];
    [self presentViewController:alertcontroller animated:YES completion:nil];
}

-(void)alertTextFieldDidChange:(NSNotification *)notificton{
    UIAlertController *alertController = (UIAlertController *)self.presentedViewController;
    if(alertController){
        UITextField *reason = alertController.textFields.firstObject;
        UIAlertAction *okAction = alertController.actions.lastObject;
        okAction.enabled = reason.text.length > 0;
    }
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

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
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
    NSString *choosedreason = [[self getplistdic].allValues objectAtIndex:[indexPath row]];
    NSLog(choosedreason);
}

@end
