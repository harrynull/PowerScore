//
//  ViewController.m

#import "ViewController.h"
#import "AndyScrollView.h"
#import "RightScrollView.h"
#import "PowerScore-swift.h"
#import "AppDelegate.h"
#import "RightView.h"
#import "HZQDatePickerView.h"

// 屏幕尺寸 ScreenRect
#define ScreenRect [UIScreen mainScreen].applicationFrame
#define ScreenRectHeight [UIScreen mainScreen].applicationFrame.size.height
#define ScreenRectWidth [UIScreen mainScreen].applicationFrame.size.width

@interface ViewController ()
<UIScrollViewDelegate,UIGestureRecognizerDelegate,UITableViewDataSource,UITableViewDelegate>
{
    //UIPanGestureRecognizer *pan;

    __weak IBOutlet UIButton *flit;
    
    __weak IBOutlet UIButton *class_bt;

    __weak IBOutlet UIButton *person_bt;
    
    __weak IBOutlet UIButton *person_false;
    
    __weak IBOutlet UIButton *openleft_bt;
    
    __weak IBOutlet UILabel *toolback;
    
    __weak IBOutlet UILabel *movinglable;
    
    __weak IBOutlet UILabel *staylable;
    
    __weak IBOutlet UIButton *add_bt;
    
    __weak IBOutlet UITableView *tableview;
    
    __weak IBOutlet UIButton *class_or_student;
    
    HZQDatePickerView *_pikerView;
    
    bool isFilterDate;
    NSDate* filterDate;
    int filterScore; //-1 负分 0 不筛选 1正分
    
    NSArray<History*>* cachedHistories;
    bool cacheVaild;
}
@property(nonatomic,strong)AndyScrollView *scroll;
@property(nonatomic,strong)RightScrollView *rscroll;
@property(nonatomic,strong)RightView *rview;
@property(nonatomic,strong)UIView *leftView;
@property(nonatomic,strong)UIView *rightView;

@end

@implementation ViewController

bool ON_CLASS = YES;

-(NSArray<History*>*) getTableViewHistories{
    if(filterScore==0&&!isFilterDate){
        return [[GlobalData getClassNow] histories];
    }
    if(!cacheVaild){
        NSMutableArray<History*>* filteredHistories= [NSMutableArray<History*> array];
        for(History* h in [[GlobalData getClassNow] histories]){
            if(filterScore==-1&&h.score>0) continue;
            if(filterScore==1&&h.score<0) continue;
            if(isFilterDate&&h.nsDate!=filterDate) continue;
            [filteredHistories addObject:h];
        }
        cachedHistories=filteredHistories;
        cacheVaild=true;
        return filteredHistories;
    }else{
        return cachedHistories;
    }
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [[self getTableViewHistories] count];
}


-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{

    static NSString * identifier = @"HistoryIdentifier";
    UITableViewCell * cell;
    cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HistoryTableViewCell" owner:self options:nil];
    if ([nib count]>0)
    {
        self.HistoryCell = [nib objectAtIndex:0];
        cell = self.HistoryCell;
    }
    History *history = [[self getTableViewHistories] objectAtIndex:indexPath.row];
    UILabel *reason = (UILabel *)[cell.contentView viewWithTag:1];
    UILabel *date_short = (UILabel *)[cell.contentView viewWithTag:2];
    UILabel *mark = (UILabel *)[cell.contentView viewWithTag:3];
    UILabel *members = (UILabel *)[cell.contentView viewWithTag:4];
    UIImageView *positive = (UIImageView *)[cell.contentView viewWithTag:5];
    
    reason.text = history.reason;
    date_short.text = history.shortDateStr;
    mark.text = history.scoreWithSign;
    members.text = history.names;
    
    if(history.score<0)
    {
        [positive setBackgroundColor:[UIColor redColor]];
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //NSUInteger row = [indexPath row];
    History *history = [[self getTableViewHistories] objectAtIndex:indexPath.row];
    NSString *reason = history.reason;
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:reason message:@"选择对该记录的操作" preferredStyle: UIAlertControllerStyleActionSheet];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    UIAlertAction *deleteAction = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDestructive handler:^(UIAlertAction *action){
        
        NSString *tip = [NSString stringWithFormat:@"确认要删除记录“%@”吗？\n该条记录所修改的分数将被撤销",reason];
        
        UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:@"删除记录" message:tip preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"删除" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            
        //$删除记录
            
        }];
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertcontroller addAction:cancelAction];
        [alertcontroller addAction:okAction];
        [self presentViewController:alertcontroller animated:YES completion:nil];

    }];
    
    UIAlertAction *moreinfoAction = [UIAlertAction actionWithTitle:@"查看详细" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //$详细记录
      
    }];
    
    [alertController addAction:cancelAction];
    [alertController addAction:deleteAction];
    [alertController addAction:moreinfoAction];
    [self presentViewController:alertController animated:YES completion:nil];
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)viewWillAppear:(BOOL)animated {
    self.navigationController.toolbarHidden = YES;
    if (ON_CLASS)
    {
        movinglable.hidden = NO;
        staylable.hidden = YES;
    }
    else
    {
        movinglable.hidden = YES;
        staylable.hidden = NO;
    }
}


- (void)viewDidLoad {
    
    [super viewDidLoad];

    [self initUI];
    [self loadHistory];
    [self loadReasons];
}


-(void)loadReasons
{
    GlobalData.reasons = [DataManager loadReasons: @"上课发言,打扫卫生,小组加分,上午迟到,中午迟到,上课讲话,晚修讲话,随意下位,没有值日"];
}

-(void)loadHistory
{
    
    GlobalData.classes=[NSMutableDictionary dictionaryWithDictionary:[DataManager loadDataFromFiles]]; //先从文件读取
    
    if([GlobalData.classes count]!=0){ //判断是否读取成功
        [GlobalData setClassNow:@"21"];
        
        [class_or_student setTitle:[GlobalData getClassNow].name forState:UIControlStateNormal];
        [class_or_student setImage:[UIImage imageNamed:@"trangle"] forState:UIControlStateNormal];
        [class_or_student setTitleEdgeInsets:UIEdgeInsetsMake(0, -10, 0, 10)];
        [class_or_student setImageEdgeInsets:UIEdgeInsetsMake(0, class_or_student.frame.size.width-15, 0, -class_or_student.titleLabel.bounds.size.width-5)];
        
        return;
    }
    
    //读取失败 从网络获取
    NSString *username=@"tester", *password=@"123456";
    
    //获得改帐号所管理的班级
    NSArray *cids = [[ViewController post:@"http://powerscore.duapp.com/getclasses.php": [NSString stringWithFormat:@"username=%@&password=%@",username,password]] componentsSeparatedByString:@","];
    
    NSMutableDictionary* dic=[NSMutableDictionary dictionary];
    
    //获取每个班级的数据
    for (int i = 0; i < cids.count - 1; i += 2) {
        ClassData* c = [DataManager process:[ViewController post:@"http://powerscore.duapp.com/sync.php":[NSString stringWithFormat:@"username=%@&password=%@&cid=%@&diff=",username,password,cids[i]]] name:cids[i+1]];
        
        [c save:cids[i]];
        
        [dic setObject:c forKey:cids[i]];
    }
    
    GlobalData.classes=dic;
    [GlobalData setClassNow:@"21"];
    
    [class_or_student setTitle:[GlobalData getClassNow].name forState:UIControlStateNormal];
    [class_or_student setImage:[UIImage imageNamed:@"trangle"] forState:UIControlStateNormal];
    [class_or_student setTitleEdgeInsets:UIEdgeInsetsMake(0, -10, 0, 10)];
    [class_or_student setImageEdgeInsets:UIEdgeInsetsMake(0, class_or_student.frame.size.width-15, 0, -class_or_student.titleLabel.bounds.size.width-5)];
}


-(void)initUI
{
    
    [self.view addSubview:self.scroll];
    [self.view addSubview:self.rscroll];
    [add_bt setBackgroundImage:[UIImage imageNamed:@"add_press"] forState:UIControlStateHighlighted];
    self.navigationController.toolbarHidden = YES;
    [self.navigationController.navigationBar setBarTintColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor],NSForegroundColorAttributeName, nil]];
    
    [class_or_student setBackgroundImage:[UIImage imageNamed:@"light_blue"] forState:UIControlStateHighlighted];
    class_or_student.adjustsImageWhenHighlighted = NO;
    
    [toolback setBackgroundColor: [UIColor colorWithRed:0.0 green:114.0/255.0 blue:198.0/255.0 alpha:1.0]];
    [flit setBackgroundImage:[UIImage imageNamed:@"flitback"] forState:UIControlStateHighlighted];
    if ([self.navigationController.navigationBar respondsToSelector:@selector( setBackgroundImage:forBarMetrics:)]){
        NSArray *list=self.navigationController.navigationBar.subviews;
        for (id obj in list) {
            if ([obj isKindOfClass:[UIImageView class]]) {
                UIImageView *imageView=(UIImageView *)obj;
                NSArray *list2=imageView.subviews;
                for (id obj2 in list2) {
                    if ([obj2 isKindOfClass:[UIImageView class]]) {
                        UIImageView *imageView2=(UIImageView *)obj2;
                        imageView2.hidden=YES;
                    }
                }
            }
        }
    }
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopen:)name:@"toopen" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopenov:)name:@"toopenov" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopensr:)name:@"toopensr" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(toopensg:)name:@"toopensg" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(tounlog:)name:@"tounlog" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(tochangeplus:)name:@"tochangeplus" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(tosetdate:)name:@"tosetdate" object:nil];
    // self.navigationController.navigationBar.barStyle = UIBarStyleBlackTranslucent;
   
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}

-(void)toopen:(NSNotification *)sender
{
    [self vc_openabouteee];
}
-(void)toopenov:(NSNotification *)sender
{
    [self vc_openoverview];
}
-(void)toopensr:(NSNotification *)sender
{
    [self vc_opensetreason];
}
-(void)toopensg:(NSNotification *)sender
{
    [self vc_opensetgroup];
}
-(void)tounlog:(NSNotification *)sender
{
    [self vc_unlogger];
}
-(void)tochangeplus:(NSNotification *)sender
{
    [self chooseplussss];
}
-(void)tosetdate:(NSNotification *)sender
{
    [self setdateee];
}
-(void)vc_changelaunch:(BOOL *)launch
{
    NSMutableDictionary *dictplist = [[NSMutableDictionary alloc ] init];
    //设置属性值
    [dictplist setObject:launch?@"YES":@"NO" forKey:@"key"];
    //写入文件
    [dictplist writeToFile:@"settings.plist" atomically:YES];
    //NSMutableDictionary *reasondic = [[NSMutableDictionary alloc] initWithContentsOfFile: @"settings.plist"];
    //NSString *load_or_not = [reasondic objectForKey:@"key"];
}
- (IBAction)openleftOnclick:(id)sender {
    [self.leftView reloadInputViews];
    [self.scroll stloadleftview];
}
- (IBAction)flitOnclick:(id)sender {
    [self.rscroll stloadrightview];
}

- (IBAction)classbtOnclick:(id)sender {
    ON_CLASS = YES;
    movinglable.hidden = NO;
    staylable.hidden = YES;
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
    movinglable.frame = CGRectMake(90, 41, 90, 3);
    [UIView animateWithDuration:0.4f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        movinglable.frame = CGRectMake(0, 41, 90, 3);
    } completion:^(BOOL finished) {
    }];
    
    //$查看班级
}

- (IBAction)personbtOnclick:(id)sender {
    ON_CLASS = NO;
    movinglable.hidden = NO;
    staylable.hidden = YES;
    person_bt.hidden = NO;
    person_false.hidden = YES;
    [class_bt.titleLabel setTextColor:[UIColor colorWithRed:128.0/255.0 green:194.0/255.0 blue:219.0/255.0 alpha:1]];
    [person_bt.titleLabel setTextColor:[UIColor colorWithRed:1 green:1 blue:1 alpha:1]];
    [UIView animateWithDuration:0.4f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
        movinglable.frame = CGRectMake(90, 41, 90, 3);
    } completion:^(BOOL finished) {
    }];
    
    //$查看个人
}

- (IBAction)add_btOnclick:(id)sender {
    [self performSegueWithIdentifier:@"gotoadd" sender:self];
}
- (IBAction)class_or_studentOnClick:(id)sender {
    if(ON_CLASS)
    {
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"选择班级" message:@"选择要查看的班级" preferredStyle: UIAlertControllerStyleActionSheet];
        
        UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        
        for(NSString* cid in GlobalData.classes)
        {
            [alertController addAction:[UIAlertAction actionWithTitle:GlobalData.classes[cid].name style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
                
                [GlobalData setClassNow:cid];
                
                [class_or_student setTitle:[GlobalData getClassNow].name forState:UIControlStateNormal];
                [class_or_student setImage:[UIImage imageNamed:@"trangle"] forState:UIControlStateNormal];
                [class_or_student setTitleEdgeInsets:UIEdgeInsetsMake(0, -10, 0, 10)];
                [class_or_student setImageEdgeInsets:UIEdgeInsetsMake(0, class_or_student.frame.size.width-15, 0, -class_or_student.titleLabel.bounds.size.width-5)];
                
                [tableview reloadData];
            }]];
        }
        
        [alertController addAction:cancelAction];
        
        [self presentViewController:alertController animated:YES completion:nil];
    }
    else
    {
        [self performSegueWithIdentifier:@"gotochoosemem" sender:self];
    }
}

-(void)vc_openabout
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopen" object:nil];
}
-(void)vc_openov
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopenov" object:nil];
}
-(void)vc_opensr
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopensr" object:nil];
}
-(void)vc_opensg
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"toopensg" object:nil];
}
-(void)vc_unlog
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"tounlog" object:nil];

}
-(void)vc_openabouteee
{
    [self.scroll stcloseleftview];
    [self performSegueWithIdentifier:@"gotoabout" sender:self];
}
-(void)vc_openoverview
{
    [self.scroll stcloseleftview];
    [self performSegueWithIdentifier:@"gotooverview" sender:self];
}
-(void)vc_opensetreason
{
    [self.scroll stcloseleftview];
 
    [self performSegueWithIdentifier:@"gotosetreason" sender:self];
    
    [self.leftView removeFromSuperview];
}
-(void)vc_opensetgroup
{
    [self.scroll stcloseleftview];
    
    [self performSegueWithIdentifier:@"gotosetgroup" sender:self];
}
-(void)vc_unlogger
{
    UIAlertController *alertcontroller = [UIAlertController alertControllerWithTitle:@"退出登录" message:@"确定要退出登录吗？\n(未同步的数据将会丢失)" preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:@"退出登录" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //$退出登录
        
    }];
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    [alertcontroller addAction:cancelAction];
    [alertcontroller addAction:okAction];
    
    [self presentViewController:alertcontroller animated:YES completion:nil];
}

-(UIScrollView *)scroll
{
    if (!_scroll)
    {
        _scroll = [[AndyScrollView alloc]initWithFrame:CGRectMake(0, 0, 20, self.view.frame.size.height)];
        _scroll.contentSize = CGSizeMake(self.view.frame.size.width * 2, 0);
        _scroll.pagingEnabled = YES;
        _scroll.showsHorizontalScrollIndicator = NO;
        _scroll.delegate = self;
        _scroll.bounces = NO;
    }
    return _scroll;
}
-(UIScrollView *)rscroll
{
    if (!_rscroll)
    {
        _rscroll = [[RightScrollView alloc]initWithFrame:CGRectMake(self.view.frame.size.width, 0, self.view.frame.size.width, self.view.frame.size.height)];
        _rscroll.contentSize = CGSizeMake(self.view.frame.size.width * 2, 0);
        _rscroll.pagingEnabled = YES;
        _rscroll.showsHorizontalScrollIndicator = NO;
        _rscroll.delegate = self;
        _rscroll.bounces = NO;
    }
    return _rscroll;
}
- (void)scrollViewDidScroll:(UIScrollView *)scrollView;
{
    if (scrollView.contentOffset.x <= 0) {
        self.scroll.pan.enabled = YES;
    }else if (scrollView.contentOffset.x >= self.view.frame.size.width)
    {
        self.scroll.pan.enabled = NO;
    }
}

-(void)chooseplus
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"tochangeplus" object:nil];
}

-(void)setdate
{
    [[NSNotificationCenter defaultCenter]postNotificationName:@"tosetdate" object:nil];
}

-(void)setdateee
{
    [self showDatePicker:DateTypeOfEnd];
}

//筛选日期
- (void)getSelectDate:(NSString *)date type:(DateType)type {
    if([date isEqualToString:@"alles"]){
        //显示所有记录
        isFilterDate=false;
        cacheVaild=false;
    }else{
        //显示某一天记录，变量date为日期，格式为yyyy-mm-dd
        isFilterDate=true;
        cacheVaild=false;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat: @"yyyy-MM-dd"];
        filterDate=[dateFormatter dateFromString:date];
    }
    [self.rscroll setDatetem:date];
    [tableview reloadData];
}

//筛选加减分
- (void)chooseplussss
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"筛选加/减分" message:@"" preferredStyle: UIAlertControllerStyleActionSheet];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];

    [alertController addAction:[UIAlertAction actionWithTitle:@"加分" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //显示加分
        filterScore=1;
        cacheVaild=false;
        [self.rscroll changePlus];
        [tableview reloadData];
    }]];
    [alertController addAction:[UIAlertAction actionWithTitle:@"减分" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //显示减分
        filterScore=-1;
        cacheVaild=false;
        [self.rscroll changeMinus];
        [tableview reloadData];
    }]];
    [alertController addAction:[UIAlertAction actionWithTitle:@"不限" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        
        //显示全部
        filterScore=0;
        cacheVaild=false;
        [self.rscroll changeAll];
        [tableview reloadData];
    }]];
    [alertController addAction:cancelAction];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

//日期选择器
-(void)showDatePicker:(DateType)type {
    _pikerView = [HZQDatePickerView instanceDatePickerView];
    _pikerView.frame = CGRectMake(0, 0, ScreenRectWidth, ScreenRectHeight - 50);
    [_pikerView setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0]];
    _pikerView.delegate = self;
    _pikerView.type = type;
    // 今天之前的日期
    [_pikerView.datePickerView setMaximumDate:[NSDate date]];
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    [window addSubview:_pikerView];
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
+(NSString*)post : (NSString*) strUrl : (NSString*) args{
    //第一步，创建URL
    NSURL * url = [[NSURL alloc]initWithString:strUrl];
    //第二步，通过URL创建可变的request请求（只有创建可变的request才能设置POST请求）
    NSMutableURLRequest * request1 = [[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:100];
    //timeoutInterval:post超时最大时间是240秒,在方法中设置多少秒也没用。
    
    //第三步，设置POST请求方式
    [request1 setHTTPMethod:@"POST"];
    //第四步，设置参数
    NSString * bodyStr = args;
    NSData * body = [bodyStr dataUsingEncoding:NSUTF8StringEncoding];
    [request1 setHTTPBody:body];
    //第五步，连接服务器
    NSData * data = [NSURLConnection sendSynchronousRequest:request1 returningResponse:nil error:nil];
    
    return [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
}

@end
