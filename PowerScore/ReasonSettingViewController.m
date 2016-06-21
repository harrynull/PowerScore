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
@property (weak, nonatomic) IBOutlet UITableView *tableview;
@end

@implementation ReasonSettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
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
        
        NSString *path1 = [[NSBundle mainBundle] pathForResource:@"Reasons" ofType:@"plist"];
        NSMutableArray *newarray = [[NSMutableArray alloc] initWithContentsOfFile:path1];
        [newarray addObject:self.newreason];
        [newarray writeToFile:path1 atomically:YES];
        [self.tableview reloadData];
        
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:nil];
    }];
    okAction.enabled = NO;
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertcontroller addAction:okAction];
    [alertcontroller addAction:cancelAction];
    [self presentViewController:alertcontroller animated:YES completion:nil];
}

-(void)alertTextFieldDidChange:(NSNotification *)notificton{
    UIAlertController *alertController = (UIAlertController *)self.presentedViewController;
    if(alertController){
        UITextField *reason = alertController.textFields.firstObject;
        UIAlertAction *okAction = alertController.actions.firstObject;
        okAction.enabled = reason.text.length > 0;
    }
}
- (NSMutableArray*)getplistarray
{
    NSString *plistpath = [[NSBundle mainBundle] pathForResource:@"Reasons" ofType:@"plist"];
    NSMutableArray *reasonarray = [[NSMutableArray alloc] initWithContentsOfFile:plistpath];
    return reasonarray;
}
-(NSUInteger)getplistcount
{
    int plistcount;
    NSString *plistpath = [[NSBundle mainBundle] pathForResource:@"Reasons" ofType:@"plist"];
    NSMutableArray *reasonarray = [[NSMutableArray alloc] initWithContentsOfFile:plistpath];
    plistcount = reasonarray.count;
    return plistcount;
}

- (NSUInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSUInteger)section
{
    int count = [self getplistcount];
    
    
    return count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *ID = @"ID";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ID];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:ID];
    }
    
    NSUInteger row = [indexPath row];
    NSArray *reasons = [self getplistarray];
    cell.textLabel.text = [reasons objectAtIndex:row];
    return cell;
}

@end
