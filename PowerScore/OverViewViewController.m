//
//  OverViewViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/6/7.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "OverViewViewController.h"
#import "HeaderView.h"
#import "ChooseRoleCell.h"
#import "PowerScore-Swift.h"
#import "AppDelegate.h"
#import "AddViewController.h"
static NSString * const ReuseIdentifierHeader = @"header";
static NSString * const ReuseIdentifierCell = @"dcell";

@interface OverViewViewController ()

@property (nonatomic, strong) NSDictionary        *dataDic;
@property (nonatomic, strong) NSArray             *dataArray;

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) NSMutableArray      *expendArray;//记录打开的分组
@property (nonatomic, strong) NSMutableArray      *selectArray;//记录选择的所有选项

@end

@implementation OverViewViewController

NSString *score_str;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.dataDic=[MemberChooseHelper getData:[GlobalData classes]];
    self.dataArray = [self.dataDic allKeys];
    
    self.tableView.tableFooterView = [UIView new];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([HeaderView class]) bundle:nil] forHeaderFooterViewReuseIdentifier:ReuseIdentifierHeader];
    
    [self.tableView setTableFooterView:[[UIView alloc] initWithFrame:CGRectZero]];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source
#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableVie
{
    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSString *key = self.dataArray[section];
    NSArray *array = self.dataDic[key];
    
    if ([self.expendArray containsObject:key]) {
        return array.count;
    }else {
        return 0.5;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 44;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 2;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    NSString *key = self.dataArray[section];
    NSArray *array = self.dataDic[key];
    HeaderView *view = [tableView dequeueReusableHeaderFooterViewWithIdentifier:ReuseIdentifierHeader];
    view.nameLabel.text = key;
    
   // view.selectButton.tag = section;
    
    
    UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(0, -2, self.view.bounds.size.width, 0.3)];
    lab.backgroundColor = [UIColor blackColor];
    lab.alpha = 0.2;
    [view addSubview:lab];
    
    BOOL selectAll = YES;
    for (NSString* object in array) {
        if (![self.selectArray containsObject:[NSString stringWithFormat:@"%@,%@", key, object]]) {
            selectAll = NO;
        }
    }
    view.selectButton.hidden = YES;
    //[view.selectButton addTarget:self action:@selector(headerButtonOnClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [view.tap addTarget:self action:@selector(headerTap:)];
    
    if ([self.expendArray containsObject:key]) {
        [view.jiantouInageView setImage:[UIImage imageNamed:@"upsig"]];
    }else {
        //view.jiantouInageView.transform = CGAffineTransformIdentity;
    }
    return view;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString *key = self.dataArray[indexPath.section];
    NSArray *array = self.dataDic[key];    
    NSString *name = array[indexPath.row];
    

    //NSString *mark = scorearray[indexPath.row];
    
    ChooseRoleCell *cell = [tableView dequeueReusableCellWithIdentifier:ReuseIdentifierCell forIndexPath:indexPath];
    cell.selectImageVIew.selected = [self.selectArray containsObject:[NSString stringWithFormat:@"%@,%@", key, name]];
    cell.nameLabel.text = name;
    
    for(NSString* cid in GlobalData.classes)
    {
        ClassData *c = GlobalData.classes[cid];
        NSNumber *score = [c.scores objectAtIndex:[c.members indexOfObject:array[indexPath.row]]];
        cell.markLable.text = [NSString stringWithFormat:@"%@",score];
        
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    /*
    NSString *key = self.dataArray[indexPath.section];
    NSArray *array = self.dataDic[key];
    NSString *name = array[indexPath.row];
    NSString *keyname=[NSString stringWithFormat:@"%@,%@", key, name];
    if ([self.selectArray containsObject:keyname]) {
        [self.selectArray removeObject:keyname];
    }else {
        [self.selectArray addObject:keyname];
    }
    [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:indexPath.section] withRowAnimation:UITableViewRowAnimationNone];
     */
    NSString *key = self.dataArray[indexPath.section];
    NSArray *array = self.dataDic[key];
    NSString *name = array[indexPath.row];
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:name message:@"" preferredStyle: UIAlertControllerStyleActionSheet];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    UIAlertAction *deleteAction = [UIAlertAction actionWithTitle:@"修改分数" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        for(NSString* cid in GlobalData.classes)
        {
            ClassData *c = GlobalData.classes[cid];
            NSNumber *score = [c.scores objectAtIndex:[c.members indexOfObject:array[indexPath.row]]];
            score_str = [NSString stringWithFormat:@"%@",score];
        }

        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:@"请设置分数" message:@"" preferredStyle:UIAlertControllerStyleAlert];
        [alertcontroller addTextFieldWithConfigurationHandler:^(UITextField *inputreason){
            inputreason.text = score_str;
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(alertTextFieldDidChange:) name:UITextFieldTextDidChangeNotification object:inputreason];
        }];
        
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            UITextField *input = alertcontroller.textFields.firstObject;
            double newScore = [input.text doubleValue];
            
            //$直接修改分数,变量newScore为新输入的分数
            
            //[self.tableview reloadData];
            
            [[NSNotificationCenter defaultCenter] removeObserver:self name:UITextFieldTextDidChangeNotification object:nil];
        }];
        
        okAction.enabled = NO;
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertcontroller addAction:cancelAction];
        [alertcontroller addAction:okAction];
        [self presentViewController:alertcontroller animated:YES completion:nil];
        
    }];
    
    UIAlertAction *moreinfoAction = [UIAlertAction actionWithTitle:@"查看记录" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //$查看选中学生的记录
        
    }];
    
    [alertController addAction:cancelAction];
    [alertController addAction:deleteAction];
    [alertController addAction:moreinfoAction];
    [self presentViewController:alertController animated:YES completion:nil];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

}

-(void)alertTextFieldDidChange:(NSNotification *)notificton{
    UIAlertController *alertController = (UIAlertController *)self.presentedViewController;
    if(alertController){
        UITextField *reason = alertController.textFields.firstObject;
        UIAlertAction *okAction = alertController.actions.lastObject;
        okAction.enabled = reason.text.length > 0 && reason.text != score_str;
    }
}

#pragma mark - private methods

- (void)headerButtonOnClick:(UIButton *)button {
    NSString *key = self.dataArray[button.tag];
    NSArray *array = self.dataDic[key];
    
    for (NSString* name in array) {
        NSString *keyname=[NSString stringWithFormat:@"%@,%@", key, name];
        if (button.selected) {
            [self.selectArray removeObject:keyname];
        }else {
            [self.selectArray addObject:keyname];
        }
    }
    
    button.selected = !button.selected;
    
    [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:button.tag] withRowAnimation:UITableViewRowAnimationNone];
}

- (void)headerTap:(UITapGestureRecognizer *)tap {
    HeaderView *view = (HeaderView *)tap.view;
    NSString *key = view.nameLabel.text;
    NSInteger index = [self.dataArray indexOfObject:key];
    
    if ([self.expendArray containsObject:key]) {
        [self.expendArray removeObject:key];
        
    }else {
        [self.expendArray addObject:key];
        [view.jiantouInageView setImage:[UIImage imageNamed:@"downsig"]];
    }
    
    [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:index] withRowAnimation:UITableViewRowAnimationNone];
}


#pragma mark - getters

- (NSDictionary *)dataDic {
    if (!_dataDic) {
        _dataDic = [NSDictionary dictionary];
    }
    return _dataDic;
}

- (NSMutableArray *)expendArray {
    if (!_expendArray) {
        _expendArray = [NSMutableArray arrayWithCapacity:0];
    }
    return _expendArray;
}

- (NSMutableArray *)selectArray {
    if (!_selectArray) {
        _selectArray = [NSMutableArray arrayWithCapacity:0];
    }
    return _selectArray;
}

@end
