/**
 * 这个js是用于二次开发ueditor，使ueditor更贴切Word的操作
 */
(function(){

window.TOWORD=this;

TOWORD.divheightmap = {};

TOWORD.pagemaxheight=900;//每页最大的可编辑区域的高度
TOWORD.margintopandbutton=5;//p标签的margin值
TOWORD.importwordrun=false;//导入Word的时候为true


//需要改变光标聚焦的div
    var changefocuspdiv;
    /**
     * 主要用于写数据和度数据
     * @type {{getAllPEvtByDivid, reTypesetting, importhtml, getAllPHeightByDivid}}
     */
    TOWORD.page=function(){
        return {
            /**
             * 导入HTML页面数据到ueditor
             */
            importhtml:function (html){
                TOWORD.importwordrun=true;
                ue.execCommand('insertHtml', html);
                //分页逻辑

                //获取所在div的全部P文件
                //循环得到高度，组成div，div高度固定1060，每一个div所有的P占有的总高度900
                //写入body
                if(TOWORD.divnum<1){
                    console.log(TOWORD.divnum+":TOWORD.divnum 没有一个div，这是不科学的");
                    return ;
                }

                var olddivnum=TOWORD.divnum;
                //清空关于divnum的缓存
                TOWORD.divnum=0;
                TOWORD.divIDmap=new Array();


                //divhtmlstart div页面的开头
                var divhtmlstart="<div id='&nestdivid' style= '"+TOWORD.divpage.divcss()+"' >";
                //divhtmlend div页面的结束
                var divhtmlend="</div>";
                //divshtml整个body所有页面的聚合
                var divshtml=divhtmlstart.replace("&nestdivid",TOWORD.divpage.getnextpagedivid()).replace("&nextdividnum",TOWORD.divnum);
                var ph=0;
                var ifr = document.getElementsByName("ueditoriframe")[0];
                for(var num=1;num <=olddivnum;num++){
                    var pagedivid=TOWORD.divpage.getpagedivid(num);
                    var div=  ifr.contentWindow.document.getElementById(pagedivid);
                    if(div==null||div.length==0){
                        continue;
                    }

                    console.log(pagedivid+":pagedivid --- div.offsetHeight:"+div.offsetHeight);


                    var divps=div.childNodes;//是否需要获取所有标签，而不只是P标签
                    if(null!=divps&&divps.length > 0){

                        for(var n=0;n<divps.length ;n++){
                            //console.log(n+":n," +divps[n].scrollHeight+":scrollHeight 导入Word");
                            var pht=getNodeHeightByNode(divps[n]);

                            if(ph<(TOWORD.pagemaxheight-pht)){//小于最大内部div高度
                                divshtml+=divps[n].outerHTML;
                                ph+=pht;
                            }else{//结束这个div，开启下一个div
                                console.log(ph+"--ph换div了，TOWORD.divnum："+TOWORD.divnum);
                                divshtml+=divhtmlend;
                                TOWORD.divheightmap[TOWORD.divpage.getpagedivid(TOWORD.divnum)]=ph;//设置这个div的高度
                                ph=pht;
                                divshtml+=divhtmlstart.replace("&nestdivid",TOWORD.divpage.getnextpagedivid())+divps[n].outerHTML;
                            }
                        }
                    }else{
                        console.log(divps+"divps is null");
                    }
                }
                if(!divshtml.endsWith("</div>")){
                    divshtml+=divhtmlend;
                    console.log(ph+"--ph最后一个div，TOWORD.divnum："+TOWORD.divnum);
                    TOWORD.divheightmap[TOWORD.divpage.getpagedivid(TOWORD.divnum)]=ph;//设置最后一个div的高度
                }
                UE.getEditor('editor').setContent(divshtml,false);

                TOWORD.importwordrun=false;//接触工作状态

                //添加光标聚焦
                changefocuspdiv=getDivByDivid(TOWORD.divpage.getpagedivid(TOWORD.divnum));
                focusp(ue,changefocuspdiv,1);
            },
            getAllPEvtByDivid:function(divid){
                return getAllPEvtByDivid(divid);
            },
            getAllPHeightByDivid:function(divid){
                return getAllPHeightByDivid(divid);
            },
            getifamedivByDivid:function(divid){
                return getDivByDivid(divid);
            },
            /**
             * 涉及到可能的重新分页问题
             * @param oldheight div的缓存高度
             * @param newheight div的新高度
             * @param divid 当前div的ID
             */
            reTypesetting:function(ue,oldheight,newheight,divid){

                //几种情况
                //组合结果：
                // 1、P总高大了，有下一页；(不需要判断这一页是否到达900的编辑极限)
                // 2、P总高小了，有下一页；(要考虑下一页往上一页补充一P是否会超过900)
                // 3、P总高大了，没有下一页；（要考虑，超过就分页，最后一个P写入下一页）
                // 4、P总高小了，没有下一页；(不需要判断这一页是否到达900的编辑极限)
                // （先按照不拆分最后或最开始的P用于补充上一页/填充下一页；最后一定要考虑当前页最后一个P和下一页的第一个P在需要重新分页的时候的处理）
                //（当剩下的P进行重新分页时，P为空的情况）

                if(oldheight<newheight){//P总高大了

                    //判断新增一行的高度之后是否大于每页的最大编辑高度，大于的话就是情况1的处理，不大于就不管
                    if(newheight<TOWORD.pagemaxheight){
                        TOWORD.divheightmap[divid]=newheight;
                        console.log("不用任何处理，divid："+divid);
                        return ;
                    }

                    //看是否存在下一页
                    var nextdividnum=TOWORD.currentdivnum+1;
                    var nextdivid=TOWORD.divpage.getpagedivid(nextdividnum);//下一页的ID
                    if(checkExistByDivid(nextdivid)){//判断是否存在

                        //情况1
                        //获取本页最后一个P,缓存p，并删除P，以及以下所有页的P；
                        // 删除除了下一页的以下所有页，清空下一页，修改缓存页数；
                        // 在下一页中进行数据导入并分页；

                        var newps=new Array();
                        var POBJs=getLeastPEvtAndDelByDivid(ue,divid,newheight);//最后几个子节点
                        for(var po=0;po<POBJs.length;po++){
                            newps.push(POBJs[po]);//当前页剩下的P重新分页
                        }
                        newps=getNextAllPEvt(newps);
                        delDivNextHimselfByDividnum(nextdividnum);//删除除了下一页的以下所有页
                        var nextdiv=getDivByDivid(nextdivid);
                        nextdiv.innerHTML="";//清空下一页
                        TOWORD.divnum=nextdividnum;//修改缓存页数

                        //重新分页
                        againFY(ue,newps);

                    }else{
                        //情况3
                        //获取本页最后一个P,缓存p，并删除P，
                        //直接新增一个div带入HTML，高度，div编号
                        var POBJs=getLeastPEvtAndDelByDivid(ue,divid,newheight);//最后一个P
                        //需要判断是在第一个P的开头还是结尾
                        TOWORD.divnum=nextdividnum;//修改缓存页数
                        //重新分页
                        againFY(ue,POBJs);

                    }
                    //添加光标聚焦
                    changefocuspdiv=getDivByDivid(TOWORD.divpage.getpagedivid(nextdividnum));

                }else {//P总高小了
                    //看是否存在下一页
                    var nextdividnum=TOWORD.currentdivnum+1;
                    var nextdivid=TOWORD.divpage.getpagedivid(nextdividnum);//下一页的ID
                    if(checkExistByDivid(nextdivid)){//判断是否存在
                        //情况2
                        //2、P总高小了，有下一页；(要考虑下一页往上一页补充一P是否会超过900)
                        var newps=new Array();
                        newps=getNextAllPEvt(newps);//下一页开始的所有页的子节点的集合

                        delDivNextHimselfByDividnum(nextdividnum);//删除除了下一页的以下所有页
                        var nextdiv=getDivByDivid(nextdivid);
                        nextdiv.innerHTML="";//清空下一页
                        TOWORD.divnum=nextdividnum;//修改缓存页数

                        //往本页加数据
                        var ph=getAllPHeightByDivid(divid);//本页原有的所有P的高度
                        //newps_addc当前div需要新增的P的聚合
                        var newps_addc=new Array();

                        var addtodivbool=false;//是否往上一个div中加了数据，没有加的话就需要把下一页删掉
                        if(newps.length==1){

                            var waitaddheight=newps[0].realHeight;

                            if(ph < (TOWORD.pagemaxheight-waitaddheight)){//还是这一页
                                ph+=waitaddheight;
                                addPToDIV_Element(ue,newps,ph,TOWORD.currentdivnum);//给当前页添加数据
                                addtodivbool=true;
                                console.log(ph+"ph,往上一个div加P，只有1个P的情况下divnum："+TOWORD.currentdivnum);
                                newps.shift();//去掉已经被写入当前页的数据
                            }else{
                                console.log(ph+"：ph--"+waitaddheight+":待加入P的高度，下一页的P数据只有一个，并且这个P的高度有点大，不能写入当前页--divnum："+TOWORD.currentdivnum);
                            }
                        }else{
                            for(var i=0;i<newps.length;i++){

                                var waitaddheight=newps[i].realHeight;
                                if(ph < (TOWORD.pagemaxheight-waitaddheight)){//还是这一页
                                    ph+=waitaddheight;
                                    newps_addc.push(newps[i]);
                                    newps.shift();//去掉已经被写入当前页的数据
                                    i--;
                                }else{
                                    if(newps_addc.length > 0){
                                        addPToDIV_Element(ue,newps_addc,ph,TOWORD.currentdivnum);//给当前页添加数据
                                        addtodivbool=true;
                                        console.log(ph+"ph,往上一个div加P，divnum："+TOWORD.currentdivnum);
                                        break ;//把当前页加满了就跳出循环
                                    }else{
                                        console.log(ph+"：ph--不用往上一个div加P，divnum："+TOWORD.currentdivnum);
                                    }
                                }
                            }
                        }




                        //剩下的也重新分页
                        if(newps.length > 0){
                            againFY(ue,newps);
                        }else{
                            delDivNextHimselfByDividnum(TOWORD.currentdivnum);//删除本页一下的所有页
                        }

                        //添加光标聚焦,

                    }else{
                        //情况4
                        //4、P总高小了，没有下一页；(不需要判断这一页是否到达900的编辑极限)
                        TOWORD.divheightmap[divid]=newheight;
                    }
                }

                //执行光标聚焦
                //还需要判断是在第一个P的开头还结尾（如果光标在P中，光标后面是否有数据）
                if(theCursoriNLeastP!=-1){
                    console.log(theCursoriNLeastP+"-----------theCursoriNLeastP 有值了")
                    focusp(ue,changefocuspdiv,theCursoriNLeastP);
                }else{
                    // ue.focus();
                }

            }
        }
    }();



    /**
     * 已经有几个div
     */
    TOWORD.divnum=0;
    /**
     * 光标正在第几个div上
     */
    TOWORD.currentdivnum=1;

    /**
     * div id的集合
     * @type {Array}
     */
    TOWORD.divIDmap = [];


    /**
     * 主要用于div页的记录和生成
     * @type {{divcss, getnextpagedivid, changediv, getpagedivid, getcurrentpagedivid}}
     */
    TOWORD.divpage=function(){

        return {
            changediv:function (num) {
                TOWORD.currentdivnum=num;
                console.log("TOWORD.currentdivnum:"+TOWORD.currentdivnum);
            },
            divcss:function() {
                return "margin: 10px auto;padding: 8% 7% 8% 7%;width: 65%;height:90%;background-repeat: no-repeat; background-size: 100% 100%;background-image: url(/ueditor/themes/default/images/body_bg.png);box-shadow:0 0 10px #000000;outline=0;";
            },
            /**
             * 这个方法只有新增页的时候才会使用
             * @returns {string}
             */
            getnextpagedivid:function() {
                TOWORD.divnum++;
                var newpagedivid="newpage"+TOWORD.divnum;

                TOWORD.divheightmap[newpagedivid]=21;//初始化的时候只有1个P标签，暂时给21 的高度
                TOWORD.divIDmap[TOWORD.divIDmap.length]=newpagedivid;

                return newpagedivid;
            },
            /**
             * 获取当前div
             * @returns {string}
             */
            getcurrentpagedivid:function() {
                return "newpage"+TOWORD.currentdivnum;
            },
            getpagedivid:function(num) {
                return "newpage"+num;
            },
            checkFirstPageExist:function(ue){
                var divid="newpage1";
                if(!checkExistByDivid(divid)){
                    console.log("newpage1 不存在，检测是否还有其他page ");
                    var divs=ue.body.getElementsByTagName('div');
                    if(!isNotEmpty(divs)||divs.length==0){
                        console.log("page is 0,add ,当前TOWORD.divnum："+TOWORD.divnum+"--如果不是0也是有问题的");
                        TOWORD.divnum=0;
                        var divhtml="<div tabindex=\"0\" hidefocus=\"true\"  id='"+TOWORD.divpage.getnextpagedivid()+"' style= '"+TOWORD.divpage.divcss()+"'><p ></p></div>";
                        UE.getEditor('editor').setContent(divhtml,false);
                    }
                }
            }
        };





    }();

    TOWORD.util=function(){

        return {
            getlineHeight:function(ue){
                return getlineHeight(ue);
            },
            getdivByChildnode:function(ue){
                return getdivByChildnode(ue);
            },
        }

    }();

    /**
     * 获取当前正在编辑的行的高度（不等于实际高度，如果是自动换行的话就不相等了）
     * @param ue
     * @returns {string}
     */
    function getlineHeight(ue) {
    var range=ue.selection.getRange();
    var bk_start = range.createBookmark().start; // 创建一个临时dom，用于获取当前光标的坐标
    bk_start.style.display = ''; // 设置临时dom不隐藏
    var domheight=UE.dom.domUtils.getComputedStyle(bk_start,"height");//当前行的高度
    var domheight_num=domheight.replace(/[^0-9]/ig,"");
    $(bk_start).remove() // 移除临时dom

    return domheight_num;
}

    /**
     * 获取当前正在编辑的p的 父节点div
     * @param ue
     * @returns {string}
     */
    function getdivByChildnode(ue) {
        var range=ue.selection.getRange().startContainer;
        var par=range.parentNode;
        if(par.nodeName=='div'||par.nodeName=='DIV'){
            return par.id;
        }
        var par2=par.parentNode;
        if(par2.nodeName=='div'||par2.nodeName=='DIV'){
            return par2.id;
        }
        var par3=par2.parentNode;
        if(null!=par3&&(par3.nodeName=='div'||par3.nodeName=='DIV')){
            return par3.id;
        }else{
            console.log("没有找到正在编辑的div的ID");
            return null;
        }

    }


    /**
     * 获取该节点下的所有tagName子节点
     */
    function getAllNextEvt(evt,tagName){
        var divps=evt.getElementsByTagName(''+tagName);
        if(null!=divps&&divps.length > 0){
            return divps;
        }else{
            console.log(divps+"divps getAllNextEvt is null");
        }
        return null;
    }

    /**
     * 获取指定div的所有P标签
     */
    function getAllPEvtByDivid(divid){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        var div=  ifr.contentWindow.document.getElementById(divid);
        var divps=div.getElementsByTagName('p');
        if(null!=divps&&divps.length > 0){
            return divps;
        }else{
            console.log(divps+"divps getAllPEvtByDivid is null");
        }
        return null;
    }

    //如果编辑的光标在最后一个P中，并且在开头就是1，并且在结尾就是2，不在P中就是-1
    var theCursoriNLeastP=-1;

    /**
     * 获取当前divID的最后几个P标签返回，并把这个div中删除P标签
     * （可能会有多个子节点直到小于最大高度）
     * @param ue 编辑器对象
     * @param divid 当前divID
     * @param newheight 当前div的最新内部P集合的高度（可以算出来，但是前面有了，这里就不用算了）
     */
    function getLeastPEvtAndDelByDivid(ue,divid,newheight){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        if(!isNotEmpty(ifr)) return;
        var div=  ifr.contentWindow.document.getElementById(divid);
        if(!isNotEmpty(div)) return null;
        var divps=div.childNodes;
        if(null!=divps&&divps.length > 0){
            var moreps=new Array();
            var rvnodeht=0;//减去的子节点的高度和
            for(var i=divps.length-1;i>=0;i--){

                var leastp=divps[i];
                var pht=leastp.scrollHeight;//p的高度加上margin的高度
                //判断一下是否有隐形的高度样式
                var marginBottom=leastp.style.marginBottom;
                if(isNotEmpty(marginBottom)){
                    pht+=in2px(marginBottom);
                }else{
                    pht+=TOWORD.margintopandbutton
                }
                var marginTop=leastp.style.marginTop;
                if(isNotEmpty(marginTop)){
                    pht+=in2px(marginTop);
                }

                var POBJ={
                    realHeight:pht,//节点真实占用高度
                    outerHTML:leastp.outerHTML,
                    innerHTML:leastp.innerHTML,
                    style:leastp.style,
                    num:0+i
                };

                //判断光标所在位置
                var range=ue.selection.getRange();
                var bk_start = range.createBookmark().start; // 创建一个临时dom，用于获取当前光标
                bk_start.style.display = ''; // 设置临时dom不隐藏
                var position = UE.dom.domUtils.getPosition(leastp,bk_start);
                if(position==16||position==20){

                    var nextnode=bk_start.nextSibling;
                    if(isNotEmpty(nextnode)&&isNotEmpty(nextnode.length)&&nextnode.length>0){
                        console.log(position+"--position,nextnode:"+nextnode.nodeName+"--"+nextnode.nodeValue);
                        theCursoriNLeastP=1;
                    }else{
                        console.log(position+"--position,最后一个node？？");
                        theCursoriNLeastP=2;
                    }
                }
                $(bk_start).remove(); // 移除临时dom


                //删除最后一个P
                leastp.parentNode.removeChild(leastp);
                rvnodeht+=pht;
                moreps.push(POBJ);
                if((newheight-rvnodeht) < TOWORD.pagemaxheight){//判断减去子元素后是否小于最大子元素高度和，小的话就直接出去

                    break;
                }else{
                    console.log(newheight+":newheight--还需要再删除几个元素--rvnodeht："+rvnodeht);
                }
            }

            var moreps_add=new Array();
            if(moreps.length > 0){
                for(var i=moreps.length-1;i>=0;i--){
                    moreps_add.push(moreps[i]);//排序反了，重新反回来
                }
            }
            return moreps_add;
        }else{
            console.log(divps+"divps getAllPEvtByDivid is null");
        }
        return null;
    }



    /**
     * 获取当前divID的以下的所有页的所有标签
     */
    function getNextAllPEvt(newps){

        var nextdividnum=TOWORD.currentdivnum+1;
        if(nextdividnum > TOWORD.divnum){
            console.log(nextdividnum+":nextdividnum--TOWORD.divnum:"+TOWORD.divnum+"下一页比总页数还大，怎么可能");
            return newps;
        }
        var ifr = document.getElementsByName("ueditoriframe")[0];

        for(var i=nextdividnum;i <= TOWORD.divnum;i++){
            var divid=TOWORD.divpage.getpagedivid(i);
            var div=  ifr.contentWindow.document.getElementById(divid);
            if(!isNotEmpty(div)) continue;
            var divps=div.childNodes;
            if(null!=divps&&divps.length > 0){
                var newpsnum=newps.length;
                for (var j=0;j<divps.length;j++){

                    var POBJ={
                        realHeight:getNodeHeightByNode(divps[j]),
                        outerHTML:divps[j].outerHTML,
                        innerHTML:divps[j].innerHTML,
                        style:divps[j].style,
                        num:newpsnum+j
                    };
                    newps.push(POBJ);
                }
            }else{
                console.log(divps+"divps getAllPEvtByDivid is null");
            }
        }
        return newps;
    }

    /**
     * 检测div是否存在
     */
    function checkExistByDivid(divid){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        var div =  ifr.contentWindow.document.getElementById(divid);
        return isNotEmpty(div);
    }

    /**
     * 获取div
     */
    function getDivByDivid(divid){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        var div =  ifr.contentWindow.document.getElementById(divid);
        if(isNotEmpty(div)){
            return div;
        }
        return null;
    }

    /**
     * 删除divIDnum以下的所有div
     */
    function delDivNextHimselfByDividnum(dividnum){

        var nextdividnum=dividnum+1;
        if(nextdividnum > TOWORD.divnum){
            console.log(nextdividnum+":nextdividnum--TOWORD.divnum:"+TOWORD.divnum+"下一页比总页数还大，不需要删除任何数据");
            return ;
        }
        var ifr = document.getElementsByName("ueditoriframe")[0];
        for(var i=nextdividnum;i <= TOWORD.divnum;i++){//div编号从1开始的
            var divid=TOWORD.divpage.getpagedivid(i);
            var div=  ifr.contentWindow.document.getElementById(divid);
            if(!isNotEmpty(div)) continue;

            var divparentNode=div.parentNode;
            divparentNode.removeChild(div);
            console.log(divid+":divid----删除了")
        }
    }

    /**
     * 获取所有div的所有标签的集合
     */
    function getAllPEvt(){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        var ps=[];
        for(var num=1;num <=TOWORD.divnum;num++){
            var div=  ifr.contentWindow.document.getElementById(TOWORD.divpage.getpagedivid(num));
            if(div==null||div.length==0){
                continue;
            }
            var divps=div.childNodes;
            if(null!=divps&&divps.length > 0){
                for(var i=0;i<divps.length;i++){
                    ps.push(divps[i]);
                }

            }else{
                console.log(divps+"divps is null");
            }
        }
        console.log(ps.length+":ps--");
        return ps;
    }

    /**
     * 返回div中所有标签的高度集合
     * 不只是P；可能还有隐形的margin高度
     * @param divid
     * @returns {*}
     */
    function getAllPHeightByDivid(divid){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        var div=  ifr.contentWindow.document.getElementById(divid);
        if(!isNotEmpty(div)){
            console.log("getAllPHeightByDivid div is null 没有找到这个div，divid"+divid);
            return 0;
        }
        var divps=div.childNodes;
        if(null!=divps&&divps.length > 0){
            var psheight=0;
            for(i=0;i<divps.length;i++){
                psheight+=getNodeHeightByNode(divps[i]);
            }
            console.log(divid+":divid ---getAllPHeightByDivid psheight:"+psheight);
            return psheight;
        }else{
            console.log(divps+"divps getAllPHeightByDivid is null");
        }
        return div.innerHeight;//未确认
    }

    /**
     * 返回改节点的实际占用高度
     * @param cnode 节点
     * @returns {*}
     */
    function getNodeHeightByNode(cnode){

        var nodeheight=cnode.scrollHeight;//节点本身的内部高度
        //判断一下是否有隐形的高度样式
        var marginBottom=cnode.style.marginBottom;
        if(isNotEmpty(marginBottom)){
            nodeheight+=in2px(marginBottom);
        }else{//如果节点有margintopandbutton/marginTop就不需要上级节点的margin属性
            nodeheight+=TOWORD.margintopandbutton;//p的高度加上margin的高度
        }
        var marginTop=cnode.style.marginTop;
        if(isNotEmpty(marginTop)){
            nodeheight+=in2px(marginTop);
        }
        return nodeheight;
    }

    /**
     * (未验证正确性)
     * 返回div中自定义标签指定值的div节点
     * @param tag 那种标签
     * @param attribute
     * @param attributeval
     * @returns {*}
     */
    function getparentNodeByTag(tag,attribute,attributeval){
        var ifr = document.getElementsByName("ueditoriframe")[0];
        for(var num=TOWORD.divnum-1;num >=1;num--){
            var div=  ifr.contentWindow.document.getElementById(TOWORD.divpage.getpagedivid(num));
            if(div==null||div.length==0){
                continue;
            }
            var divps=getAllNextEvt(div,tag);
            if(null!=divps&&divps.length > 0){
                for(var i=divps.length-1;i>=0;i--){
                    var at=divps[i].getAttribute(attribute);
                    if(isNotEmpty(at)&&at==attributeval){
                        return divps.parentNode();//对应P的子节点
                    }
                }
            }else{
                console.log(divps+"divps is null");
            }
        }
        return null;
    }

    /**
     * 重新分页
     * @param allp 所有的p
     */
    function againFY(ue,allp){


        //pshtml每个div所有p的聚合
        var pshtml="";
        var ph=0;
        var divnum=TOWORD.divnum;//新增页的编号

        for(var i=0;i<allp.length;i++){

            if(ph < (TOWORD.pagemaxheight-allp[i].realHeight)){//还是这一页
                pshtml+=allp[i].outerHTML;
                ph+=allp[i].realHeight;
            }else{//下一页
                addPToDIV(ue,pshtml,ph,divnum);
                divnum++;
                ph=allp[i].realHeight;
                pshtml=allp[i].outerHTML;
            }
        }

        //最后一页
        if(ph> 0&&pshtml!=""){
            addPToDIV(ue,pshtml,ph,divnum);
        }

    }

    /**
     * 往ue里面添加数据（给当前页加数据）
     * @param ue
     * @param divps 插入的数据
     * @param ph 这个div里面的所有P的高度和
     * @param divnum
     */
    function addPToDIV_Element(ue,divps,ph,divnum){

        var cdivid=TOWORD.divpage.getpagedivid(divnum);
        TOWORD.divheightmap[cdivid]=ph;//给div的内部P集合赋高度
        var div=getDivByDivid(cdivid);
        if(null!=div){//如果存在直接写入，如果不存在就需要创建再写入
            for(var i=0;i<divps.length;i++){
                var pnewpage=document.createElement('p');
                pnewpage.innerHTML=divps[i].innerHTML;
                div.appendChild(pnewpage);//修改字符串为数组对象，innerHTML和style都上去就可以了
            }

        }else{
            console.log(cdivid+"--cdivid没有当前页？ 不可能。。。:");
        }
    }

    /**
     * 往ue里面添加数据
     * @param ue
     * @param divps 插入的数据
     * @param ph 这个div里面的所有P的高度和
     * @param divnum
     */
    function addPToDIV(ue,divps,ph,divnum){

        var cdivid=TOWORD.divpage.getpagedivid(divnum);
        TOWORD.divheightmap[cdivid]=ph;//给div的内部P集合赋高度
        var div=getDivByDivid(cdivid);
        if(null!=div){//如果存在直接写入，如果不存在就需要创建再写入,覆盖式写入
            div.innerHTML=divps;
        }else{
            TOWORD.divnum++;
            var divnewpage=document.createElement('div');
            divnewpage.style=TOWORD.divpage.divcss();
            divnewpage.id=cdivid;
            divnewpage.className=cdivid;
            divnewpage.innerHTML=divps;
            ue.body.appendChild(divnewpage);

            console.log(TOWORD.divnum+":TOWORD.divnum--addPToDIV --如果不相等的话就说明有问题--cdivid:"+cdivid);
        }
    }

    /**
     * 聚焦到指定的P节点的前面或者后面
     * @param div/P div节点
     * @param moveCursorBool 1：p的前面,2：p的后面
     */
    function focusp(ue,div,moveCursorBool){
        if(isNotEmpty(div)){
            var p;
            if (div.nodeName=='div'||div.nodeName=='DIV'){
                p = div.firstChild;
            }else{
                p=div;
            }
            var rng=ue.selection.getRange();
            if(moveCursorBool==1){
                rng.setStart(p,0).setCursor(false,true);//第一个节点之前
            }else if(moveCursorBool==2){
                rng.setStart(p,1).setCursor(false,true);//第一个节点之后
            }
        }
        theCursoriNLeastP=-1;//还原聚焦方式
    }


    /**
     * election上暂时可以
     * in换算成px后期需要找到更好的方法
     * @param cm
     * @returns {number}
     */
    function in2px(cm) {
        var pixel = parseFloat(cm)*100;
        return (parseInt(pixel))
    }


})();