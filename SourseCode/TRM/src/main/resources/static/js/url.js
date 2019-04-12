var ipath;
var roleuuid="";//主要用于RA查阅人员的特定权限问题

function setroleuuid(roleuuid_){
	roleuuid=roleuuid_;
}
function getroleuuid(){
	return roleuuid;
}

function setPath() {
    var hostport=document.location.host;
	ipath = 'http://'+hostport;
//    ipath = 'https://'+hostport;//HTTPS加密协议
};
function getPath() {
	
	if (ipath == "" || ipath == null || ipath == undefined || ipath == 'null' || ipath == 'Null' || ipath == 'NULL') {
		setPath();
	}
	
	return ipath;
}
//跳转到兑换统计
function getExchangeDetailsStat(userid){
	window.location.href = "/baseaction/userAction/gotoUrl_manage?url=app/appRedExchangeStat&&rid="+userid;
}
function getBasePath_manage(){
	return getPath()+"/webaction";
}

function getBasePath_page(){
	return getPath()+"/baseaction";
}

function getUrl_manage() {
	return {
		//后台请求
		//页面请求 
		login:getBasePath_page()+"/userAction/login",//用户登录
		
		businesslogin:getBasePath_page()+"/userAction/businesslogin",//商家登录
		
		gotoURL:getBasePath_page()+"/userAction/gotoUrl_manage",//后台首页查看
		
		getMenuBarlist:getBasePath_manage()+"/menubaraction/getMenuBarlist",//菜单栏***
		
		getRole:getBasePath_manage()+"/roleaction/getRoleList",//查询角色
		
		getUpdateRole:getBasePath_manage()+"/userAction/getUpdateRole",//查询修改角色名
		
		doUpdateRole:getBasePath_manage()+"/userAction/doUpdateRole",//修改角色名
		
		getUserList:getBasePath_manage()+"/userlist/getUserList",//前端用户查询全部
		
		getUserInfoById:getBasePath_manage()+"/userlist/getUserInfoById",  //前端用户查询单个
		
		updateUserInfo:getBasePath_manage()+"/userlist/updateUserInfo", //前端用户修改

		updataUserInfoUseState:getBasePath_manage()+"/userlist/updataUserInfoUseState",//前端用户删除
		
		updataUserInfoUseStatework:getBasePath_manage()+"/userlist/updataUserInfoUseStatework",//前端用户工作状态滑动开关
		
		ExcelgetUserInfoAll:getBasePath_manage()+"/userlist/ExcelgetUserInfoAll",//前端用户导出Excel
		
		ExcelgetUserInfoRankingAll:getBasePath_manage()+"/userlist/ExcelgetUserInfoRankingAll",//前端用户积分排名导出Excel

		updataUserInfoUseStatework:getBasePath_manage()+"/userlist/updataUserInfoUseStatework",//前端用户工作状态滑动开关
		
		getUserInfoRankingPage:getBasePath_manage()+"/userlist/getUserInfoRankingPage",//前端用户积分排名
		
		userAnalysisPage:getBasePath_manage()+"/userlist/userAnalysisPage",  //用户分析
		
		userProportion:getBasePath_manage()+"/userlist/userProportion",  //用户统计
		
		getAdminInfoPage:getBasePath_manage()+"/admininfoaction/getAdminInfoPage", //管理员用户查询
		
		updataAdminInfoState:getBasePath_manage()+"/admininfoaction/updataAdminInfoState",//管理员用户删除修改状态
				
		getAdminInfoA:getBasePath_manage()+"/admininfoaction/getAdminInfoA", //管理员查询单个

		updateAdminInfo:getBasePath_manage()+"/admininfoaction/updateAdminInfo", //管理员修改or添加
		
		addAdminInfo:getBasePath_manage()+"/admininfoaction/addAdminInfo", //管理员修改or添加
		
		ExcelgetAdminInfoAll:getBasePath_manage()+"/admininfoaction/ExcelgetAdminInfoAll", //管理员导出Excel
	
		getRoleList:getBasePath_manage()+"/roleaction/getRoleList", //角色查询全部
		
		getallintegralranklist:getBasePath_manage()+"/integralAction/getallintegralranklist", 
		
		getstatisticalOfRank:getBasePath_manage()+"/integralAction/getstatisticalOfRank",//总积分图表统计
		
		getproofreadintegralranklist:getBasePath_manage()+"/integralAction/getproofreadintegralranklist",//校对积分排名
	
		getcheckintegralranklist:getBasePath_manage()+"/integralAction/getcheckintegralranklist",//校对积分排名
	
		getexportExcel:getBasePath_manage()+"/integralAction/getexportExcel",//导出Excle
		
		getGradeType:getBasePath_manage()+"/geadeAction/getGradeType",//获取等级类型
		
		getGradeByType:getBasePath_manage()+"/geadeAction/getGradeByType",//根据等级类型获取等级列表
		
		saveModifyGrade:getBasePath_manage()+"/geadeAction/saveModifyGrade",//保存修改过的数据到后台
		
		savetz:getBasePath_manage()+"/userAction/gotoUrl_manage?url=grade&rid=",//保存成功页面重新跳转原来页面
		
		delectGradeById:getBasePath_manage()+"/geadeAction/delectGradeById",//根据等级id删除等级
		
		addToGrade:getBasePath_manage()+"/geadeAction/addToGrade",//添加等级
		
		getMaxGradebyTypeid:getBasePath_manage()+"/geadeAction/getMaxGradebyTypeid",//
		
		delectRole:getBasePath_manage()+"/roleaction/delectRole",//根据id删除角色
		
		doRole:getBasePath_manage()+"/userAction/gotoUrl_manage?url=role",
		
		addToRole:getBasePath_manage()+"/roleaction/addToRole",
		
		getRoleById:getBasePath_manage()+"/roleaction/getRoleById",
		
		UpdateRoleToFunction:getBasePath_manage()+"/roleaction/UpdateRoleToFunction",
		
		doUpdateRoleToFunction:getBasePath_manage()+"/roleaction/doUpdateRoleToFunction",
		
		getAllFunction:getBasePath_manage()+"/functionAction/getAllFunction",//获取所有的权限
		
		getFunctionType:getBasePath_manage()+"/functionAction/getFunctionType",//获取所有的权限类型
		
		getFunctionGou:getBasePath_manage()+"/functionAction/getFunctionGou",//获取所有的权限分组
		
		getFunctionSta:getBasePath_manage()+"/functionAction/getFunctionSta",//获取所有的权限状态
		
		updateToFunction:getBasePath_manage()+"/functionAction/updateToFunction",//更新权限
		
		delectFunction:getBasePath_manage()+"/functionAction/delectFunction",//获取所有的权限
		
		updateFunctionisenable:getBasePath_manage()+"/functionAction/updateFunctionisenable",//更改权限
		
		getUserToRole:getBasePath_manage()+"/userToRoleAction/getUserToRole",
		
		getFunctionByid:getBasePath_manage()+"/functionAction/getFunctionByid",
		
		doupdateUserToRole:getBasePath_manage()+"/userToRoleAction/doupdateUserToRole",
		
		updateUserToRole:getBasePath_manage()+"/userToRoleAction/updateUserToRole",
				
		getTaskstrategyPage:getBasePath_manage()+"/taskstrategyaction/getTaskstrategyPage", //查询任务策略
		
		updateTaskstrategy:getBasePath_manage()+"/taskstrategyaction/updateTaskstrategy", //修改任务策略
		
		addTaskstrategy:getBasePath_manage()+"/taskstrategyaction/addTaskstrategy", //添加任务策略
		
		countTaskstrategylist:getBasePath_manage()+"/taskstrategyaction/countTaskstrategylist", //任务策略总条数

		updateTaskstrategyBacktorecordbool:getBasePath_manage()+"/taskstrategyaction/updateTaskstrategyBacktorecordbool", //修改任务策略是否重新录状态+以及是否开启AI校对+以及是否跳过验证环节

		getTaskstrategyById:getBasePath_manage()+"/taskstrategyaction/getTaskstrategyById", //根据id查询单个任务策略
		
		updateTaskstrategyState:getBasePath_manage()+"/taskstrategyaction/updateTaskstrategyState", //删除任务策略（修改状态）

		getIndexMainNum:getBasePath_manage()+"/indexmainAction/getIndexMainNum",//采集首页展示数据（目前包括用户、服务器数据）

		getThresholdPage:getBasePath_manage()+"/thresholdaction/getThresholdPage",//查询全部page阀值
		
		getThresholdByOrdercol:getBasePath_manage()+"/thresholdaction/getThresholdByOrdercol",//查询全部阀值类型
		
		getThresholdById:getBasePath_manage()+"/thresholdaction/getThresholdById",//查询单个id阀值
		
		getThresholdByThresholdtype:getBasePath_manage()+"/thresholdaction/getThresholdByThresholdtype",//查询单个阀值根据Thresholdtype
		
		updateThreshold:getBasePath_manage()+"/thresholdaction/updateThreshold",//修改阀值
		
		getLogToUserinfoPage:getBasePath_manage()+"/logtouserinfoaction/getLogToUserinfoPage",//查看用户日志page
		
		getLogToUserinfoById:getBasePath_manage()+"/logtouserinfoaction/getLogToUserinfoById",//查看根据id单个用户日志详情
		
		getLogtoIntegralPage:getBasePath_manage()+"/logtointegralaction/getLogtoIntegralPage",//查看积分日志page
		
		getLogtoIntegralById:getBasePath_manage()+"/logtointegralaction/getLogtoIntegralById",//查看根据id单个积分日志详情
		
		getLogtoTaskPage:getBasePath_manage()+"/logtotaskaction/getLogtoTaskPage",//查看任务日志page
		
		getLogtoTaskById:getBasePath_manage()+"/logtotaskaction/getLogtoTaskById",//查看根据id单个任务日志详情
		
		getLogtoExchangePage:getBasePath_manage()+"/logtoexchangeaction/getLogtoExchangePage",//查看积分兑换日志page
		
		getLogtoExchangeById:getBasePath_manage()+"/logtoexchangeaction/getLogtoExchangeById",//查看根据id单个积分兑换日志详情
		
		insertExchange:getBasePath_manage()+"/exchangeaction/insertExchange",//积分兑换操作
	
		getOpenOrCloseAI:getBasePath_manage()+"/taskstrategyaction/getOpenOrCloseAI", //查询AI智能校对是否开启and跳过验证环节状态
				
		PassOrNoPass:getBasePath_manage()+"/taskstrategyaction/PassOrNoPass", //操作跳过验证环节是否开启
		
		getConfigInfo:getBasePath_manage()+"/configinfoaction/getConfigInfo", //查询系统配置
				
		updateConfigInfo:getBasePath_manage()+"/configinfoaction/updateConfigInfo", //修改系统配置
		
		addConfigInfo:getBasePath_manage()+"/configinfoaction/addConfigInfo", //添加系统配置	
		
		UserInfoRankingReset:getBasePath_manage()+"/userlist/UserInfoRankingReset", //用户总积分排名【清零】：未实现
		
		UserInfoRankingSettlement:getBasePath_manage()+"/userlist/UserInfoRankingSettlement", //用户总积分排名【结算】：只实现改变阀值
		
		getRoleStatistical:getBasePath_manage()+"/roleaction/getRoleStatistical", //获取角色以及角色人数
		
		getErrorFeedbackForProofreadPage:getBasePath_manage()+"/errorfeedbackforproofreadaction/getErrorFeedbackForProofreadPage", //查询验证打回的错误录音page
		
		getErrorFeedbackForRecordPage:getBasePath_manage()+"/errorfeedbackforrecordaction/getErrorFeedbackForRecordPage", //查询校对打回的错误录音page

		getThresholdInThresholdtype:getBasePath_manage()+"/thresholdaction/getThresholdInThresholdtype",//查询Thresholdtype为录音校对检查的阀值
		
		updateThresholdBythresholdtype:getBasePath_manage()+"/thresholdaction/updateThresholdBythresholdtype",//根据thresholdtype修改阀值
					
		/***********0926*************/
		getAppUserPage:getBasePath_manage()+"/appuserwaction/getAppUserPage",//输入法用户；列表
				
		getSensitivityOverPage:getBasePath_manage()+"/sensitivityoveraction/getSensitivityOverPage",//获取敏感度超标列表
		
		getSensitivityOverById:getBasePath_manage()+"/sensitivityoveraction/getSensitivityOverById",//根据id获取单个敏感度超标信息

		SensitivityOverbackRecord:getBasePath_manage()+"/sensitivityoveraction/SensitivityOverbackRecord",//打回敏感度超标到录音表

		getSensitivityLevels:getBasePath_manage()+"/sensitivitylevelaction/getSensitivityLevels",//敏感度级别状态为正常
		
		getErrorFeedbackReason:getBasePath_manage()+"/errorfeedbackreasonaction/getErrorFeedbackReason",//获取状态可用的全部错误原因
		
		/***********2018/11/02************/
		batchget_material:getBasePath_manage()+"/appmediaaction/batchget_material",//素材列表
		
		get_materialBymediaId:getBasePath_manage()+"/appmediaaction/get_materialBymediaId",//获取单个素材
		
		del_material:getBasePath_manage()+"/appmediaaction/del_material",//删除素材
		
		add_material:getBasePath_manage()+"/appmediaaction/add_material",//永久上传其他素材
		
		/***********2018/11/13************/
		getAppVersionsPage:getBasePath_manage()+"/appversionsaction/getAppVersionsPage",//获取版本号分页数据

		addAppVersions:getBasePath_manage()+"/appversionsaction/addAppVersions",//添加版本号
		
		updateAppVersions:getBasePath_manage()+"/appversionsaction/updateAppVersions",//修改版本号
		
		updateAppVersionsByIsenable:getBasePath_manage()+"/appversionsaction/updateAppVersionsByIsenable",//修改版本号状态
		
		getAppVersionsById:getBasePath_manage()+"/appversionsaction/getAppVersionsById",//获取单个版本号
		
		/***********2018/11/20************/
		push_material:getBasePath_manage()+"/appmediaaction/push_material",//广告推送缓存
		
		/***********2018/11/21************/
		add_news:getBasePath_manage()+"/appmediaaction/add_news",//永久上传图文素材
		
		update_news:getBasePath_manage()+"/appmediaaction/update_news",//修改永久图文素材
		
		/***********2018/11/22************/
		uploadnews:getBasePath_manage()+"/appmediaaction/uploadnews",//群发素材
		
		get_material:getBasePath_manage()+"/appmediaaction/get_material",//获取单个素材
		
		updatenewversion:getBasePath_manage()+"/appversionsaction/updatenewversion",//更换供用户下载的版本
		
		getnewversion:getBasePath_manage()+"/appversionsaction/getnewversion",//获取用户下载的版本	
		
		get_material:getBasePath_manage()+"/appmediaaction/get_material",//群发素材	
		
		/***********2018/11/24************/
		getAsrServerForPage:getBasePath_manage()+"/asrserveraction/getAsrServerForPage",//获取ASR服务器列表
		
		getAsrServerById:getBasePath_manage()+"/asrserveraction/getAsrServerById",//获取ASR服务器单个
		
		addAsrServer:getBasePath_manage()+"/asrserveraction/addAsrServer",//添加ASR服务器
		
		updateAsrServer:getBasePath_manage()+"/asrserveraction/updateAsrServer",//修改ASR服务器
		
		/***********2018/11/28************/
		get:getBasePath_manage()+"/appmediaaction/get",//获取临时素材
		
		getAppFeedBackListforPage:getBasePath_manage()+"/appfeedbackaction/getAppFeedBackListforPage",//获取用用户反馈信息
		
		/***********2018/12/02************/
		getAppFeedBackById:getBasePath_manage()+"/appfeedbackaction/getAppFeedBackById",//获取单个反馈信息
		
		updateAppFeedBackBybackstate:getBasePath_manage()+"/appfeedbackaction/updateAppFeedBackBybackstate",//修改反馈信息
		
		/***********2018/12/12************/
		getCheckListPage:getBasePath_manage()+"/wxCheckAction/getCheckListPage",//获取微信对账单
		
		getIndexMainNum2:getBasePath_manage()+"/indexmainAction/getIndexMainNum2",//输入法首页展示数据

		getCheckDetails:getBasePath_manage()+"/wxCheckAction/getCheckDetails",//获取微信对账单
		
		getRedPacketListPage:getBasePath_manage()+"/appRedPacketAction/getRedPacketListPage",//获取红包列表
				
		getRedpacketDetails:getBasePath_manage()+"/appRedPacketAction/getRedpacketDetails",//获取红包详情
		
		getAdvertisingListOfRed:getBasePath_manage()+"/appRedPacketAction/getAdvertisingListOfRed",//获取红包广告
		
		getAdvertisingListOfActivity:getBasePath_manage()+"/appRedPacketAction/getAdvertisingListOfActivity",//获取活动广告
		
		getMerchantList:getBasePath_manage()+"/appRedPacketAction/getMerchantList",//获取商户
		
		getAppAsrserver:getBasePath_manage()+"/appRedPacketAction/getAppAsrserver",//获取语种
		
		addBusinessRedPacket:getBasePath_manage()+"/appRedPacketAction/addBusinessRedPacket",//添加商务红包
		
		/***********兑换/积分************/
		getAppRedExchangeApplyForPage:getBasePath_manage()+"/appRedExchangeApplyAction/getAppRedExchangeApplyForPage",//获取兑换申请的分页列表
		
		getExchangeDetailsById:getBasePath_manage()+"/appRedExchangeApplyAction/getExchangeDetailsById",//获取兑换记录的详情
	
		passOrRejectApplyById:getBasePath_manage()+"/appRedExchangeApplyAction/passOrRejectApplyById",//处理兑换申请
		
		getAppRedExchangeForPage:getBasePath_manage()+"/appRedExchangeApplyAction/getAppRedExchangeForPage",//获取兑换记录的列表
		
		getExchangeStat:getBasePath_manage()+"/appRedExchangeApplyAction/getExchangeStat",//获取兑换统计
		
		getAppBuyScoreForPage:getBasePath_manage()+"/appUserScoreAction/getAppBuyScoreForPage",//获取积分充值记录
		
		getUserScoreListOrderBy:getBasePath_manage()+"/appUserScoreAction/getUserScoreListOrderBy",//分页排序查询用户积分
		
		getUserScoreDetailsById:getBasePath_manage()+"/appUserScoreAction/getUserScoreDetailsById",//用户积分详情
		
		/***********2018/12/29************/
		getAppLogtoExchangePage:getBasePath_manage()+"/appLogtoExchangeAction/getAppLogtoExchangePage",//日志用户积分兑换查询
			
		getAppLogtoExchangeById:getBasePath_manage()+"/appLogtoExchangeAction/getAppLogtoExchangeById",//日志用户积分兑换查询单个Id
		
		getAppLogtoIntegralPage:getBasePath_manage()+"/appLogtoIntegralAction/getAppLogtoIntegralPage",//日志用户积分查询
			
		getAppLogtoIntegralById:getBasePath_manage()+"/appLogtoIntegralAction/getAppLogtoIntegralById",//日志用户积分查询单个Id
		
		getAppLogtoUserPage:getBasePath_manage()+"/appLogtoUserAction/getAppLogtoUserPage",//日志用户查询
		
		getAppLogtoUserById:getBasePath_manage()+"/appLogtoUserAction/getAppLogtoUserById",//日志用户查询单个Id
		
		getAppPayToLogPage:getBasePath_manage()+"/appPayToLogAction/getAppPayToLogPage",//日志交易流水户查询
		
		getAppPayToLogById:getBasePath_manage()+"/appPayToLogAction/getAppPayToLogById",//日志交易流水单个Id
		
		getAppRedToLogPage:getBasePath_manage()+"/appRedToLogAction/getAppRedToLogPage",//日志红包查询
		
		getAppRedToLogById:getBasePath_manage()+"/appRedToLogAction/getAppRedToLogById",//日志红包单个Id
	
	 	/**********锦鲤活动*************/
		getAppActivityForPage:getBasePath_manage()+"/appActivityAction/getAppActivityForPage",//获取活动列表
		
		getEnrollUsersByActivityIdForPage:getBasePath_manage()+"/appActivityAction/getEnrollUsersByActivityIdForPage",//获取单个活动报名用户分页
		
		getGiftsByActivityId:getBasePath_manage()+"/appActivityAction/getGiftsByActivityId",//通过活动id获取礼品
		
		getUsersByActivityId:getBasePath_manage()+"/appActivityAction/getUsersByActivityId",//通过活动id获取中奖人
		
		getUsers:getBasePath_manage()+"/appActivityAction/getUsers",//获取可以内定的用户
		
		getActivityGoods:getBasePath_manage()+"/appActivityAction/getActivityGoods",//内定   获取活动奖品数据
		
		getAppActivityMessageById:getBasePath_manage()+"/appActivityAction/getAppActivityMessageById",//内定 获取活动名、奖品等数据
		
		confirmMessage:getBasePath_manage()+"/appActivityAction/confirmMessage",//提交内定
		
		addDateActivity:getBasePath_manage()+"/appActivityAction/addDateActivity",//添加日锦鲤/月锦鲤/年度锦鲤等活动
		
		getEndtimeClock:getBasePath_manage()+"/appActivityAction/getEndtimeClock",//获取结束时间阈值点
		
		addActivityAndAdvertising:getBasePath_manage()+"/appActivityAction/addActivityAndAdvertising",//添加活动配置广告
		
		getActivityDetails:getBasePath_manage()+"/appActivityAction/getActivityDetails",//编辑活动详情
		
		cancelActivity:getBasePath_manage()+"/appActivityAction/cancelActivity",//取消活动
		
		recoverActivity:getBasePath_manage()+"/appActivityAction/recoverActivity",//取消活动
		
		getAppGoodsForPage:getBasePath_manage()+"/appActivityGoodsAction/getAppGoodsForPage",//获取商品列表
		
		addOrUpdateGoods:getBasePath_manage()+"/appActivityGoodsAction/addOrUpdateGoods",//添加或修改商品
		
		getGoodsById:getBasePath_manage()+"/appActivityGoodsAction/getGoodsById",//获取修改商品信息
		
		deleteGoodsById:getBasePath_manage()+"/appActivityGoodsAction/deleteGoodsById",//删除商品
		
		/**********20190105*************/
		getAppAdvertisingPage:getBasePath_manage()+"/appAdvertisingAction/getAppAdvertisingPage",//广告查询page
		
		addAppAdvertising:getBasePath_manage()+"/appAdvertisingAction/addAppAdvertising",//添加广告
		
		updateAppAdvertising:getBasePath_manage()+"/appAdvertisingAction/updateAppAdvertising",//修改广告
		
		getAppAdvertisingToGoodsById:getBasePath_manage()+"/appAdvertisingAction/getAppAdvertisingToGoodsById",//广告查询单个id
		
		/**********20190107*************/
		getAppActivityGoodsList:getBasePath_manage()+"/appActivityGoodsAction/getAppActivityGoodsList",//查询全部商品
		
		addAppActivityGoods:getBasePath_manage()+"/appActivityGoodsAction/addAppActivityGoods",//添加商品全部商品
		
		updateAppAdvertisingForAuditstate:getBasePath_manage()+"/appAdvertisingAction/updateAppAdvertisingForAuditstate",//修改广告审核状态
	
		/**********20190111*************/
		appPagesList:getBasePath_manage()+"/appPagesAction/appPagesList",//页面广告列表
		
		getAppAdvertisingToGoodsByName:getBasePath_manage()+"/appAdvertisingAction/getAppAdvertisingToGoodsByName",//查询广告
		
		getAppAdApproachedPage:getBasePath_manage()+"/appAdApproachedAction/getAppAdApproachedPage",//查看接洽page
		
		getAppAdApproachedToAppUserById:getBasePath_manage()+"/appAdApproachedAction/getAppAdApproachedToAppUserById",//查看接洽id
		
		updateAppAdApproached:getBasePath_manage()+"/appAdApproachedAction/updateAppAdApproached",//修改接洽
		
		addOrDeleteappPages:getBasePath_manage()+"/appPagesAction/addOrDeleteappPages",//新增或删除页面广告
		
		getAppPagesToAdAndSingVOById:getBasePath_manage()+"/appPagesAction/getAppPagesToAdAndSingVOById",//查询页面关联广告
		
		getAppAdvertisingList:getBasePath_manage()+"/appAdvertisingAction/getAppAdvertisingList",//
		
		/**********20190114*************/
		nowActivity:getBasePath_manage()+"/appActivityAction/nowActivity",//活动马上开奖
		
		updateAppUserScore:getBasePath_manage()+"/appuserwaction/updateAppUserScore",//输入法高级用户充值
		
		checkSystemuserkey:getBasePath_manage()+"/appuserwaction/checkSystemuserkey",//输入法高级用户充值检查密钥
		
		getAsrServerlist:getBasePath_manage()+"/asrserveraction/getAsrServerlist",//输入法高级用户充值检查密钥
		
		/*********20190218**********/
		getAppIntegralSwitchPage:getBasePath_manage()+"/appIntegralSwitchAction/getAppIntegralSwitchPage",//输入环节灵活开关查询page
		
		updateAppIntegralSwitch:getBasePath_manage()+"/appIntegralSwitchAction/updateAppIntegralSwitch",//输入环节灵活开关修改
		
		addAppIntegralSwitch:getBasePath_manage()+"/appIntegralSwitchAction/addAppIntegralSwitch",//输入环节灵活开关添加
		
		getAppIntegralSwitchBySwitchtype:getBasePath_manage()+"/appIntegralSwitchAction/getAppIntegralSwitchBySwitchtype",//输入环节灵活开关查询单个
		
		downloadbill:getBasePath_manage()+"/appStatementsAction/downloadbill",//获取对账单
		
		/*********20190226**********/
		FinishAppAdApproached:getBasePath_manage()+"/appAdApproachedAction/FinishAppAdApproached",//接洽完成

		/*********20190227**********/
		getAdsByBusinessId:getBasePath_manage()+"/businessAction/getAdsByBusinessId",//获取商家广告
		
		getBusinessIndex:getBasePath_manage()+"/indexmainAction/getBusinessIndex",//接洽完成
		
		getAdsByBusinessIdByAdId:getBasePath_manage()+"/businessAction/getAdsByBusinessIdByAdId",//商家广告查看
		
		updateAdsByBusinessIdByAdId:getBasePath_manage()+"/businessAction/updateAdsByBusinessIdByAdId",//商家广告修改
		
		getAdsByBusinessById:getBasePath_manage()+"/businessAction/getAdsByBusinessById",//商家广告修改查看
	};
}
