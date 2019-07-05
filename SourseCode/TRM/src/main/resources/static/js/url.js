var ipath;


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

function getBasePath_manage(){
	return getPath();
}



function getUrl_manage() {
	return {
		//后台请求
		ceshi3:getPath()+getinterface_service().ceshi3,
		ceshi2:getPath()+getinterface_service().ceshi2,
        startRercord:getPath()+getinterface_service().startRercord,
        overRercord:getPath()+getinterface_service().overRercord,
        getClient:getPath()+getinterface_service().getClient,
        getRecord:getPath()+getinterface_service().getRecord,
        getPlayUrl:getPath()+getinterface_service().getPlayUrl,
        getRecordrealing:getPath()+getinterface_service().getRecordrealing,
        getPolygraphdata:getPath()+getinterface_service().getPolygraphdata,
        getEquipmentsState:getPath()+getinterface_service().getEquipmentsState,
        getPHDataBack:getPath()+getinterface_service().getPHDataBack,
        getFdrecordStarttimeByMTssid:getPath()+getinterface_service().getFdrecordStarttimeByMTssid
	};
}


function getinterface_service() {

	var basepath="/sweb";

	return {
		//后台请求
		ceshi3:basepath+"/ceshi/ceshi3",
		ceshi2:basepath+"/ceshi/ceshi2",

        startRercord:"/v1/police/out/startRercord",//开始会议
        overRercord:"/v1/police/out/overRercord",//结束会议
        getClient:"/v1/police/out/getClient",//获取各个服务器状态：暂时未启用
        getRecord:"/v1/police/out/getRecord",//获取笔录回访
        getPlayUrl:"/v1/police/out/getPlayUrl",//获取播放地址
        getRecordrealing:"/v1/police/out/getRecordrealing",//获取缓存中的实时笔录
        getPolygraphdata:"/v1/police/out/getPolygraphdata",//获取身心检测数据
        getEquipmentsState:"/v1/police/out/getEquipmentsState",//获取设备状态
        getPHDataBack:"/v1/police/out/getPHDataBack",
        getFdrecordStarttimeByMTssid:"/v1/police/out/getFdrecordStarttimeByMTssid",
	};
}


var pageid;


