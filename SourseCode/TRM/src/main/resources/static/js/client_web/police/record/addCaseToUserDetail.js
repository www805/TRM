$(function () {
    $("#getCardreader_btn").click(function () {
        var cardtypetext=$("#cards option:selected").text();
        if ($.trim(cardtypetext)!="居民身份证"){
            layer.msg("请先选择身份证类型",{icon:5});
            return;
        }
        var url=getActionURL(getactionid_manage().addCaseToUser_getCardreader);
        ajaxSubmitByJson(url,null,function (data) {
            if(null!=data&&data.actioncode=='SUCCESS'){
              var data=data.data;
              if (isNotEmpty(data)){
                  var data = eval("("+data+")");
                  if (isNotEmpty(data)){
                      reset();
                      var retmsg=data.retmsg==null?"未知错误":data.retmsg;

                      var bool=checkout_cardnum(data.cardno,"居民身份证");
                      if (!bool){
                          return;
                      }

                      $("#username").val(data.name==null?"":data.name);
                      $("#cardnum").val(data.cardno);
                      $("#domicile").val(data.address);
                      $("#sex").val(data.sex=="女"?2:(data.sex=="男"?1:-1));
                    /*  $("#national").val(data.nation);*///和数据库不符合


                      var nation=data.nation;
                      $("#national option").each(function () {
                          var txt=$(this).text();
                          var value=$(this).attr("value");
                          if (txt.indexOf(nation)>-1){
                              $("#national").val(value);
                              return;
                          }
                      })



                      layui.use('form', function(){
                          var form =  layui.form;
                          form.render();
                      });
                      layer.msg(retmsg,{icon:6,time:1000},function () {
                          getUserByCard();
                      });
                  }
              }
            }else {
                layer.msg(data.message,{icon:5});
            }
        })
    });



     // 建立连接
    /*    socket = io.connect('http://localhost:5000',{'timeout': 300000,'reconnectionDelayMax':1000,'reconnectionDelay':500});
        socket.on('connect', function () {
            console.log("连接成功___读卡器开始读卡___")
            socket.emit('startRead');
            socket.emit('readOnce','true');
        });
        socket.on('disconnect', function () {
            console.log("连接断开___读卡器开始读卡___")
        });
        socket.on('card message', function(msg){

            var cardtypetext=$("#cards option:selected").text();
            if ($.trim(cardtypetext)!="居民身份证"){
                layer.msg("请先选择身份证类型",{icon:5});
                return;
            }

            var base = new Base64();
            //2.解密后是json字符串
            var result1 = base.decode(msg);
            var data = eval("("+result1+")");
            console.log(data)
            if (isNotEmpty(data)){
                reset();
                var retmsg=data.retmsg==null?"未知错误":data.retmsg;
                var bool=checkout_cardnum(data.cardno,"居民身份证");
                if (!bool){
                    return;
                }
                $("#username").val(data.name==null?"":data.name);
                $("#cardnum").val(data.cardno);
                $("#domicile").val(data.address);
                $("#sex").val(data.sex=="女"?2:(data.sex=="男"?1:-1));

                 var nation=data.nation;
                  $("#national option").each(function () {
                          var txt=$(this).text();
                          var value=$(this).attr("value");
                          if (txt.indexOf(nation)>-1){
                              $("#national").val(value);
                              return;
                          }
                      })

                layui.use('form', function(){
                    var form =  layui.form;
                    form.render();
                });
                layer.msg(retmsg,{icon:6,time:1000},function () {
                    getUserByCard();
                });
            }
        });*/
})

function Base64() {
    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for decoding
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }


    // private method for UTF-8 decoding
    _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while ( i < utftext.length ) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}

function reset() {
    dquserssid=null;//当前用户的ssid
    dqcasessid=null;//当前案件ssid
    cases=null;
    dqotheruserinfossid=null;//当前询问人(新增询问人回显)
    dqotherworkssid=null;//当前询问人对应的工作单位
    layui.use(['form','laydate'], function(){
        var form=layui.form;
        var laydate=layui.laydate;
        $("input:not('#adminname'):not('#workname'):not('#recordplace'):not('#cardnum'):not('#modelssid')").val("");/*not('#occurrencetime'):not('#starttime'):not('#endtime'):*/
        $("#asknum").val(0);
        $('select').not("#cards").prop('selectedIndex', 0);
        $("#casename_ssid").html("");

        laydate.render({
            elem: '#occurrencetime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });
        laydate.render({
            elem: '#starttime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });
        laydate.render({
            elem: '#endtime' //指定元素
            ,type:"datetime"
            ,value: new Date(Date.parse(new Date()))
            ,format: 'yyyy-MM-dd HH:mm:ss'
        });

        form.render('select');
    });
}