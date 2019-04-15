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
	return getPath()+"/web";
}



function getUrl_manage() {
	return {
		//后台请求
		ceshi3:getBasePath_manage()+"/ceshi/ceshi3",

		ceshi2:getBasePath_manage()+"/ceshi/ceshi2",
	};
}
