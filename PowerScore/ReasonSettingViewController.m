//
//  ReasonSettingViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/6/7.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ReasonSettingViewController.h"
#import "PowerScore-Swift.h"

@interface ReasonSettingViewController ()
@property (weak, nonatomic) IBOutlet UITableView *tableview;
@end

@implementation ReasonSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (IBAction)addreasonOnClick:(id)sender {
    UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:@"添加理由" message:@"添加您常用的理由" preferredStyle:UIAlertControllerStyleAlert];
    [alertcontroller addTextFieldWithConfigurationHandler:^(UITextField *inputreason){
        inputreason.placeholder = @"键入新理由";
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(alertTextFieldDidChange:) name:UITextFieldTextDidChangeNotification object:inputreason];
    }];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        UITextField *reason = alertcontroller.textFields.firstObject;
        GlobalData.reasons=[GlobalData.reasons arrayByAddingObject:reason.text];
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

- (NSUInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSUInteger)section
{
    return GlobalData.reasons.count;
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
    cell.textLabel.text = [GlobalData.reasons objectAtIndex:indexPath.row];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString* reason=[GlobalData.reasons objectAtIndex:indexPath.row];
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle: reason message:@"选择对该理由的操作" preferredStyle: UIAlertControllerStyleActionSheet];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    UIAlertAction *deleteAction = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDestructive handler:^(UIAlertAction *action){
        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:reason message:@"确认要删除这个理由吗？" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"是的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            
            //删除选中记录
            NSMutableArray* newreasons=[NSMutableArray arrayWithArray: GlobalData.reasons];
            [newreasons removeObjectAtIndex:indexPath.row];
            GlobalData.reasons=newreasons;
            
            [self.tableview reloadData];
            
        }];
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertcontroller addAction:cancelAction];
        [alertcontroller addAction:okAction];
        [self presentViewController:alertcontroller animated:YES completion:nil];
    }];
    UIAlertAction *resetAction = [UIAlertAction actionWithTitle:@"修改" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        NSString* reason=[GlobalData.reasons objectAtIndex:indexPath.row];
        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:reason message:@"修改该理由" preferredStyle:UIAlertControllerStyleAlert];
        [alertcontroller addTextFieldWithConfigurationHandler:^(UITextField *inputreason){
            inputreason.text = reason;
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(alertTextFieldDidChange:) name:UITextFieldTextDidChangeNotification object:inputreason];
        }];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            UITextField *reason = alertcontroller.textFields.firstObject;
            
            NSMutableArray* newreasons=[NSMutableArray arrayWithArray: GlobalData.reasons];
            [newreasons replaceObjectAtIndex:indexPath.row withObject:reason.text];
            GlobalData.reasons=newreasons;
            
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
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

@end
