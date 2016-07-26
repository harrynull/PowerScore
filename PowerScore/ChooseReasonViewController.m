//
//  ChooseReasonViewController.m
//  PowerScore
//
//  Created by Gustav Wang on 16/5/8.
//  Copyright © 2016年 Gustav Wang. All rights reserved.
//

#import "ChooseReasonViewController.h"
#import "AddViewController.h"
#import "PowerScore-Swift.h"
@interface ChooseReasonViewController ()

@property (weak, nonatomic) IBOutlet UITextField *reasoninputlable;
@end

@implementation ChooseReasonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (NSUInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSUInteger)section
{
    return GlobalData.reasons.count;
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
    cell.textLabel.text = [GlobalData.reasons objectAtIndex:[indexPath row]];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.reasoninputlable setText:[GlobalData.reasons objectAtIndex:[indexPath row]]];
}
- (IBAction)okbtOnClick:(id)sender {
    AddViewController *receive = [self.navigationController.viewControllers objectAtIndex:self.navigationController.viewControllers.count-2];
    receive.reasonreceive = [self.reasoninputlable.text isEqual: @""]?@"&&**##NOREASONINPUT$#%":self.reasoninputlable.text;
    //使用popToViewController返回并传值到上一页面
    [self.navigationController popToViewController:receive animated:true];
}

@end
