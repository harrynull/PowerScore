//
//  ChooseMemTableViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/4.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ChooseMemTableViewController.h"

#import "HeaderView.h"
#import "ChooseRoleCell.h"
#import "PowerScore-Swift.h"
#import "AppDelegate.h"
static NSString * const ReuseIdentifierHeader = @"header";
static NSString * const ReuseIdentifierCell = @"dcell";

@interface ChooseMemTableViewController ()

@property (nonatomic, strong) NSDictionary<NSString *, NSArray<NSString *> *> *dataDic;
@property (nonatomic, strong) NSArray             *dataArray;

@property (nonatomic, strong) NSMutableArray      *expendArray;//记录打开的分组
@property (nonatomic, strong) NSMutableArray      *selectArray;//记录选择的所有选项

@end

@implementation ChooseMemTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.navigationController.toolbarHidden = YES;
    
    [self.navigationController.toolbar setBarTintColor:[UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    
    self.dataDic=[MemberChooseHelper getData:GlobalData.classes];
    self.dataArray = [self.dataDic allKeys];
    
    self.tableView.tableFooterView = [UIView new];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([HeaderView class]) bundle:nil] forHeaderFooterViewReuseIdentifier:ReuseIdentifierHeader];
    
    [self.tableView setTableFooterView:[[UIView alloc] initWithFrame:CGRectZero]];
    
}


- (IBAction)okAction:(id)sender {
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
    view.selectButton.tag = section;
    
    
    UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(0, -2, self.view.bounds.size.width, 0.3)];
    lab.backgroundColor = [UIColor blackColor];
    lab.alpha = 0.2;
    [view addSubview:lab];
    
    BOOL selectAll = YES;
    for (id object in array) {
        if (![self.selectArray containsObject:object]) {
            selectAll = NO;
        }
    }
    view.selectButton.selected = selectAll;
    [view.selectButton addTarget:self action:@selector(headerButtonOnClick:) forControlEvents:UIControlEventTouchUpInside];
    
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
    
    ChooseRoleCell *cell = [tableView dequeueReusableCellWithIdentifier:ReuseIdentifierCell forIndexPath:indexPath];
    cell.accessoryType = UITableViewCellAccessoryCheckmark;    // regular chevron. doesn't track
    //    UITableViewCellAccessoryDetailDisclosureButton, // blue button w/ chevron. tracks
    //    UITableViewCellAccessoryCheckmark
    if ([self.selectArray containsObject:name]) {
        cell.selectImageVIew.selected = YES;
    }else {
        cell.selectImageVIew.selected = NO;
    }
    //cell.backgroundColor = [UIColor groupTableViewBackgroundColor];
    cell.nameLabel.text = name;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString *key = self.dataArray[indexPath.section];
    NSArray *array = self.dataDic[key];
    NSString *name = array[indexPath.row];
    
    if ([self.selectArray containsObject:name]) {
        [self.selectArray removeObject:name];
    }else {
        [self.selectArray addObject:name];
    }
    
    [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:indexPath.section] withRowAnimation:UITableViewRowAnimationNone];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

}


#pragma mark - private methods

- (void)headerButtonOnClick:(UIButton *)button {
    NSString *key = self.dataArray[button.tag];
    NSArray *array = self.dataDic[key];
    
    if (button.selected) {
        [self.selectArray removeObjectsInArray:array];
    }else {
        [self.selectArray addObjectsFromArray:array];
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

- (NSDictionary<NSString *, NSArray<NSString *> *> *)dataDic {
    if (!_dataDic) {
        _dataDic = [NSMutableDictionary dictionaryWithCapacity:0];
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
