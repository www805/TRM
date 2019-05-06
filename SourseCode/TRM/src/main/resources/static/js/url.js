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
	};
}


function getinterface_service() {

	var basepath="/sweb";

	return {
		//后台请求
		ceshi3:basepath+"/ceshi/ceshi3",

		ceshi2:basepath+"/ceshi/ceshi2",
	};
}


var pageid;


