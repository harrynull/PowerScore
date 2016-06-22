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
        NSString *path = [self getplistpath];
        NSMutableDictionary *newdic = [[[NSMutableDictionary alloc]initWithContentsOfFile:path]mutableCopy];
        NSInteger totalnum = [self getplistcount]+1;
        NSString *total = [NSString stringWithFormat:@"%ld",(long)totalnum];
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
    NSUInteger row = [indexPath row];
    NSArray *reasonkeys = [self getplistdic].allKeys;
    NSArray *reasons = [self getplistdic].allValues;
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:[reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])]message:@"选择对该记录的操作" preferredStyle: UIAlertControllerStyleActionSheet];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    UIAlertAction *deleteAction = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDestructive handler:^(UIAlertAction *action){
        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:[reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])] message:@"确认要删除这个理由吗？" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"是的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            
            NSString *path = [self getplistpath];
            NSMutableDictionary *newdic = [[[NSMutableDictionary alloc]initWithContentsOfFile:path]mutableCopy];
            
            //删除选中记录
            [newdic removeObjectForKey:[NSString stringWithFormat:@"%ld",indexPath.row+1]];
            
            
            //如果是倒数第二个，改变最后一个key，删除多余值
            if(indexPath.row+1 == newdic.allValues.count)
            {
                [newdic setObject:[newdic objectForKey:[NSString stringWithFormat:@"%lu",newdic.allValues.count+1]] forKey:[NSString stringWithFormat:@"%lu",(unsigned long)newdic.allValues.count]];
                [newdic removeObjectForKey:[NSString stringWithFormat:@"%lu",(unsigned long)newdic.allValues.count]];
            } else {
                //不是倒数第二个或最后一个，改变后方所有key，删除多余值
                for(long i = indexPath.row+1;i<newdic.allValues.count;++i){
                    [newdic setObject:[newdic objectForKey:[NSString stringWithFormat:@"%ld",i+1]] forKey:[NSString stringWithFormat:@"%ld",i]];
                }
                if(indexPath.row!=newdic.allValues.count)
                {
                    [newdic removeObjectForKey:[NSString stringWithFormat:@"%lu",(unsigned long)newdic.allValues.count]];
                }
                
            }
            
            [newdic writeToFile:[self getplistpath] atomically:YES];
            [self.tableview reloadData];
            
        }];
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertcontroller addAction:cancelAction];
        [alertcontroller addAction:okAction];
        [self presentViewController:alertcontroller animated:YES completion:nil];
        
        
        
    }];
    UIAlertAction *resetAction = [UIAlertAction actionWithTitle:@"修改" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:[reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])] message:@"修改该理由" preferredStyle:UIAlertControllerStyleAlert];
        [alertcontroller addTextFieldWithConfigurationHandler:^(UITextField *inputreason){
            inputreason.text = [reasons objectAtIndex:([reasonkeys indexOfObject:([NSString stringWithFormat:@"%ld",(long)row+1])])];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(alertTextFieldDidChange:) name:UITextFieldTextDidChangeNotification object:inputreason];
        }];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            UITextField *reason = alertcontroller.textFields.firstObject;
            self.newreason = reason.text;
            NSString *path = [self getplistpath];
            NSMutableDictionary *newdic = [[[NSMutableDictionary alloc]initWithContentsOfFile:path]mutableCopy];
            [newdic writeToFile:[self getplistpath] atomically:YES];
            [newdic setObject:[self newreason] forKey:[NSString stringWithFormat:@"%d",indexPath.row+1]];
            [newdic writeToFile:[self getplistpath] atomically:YES];
            [self.tableview reloadData];
            
            [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:nil];
        }];
        okAction.enabled = NO;
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertcontroller addAction:cancelAction];
        [alertcontroller addAction:okAction];
        [self presentViewController:alertcontroller animated:YES completion:nil];
        
        
        
    }];
    [alertController addAction:cancelAction];
    [alertController addAction:deleteAction];
    [alertController addAction:resetAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

@end
