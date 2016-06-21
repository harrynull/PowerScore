//
//  ChooseReasonViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/8.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ChooseReasonViewController.h"

@interface ChooseReasonViewController ()

@property (weak, nonatomic) IBOutlet UITextField *reasoninputlable;

@end

@implementation ChooseReasonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    NSArray *reasons = [self getplistarray];
    cell.textLabel.text = [reasons objectAtIndex:row];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *choosedreason = [[self getplistarray] objectAtIndex:[indexPath row]];
    [self.reasoninputlable setText:choosedreason];
}

@end
