/**
 * 这个js是用于二次开发ueditor，使ueditor更贴切Word的操作
 */
(function(){

    window.TOWORD=this;

    TOWORD.divheightmap = {};

    TOWORD.pagemaxheight=900;//每页最大的可编辑区域的高度
    TOWORD.margintopandbutton=5;//p标签的margin值
    TOWORD.notneedwordrun=false;//不需要监控键盘操作的时候给true

    TOWORD.pmaxlineheight=150;//每页最大的一行只可能是150px

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
                TOWORD.notneedwordrun=true;
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
                            checkAndDealSpanHeight(divps[n]);
                        }
                        divps=div.childNodes;//重新获取

                        for(var n=0;n<divps.length ;n++){

                            //检测子节点是否有大于最大div高度的节点


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
                                var outerHTML=divps[n].outerHTML;
                                if(n==divps.length-1){//最后一个元素
                                    if(divps[n].innerHTML==''||divps[n].innerHTML=='<br>'||divps[n].innerHTML=='<BR>'){//是理论上的无用段，就直接去掉
                                        console.log("不需要再新建页为这一个不用段落，outerHTML："+outerHTML);
                                    }else{
                                        divshtml+=divhtmlstart.replace("&nestdivid",TOWORD.divpage.getnextpagedivid())+outerHTML;
                                    }
                                }else{
                                    divshtml+=divhtmlstart.replace("&nestdivid",TOWORD.divpage.getnextpagedivid())+outerHTML;
                                }

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

                TOWORD.notneedwordrun=false;//解除工作状态

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

                //首先检测当前div的子元素是否有超标的，有就需要处理

                var div=getDivByDivid(divid);
                var divps=div.childNodes;
                for(var i=0;i<divps.length;i++){
                    checkPHeight(ue,divps[i]);
                }

                //几种情况
                //组合结果：
                // 1、P总高大了，有下一页；(不需要判断这一页是否到达900的编辑极限)
                // 2、P总高小了，有下一页；(要考虑下一页往上一页补充一P是否会超过900)
                // 3、P总高大了，没有下一页；（要考虑，超过就分页，最后一个P写入下一页）
                // 4、P总高小了，没有下一页；(不需要判断这一页是否到达900的编辑极限)
                // （先按照不拆分最后或最开始的P用于补充上一页/填充下一页；最后一定要考虑当前页最后一个P和下一页的第一个P在需要重新分页的时候的处理）
                //（当剩下的P进行重新分页时，P为空的情况）

                if(oldheight<newheight || newheight >= TOWORD.pagemaxheight){//P总高大了

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
                        for(var i=0;i<POBJs.length;i++){
                            newps.push(POBJs[i]);//当前页剩下的P重新分页
                        }
                        newps=getNextAllPEvt(TOWORD.currentdivnum,newps);
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
                        newps=getNextAllPEvt(TOWORD.currentdivnum,newps);//下一页开始的所有页的子节点的集合

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
                                    }else{
                                        console.log(ph+"：ph--不用往上一个div加P，divnum："+TOWORD.currentdivnum);
                                        TOWORD.divheightmap[divid]=newheight;//下一页的第一个p的高度太高抬不上去也要把这个页面的的高度给它重置一下
                                    }
                                    break ;//把当前页加满了就跳出循环
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

            },
            moreMaxHeight:function (ue,pseight_old,psheight,divid){//以后可能改，现在就用这个
                TOWORD.page.reTypesetting(ue,pseight_old,psheight,divid);
            },
            checkAndDealSpanHeight:function (node,bool){

                checkPHeight(ue,node);

                //bool询问是否需要先判断该页的高度
                if(isNotEmpty(bool)&&bool){
                    //先判断该节点的div的高度
                    var div=getDivByChildNode(node);
                    if(isNotEmpty(div)){
                        var divid=div.id;
                         var ht=getAllPHeightByDivid(divid);
                         if(ht > TOWORD.pagemaxheight){

                             //看是否存在下一页
                             var currentdivnum=parseInt(divid.replace(/[^0-9]/ig,""));
                             var nextdividnum=currentdivnum+1;
                             var nextdivid=TOWORD.divpage.getpagedivid(nextdividnum);//下一页的ID
                             if(checkExistByDivid(nextdivid)){//判断是否存在

                                 //情况1
                                 //获取本页最后一个P,缓存p，并删除P，以及以下所有页的P；
                                 // 删除除了下一页的以下所有页，清空下一页，修改缓存页数；
                                 // 在下一页中进行数据导入并分页；

                                 var newps=new Array();
                                 var POBJs=getLeastPEvtAndDelByDivid(ue,divid,ht);//最后几个子节点
                                 for(var i=0;i<POBJs.length;i++){
                                     newps.push(POBJs[i]);//当前页剩下的P重新分页
                                 }
                                 newps=getNextAllPEvt(currentdivnum,newps);
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
                                 var POBJs=getLeastPEvtAndDelByDivid(ue,divid,ht);//最后一个P
                                 //需要判断是在第一个P的开头还是结尾
                                 TOWORD.divnum=nextdividnum;//修改缓存页数
                                 //重新分页
                                 againFY(ue,POBJs);

                             }

                         }
                    }else{
                        console.log("该节点不在div分页内？？node："+node.nodeName);
                        return ;
                    }
                }

            },
            checkDivAndChirldNodeHeight : function(){
                if(!TOWORD.notneedwordrun){//导入Word是非工作的状态下才会检测是否需要重新排版

                    //为什么先检查子节点的高度再检查div的高度，
                    // 因为子节点检查完后可能会导致div的总高度增加

                    //检测所在子节点的高度
                    //如果段落高度大于子节点的最大高度，就需要判断子节点的高度问题
                    var cchecknode=ue.selection.getRange().startContainer;
                    checkPHeight(ue,cchecknode);
                    console.log(cchecknode+":cchecknode-----");

                    //检测该页div中所有p的高度
                    //找到div所有的P，计算所有P的总高度

                    var divid=TOWORD.util.getDivIdByUE(ue);
                    if(!isNotEmpty(divid)){
                        return;
                    }else{
                        TOWORD.currentdivnum=parseInt(divid.replace(/[^0-9]/ig,""));
                    }
                    var psheight=TOWORD.page.getAllPHeightByDivid(divid);
                    var pseight_old=TOWORD.divheightmap[divid];
                    var pMaxHeight=TOWORD.pagemaxheight-TOWORD.pmaxlineheight;
                    if(isNotEmpty(psheight)){
                        //对比上一次的高度，有变化的话，触发重新排版
                        if(isNotEmpty(pseight_old)&&(psheight!=pseight_old)){
                            //重新排版，写入toWord里面
                            console.log("重新排版，写入toWord里面");
                            TOWORD.page.reTypesetting(ue,pseight_old,psheight,divid);
                        }else if(psheight>=TOWORD.pagemaxheight){
                            //重新排版，写入toWord里面
                            console.log("重新排版，写入toWord里面--2,psheight:"+psheight);
                            TOWORD.page.moreMaxHeight(ue,pseight_old,psheight,divid);
                        }
                        console.log(pseight_old+":pseight_old----pseight:"+psheight+"---divid:"+divid);
                    }

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
            },
            cleardoc:function(){
                //清空整个doc，重置div page
                TOWORD.divnum=0;
                var divshtml="<div tabindex=\"0\" hidefocus=\"true\"  id='"+TOWORD.divpage.getnextpagedivid()+"' style= '"+TOWORD.divpage.divcss()+"'><p ></p></div>";
                UE.getEditor('editor').setContent(divshtml,false);
            }
        };





    }();

    TOWORD.util=function(){

        return {
            getlineHeight:function(ue){
                return getlineHeight(ue);
            },
            getDivIdByUE:function(ue){
                return getDivIdByUE(ue);
            },
            getpByRange:function (ue) {
                return getpByRange(ue);
            },
            checkPHeight:function (ue,p) {
                checkPHeight(ue,p);
            },
            getBeforeCLineHeight:function(node){
                return getBeforeCLineHeight(node);
            }
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
     * 获取当前div中正在编辑的行以上的所有元素的高度和
     * @param ue
     * @returns {string}
     */
    function getBeforeCLineHeight(node) {

        var heightBeforeCLine=0;
        if(isNotEmpty(node)){
            var childNode=node;
            var parentNode=childNode.parentNode;
            do{
                if(isNotEmpty(parentNode)){
                    if(isNotEmpty(parentNode.id)&&parentNode.id.indexOf("newpage") > -1){

                        return childNode.offsetTop;
                    }else{
                        childNode=parentNode;
                        parentNode=childNode.parentNode;
                    }
                }else{
                    console.log("没有父节点了");
                    break;
                }

            }while(isNotEmpty(parentNode));

            console.log("感觉这个节点不是用于page里面写数据的节点childNode:"+childNode.nodeName);
        }else{
            console.log("传过来的节点是个空节点");
        }

        return null;
    }

    /**
     * 获取当前正在编辑的p的 父节点div
     * @param ue
     * @returns {string}
     */
    function getDivIdByUE(ue) {
        //用循环的方式获取div
        var childNode=ue.selection.getRange().startContainer;
        var parentNode=childNode.parentNode;
        do{
            if(isNotEmpty(parentNode)){
                if(isNotEmpty(parentNode.id)&&parentNode.id.indexOf("newpage") > -1){

                    return parentNode.id;
                }else{
                    childNode=parentNode;
                    parentNode=childNode.parentNode;
                }
            }else{
                console.log("没有父节点了");
                break;
            }

        }while(isNotEmpty(parentNode));
        console.log("没有找到正在编辑的div的ID");
        return null;

    }

    /**
     *
     * 获取当前光标所在的P标签
     * @param ue
     * @returns {*}
     */
    function getpByRange(ue) {
        var range=ue.selection.getRange().startContainer;
        if(range.nodeName=='body'||range.nodeName=='BODY'){
            console.log('应该是全选，返回null');
            return null;
        }
        //用循环的方式获取P
        var childNode=range;
        var parentNode=childNode.parentNode;
        do{
            if(isNotEmpty(parentNode)){
                if(isNotEmpty(parentNode.id)&&parentNode.id.indexOf("newpage") > -1){

                    return childNode;
                }else{
                    childNode=parentNode;
                    parentNode=childNode.parentNode;
                }
            }else{
                console.log("没有父节点了");
                break;
            }

        }while(isNotEmpty(parentNode));

        console.log("没有找到正在编辑的p-------------请注意");
        return null;

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

                //如果
                var pht=getNodeHeightByNode(leastp);

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

                //var pMaxHeight=TOWORD.pagemaxheight-TOWORD.pmaxlineheight;//每一个P段落最大的高度750


                var POBJ={
                    realHeight:pht,//节点真实占用高度
                    outerHTML:leastp.outerHTML,
                    innerHTML:leastp.innerHTML,
                    style:leastp.style,
                    num:0+i
                };
                var parentNode=leastp.parentNode;
                parentNode.removeChild(leastp);//删除最后一个P
                moreps.push(POBJ);

                rvnodeht+=pht;
                var realnewheight=newheight-rvnodeht;
                if((realnewheight) < TOWORD.pagemaxheight){//判断减去子元素后是否小于最大子元素高度和，小的话就直接出去

                    //找到当前p,分拆p，现阶段只做简单的分拆2个P
                    // var newps=moreMaxHeightDealP(leastp,realnewheight);//leastp一定不能出现不在子节点下的文字12234234<span>sdfsdf</span>,这样是不允许的
                    // if(null!=newps&&newps.length > 1 ){
                    //
                    //     leastp.innerHTML='';//先清空
                    //
                    //     for (var s=0;s<newps.length;s++){
                    //         var sOBJ=newps[s];
                    //         if((realnewheight+sOBJ.realHeight) < TOWORD.pagemaxheight){
                    //             realnewheight+=sOBJ.realHeight;
                    //             leastp.append(sOBJ.outerHTML);//添加上去
                    //         }else{//放到下一个页面的数据
                    //             moreps.push(sOBJ);
                    //         }
                    //     }
                    //
                    // }else{
                    //     var POBJ={
                    //         realHeight:pht,//节点真实占用高度
                    //         outerHTML:leastp.outerHTML,
                    //         innerHTML:leastp.innerHTML,
                    //         style:leastp.style,
                    //         num:0+i
                    //     };
                    //     parentNode.removeChild(leastp);//删除最后一个P
                    //     moreps.push(POBJ);
                    //
                    // }

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

    //当前位置是光标的检测
    /**
     * 检测当前光标是不是在节点上
     * @param ue
     * @param node
     */
    function checkCurrentPosition(ue,node){

        var bool=false;
        //判断光标所在位置
        var range=ue.selection.getRange();
        var bk_start = range.createBookmark().start; // 创建一个临时dom，用于获取当前光标
        bk_start.style.display = ''; // 设置临时dom不隐藏
        var position = UE.dom.domUtils.getPosition(node,bk_start);
        if(position==16||position==20){

            bool=true;
        }
        $(bk_start).remove(); // 移除临时dom

        return bool;
    }

    /**
     * 失效，暂停使用
     * p大于div最大高度，大于的话就需要切割高度组成多个P并返回
     * @param p 当前P
     * @param pheight 当前P的高度
     * @param cdivBeforePHeight 当前div页这个p之前的所有P的高度和
     */
    function moreMaxHeightDealP(p,cdivBeforePHeight){

        var parr=new Array();

        var pstyle=p.style;
        var childNodes=p.childNodes;//一般都是span
        if(null!=childNodes&&childNodes.length > 1){
            var newp='<p style="'+pstyle+'">';
            var newp_inner='';
            //从上往下组P，高度从cdivBeforePHeight算起，大于最大div高度就分p,
            i=0;
            var newpHeight=cdivBeforePHeight;
            var endp='</p>';

            while(i<childNodes.length){

                var cnode=childNodes[i];
                var nodeh=cnode.scrollHeight;
                if(nodeh>=TOWORD.pagemaxheight){//一个span的高度都大于div的最大高度，需要算字数，一个个减来处理，暂时不处理，直接对半处理
                    console.log("最不想看见的问题 nodeh>=TOWORD.pagemaxheight 一个span的高度都大于div的最大高度，需要算字数，一个个减来处理，暂时不处理，直接对半处理")

                }else if((nodeh+newpHeight) >= TOWORD.pagemaxheight){//说明从这个span开始，就是下一个p
                    //组上一个P，开始下一个P
                    if(isNotEmpty(newp_inner)){
                        newp+=newp_inner+endp;
                        var POBJ={
                            realHeight:newpHeight,//节点真实占用高度
                            outerHTML:newp,
                            innerHTML:newp_inner,
                            style:pstyle,
                            num:0+i
                        };
                        parr.push(POBJ);
                    }
                    newp_inner=cnode.outerHTML;
                    newpHeight=nodeh;//第二个P的高度从0算起
                }else{//需要加入更多span
                    newp_inner+=cnode.outerHTML;
                    newpHeight+=nodeh;
                }

                if(i=childNodes.length-1){//最后一个，组成最后一个p

                    if(isNotEmpty(newp_inner)){
                        newp+=newp_inner+endp;
                        var POBJ={
                            realHeight:newpHeight,//节点真实占用高度
                            outerHTML:newp,
                            innerHTML:newp_inner,
                            style:pstyle,
                            num:0+i
                        };
                        parr.push(POBJ);
                    }
                    break;
                }else{
                    i++;
                }
            }
            return (parr.length > 0)?parr:null;
        }else{
            //需要算字数，一个个减来处理，暂时不处理，直接对半处理
            console.log('childNodes.length <= 1需要算字数，一个个减来处理，暂时不处理，直接对半处理');
        }

        return null;

    }



    /**
     * 获取当前divID的以下的所有页的所有标签
     */
    function getNextAllPEvt(currentdivnum,newps){

        var nextdividnum=currentdivnum+1;
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
     * 通过子节点获取div，最多4级子节点
     */
    function getDivByChildNode(node){
        var range2=node.parentNode;
        if(range2.nodeName=='div'||range2.nodeName=='DIV'){
            return range2;
        }
        var range3=range2.parentNode;
        if(null!=range3&&(range3.nodeName=='div'||range3.nodeName=='DIV')){
            return range3;
        }
        var range4=range3.parentNode;
        if(null!=range4&&(range4.nodeName=='div'||range4.nodeName=='DIV')){
            return range4;
        }
        var range5=range4.parentNode;
        if(null!=range5&&(range5.nodeName=='div'||range5.nodeName=='DIV')){
            return range5;
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
     * 返回该节点的实际占用高度
     * @param cnode 节点
     * @returns {*}
     */
    function getNodeHeightByNode(cnode){

        if(null==cnode||cnode.nodeType==3){
            console.log("getNodeHeightByNode cnode is null或者cnode.nodeType==3,警告");
            return 0;
        }
        var nodeheight=cnode.scrollHeight;//节点本身的内部高度
        if(!isNotEmpty(nodeheight)||nodeheight==0){
            nodeheight=cnode.offsetHeight;//保险起见
        }
        //判断一下是否有隐形的高度样式
        var marginBottom=cnode.style.marginBottom;
        if(isNotEmpty(marginBottom)){
            if(marginBottom.indexOf("in")> -1||marginBottom.indexOf("em")> -1){
                nodeheight+=in2px(marginBottom);//这里可能还需要做细致的转换处理，in,em之类的单位转px
            }else if(marginBottom.indexOf("pt")> -1||marginBottom.indexOf("PT")> -1){
                nodeheight+=pt2px(marginBottom);
            }else {
                nodeheight+=parseInt(marginBottom.replace(/[^0-9]/ig,""));
            }
        }else{//如果节点有margintopandbutton/marginTop就不需要上级节点的margin属性
            nodeheight+=TOWORD.margintopandbutton;//p的高度加上margin的高度
        }
        var marginTop=cnode.style.marginTop;
        if(isNotEmpty(marginTop)){
            if(marginTop.indexOf("in")> -1||marginTop.indexOf("em")> -1){
                nodeheight+=in2px(marginTop);
            }else if(marginTop.indexOf("pt")> -1||marginTop.indexOf("PT")> -1){
                nodeheight+=pt2px(marginTop);
            }else {//这里可能还需要做细致的转换处理，in,em之类的单位转px
                nodeheight+=parseInt(marginTop.replace(/[^0-9]/ig,""));
            }
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
                // pnewpage.innerHTML=divps[i].innerHTML;
                // pnewpage.style=divps[i].style.cssText;
                div.appendChild(pnewpage);//修改字符串为数组对象，innerHTML和style都上去就可以了
                pnewpage.outerHTML=divps[i].outerHTML;
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

    /**
     * election上暂时可以
     * PT换算成px后期需要找到更好的方法
     * @param pt
     * @returns {number}
     */
    function pt2px(pt) {
        var pixel = parseFloat(pt)*16/12;
        return (parseInt(pixel))
    }


    /**
     * 检测节点高度是否超过警戒线，超过就自动分段
     * @param ue
     * @param p 可以不填，不填就检测当前光标所在的p的节点，填了就是填写的节点
     */
    function checkPHeight(ue,p){
        var realp;
        if(isNotEmpty(p)){
            realp=p;
        }else{
            realp=getpByRange(ue);
        }
        if(!isNotEmpty(realp)){
            console.log('没有找到对应的处理节点，直接返回');
            return ;
        }
        var realpmaxheight=TOWORD.pagemaxheight-TOWORD.pmaxlineheight;//真实的最大子节点高度
        var nodeHeight=getNodeHeightByNode(realp);

        // console.log(realpmaxheight+"：realpmaxheight，---checkPHeight---，nodeHeight："+nodeHeight);
        if(nodeHeight >= realpmaxheight){
            console.log(realpmaxheight+"：realpmaxheight，子节点高度已经超过警戒线，分段开始，nodeHeight："+nodeHeight);

            var newp=document.createElement(realp.nodeName);

            var nodes=realp.childNodes;
            for(var i=nodes.length-1;i>=0;i--){
                var node=nodes[i];
                var nodeHeight=node.offsetHeight;
                console.log(nodeHeight+"：node.height，p.childNodes");
                if(nodeHeight > 0&&nodeHeight<realpmaxheight){

                    var bool=checkCurrentPosition(ue,node);
                    newp.innerHTML=node.outerHTML;
                    newp.style=realp.style.cssText;
                    node.parentNode.removeChild(node);

                    insertAfterChildNode(newp,realp,bool);

                    var starttime=realp.getAttribute('starttime');
                    if(isNotEmpty(starttime)){
                        newp.setAttribute('starttime',starttime);
                        realp.removeAttribute('starttime');
                    }
                    var usertype=realp.getAttribute('usertype');
                    if(isNotEmpty(usertype)){
                        newp.setAttribute('usertype',usertype);
                        realp.removeAttribute('usertype');
                    }

                    return;
                }else if(nodeHeight > 0&&nodeHeight>=realpmaxheight){
                    var snodes=node.childNodes;
                    var addht=0;
                    for(var i=snodes.length-1;i>=0;i--){
                        var snode=snodes[i];
                        var nodeHeight=snode.offsetHeight;
                        console.log(nodeHeight+"：snode.height，node.childNodes");
                        if(nodeHeight > 0){

                            var bool=checkCurrentPosition(ue,snode);
                            newp.innerHTML=snode.outerHTML;
                            newp.style=realp.style.cssText;
                            snode.parentNode.removeChild(snode);
                            //要判断是不是当前光标位置
                            insertAfterChildNode(newp,realp,bool);


                            var starttime=realp.getAttribute('starttime');
                            if(isNotEmpty(starttime)){
                                newp.setAttribute('starttime',starttime);
                                realp.removeAttribute('starttime');
                            }
                            var usertype=realp.getAttribute('usertype');
                            if(isNotEmpty(usertype)){
                                newp.setAttribute('usertype',usertype);
                                realp.removeAttribute('usertype');
                            }

                            return ;
                        }
                    }
                }
            }

        }else{
            console.log(realpmaxheight+"：realpmaxheight，子节点高度没有超过警戒线，不用分段，nodeHeight："+nodeHeight);
        }

    }

    /**
     *  往节点后追加一个同级节点
     * @param newp 新节点
     * @param realp 老节点
     * @param isworkp 是否需要光标聚焦
     */
    function insertAfterChildNode(newp,realp,isworkp) {
        $(newp).insertAfter(realp);
        if(isworkp){//只有使用当前光标所在的p才会去考虑光标定位
            var rng=ue.selection.getRange();
            rng.setStart(newp,1).setCursor(false,true);//第一个节点之后
        }
        //更新div高度
        var div=getDivByChildNode(realp);
        if(isNotEmpty(div)){
            TOWORD.divheightmap[div.id]=getAllPHeightByDivid(div.id);
        }
    }


    /**
     *  检测和处理的文本输入高度
     * @param node 当前节点
     * @param pageHeightBeforeNode 本页当前节点之前的高度
     */
    function checkAndDealSpanHeight(node){

        if(node.nodeName=='BODY'||node.nodeName=='body'
            ||node.nodeName=='div'||node.nodeName=='DIV'
            ||node.nodeName=='WINDOWS'||node.nodeName=='Windows'||node.nodeName=='windows'){
            console.log(node.nodeName+":node.nodeName 怎么可能，开玩笑吧，这个类型");
            return ;
        }

        // var beforeheight=getBeforeCLineHeight(node);
        var beforeheight=0;
        if(null==beforeheight||beforeheight< 0){
            beforeheight=0;
        }else if(beforeheight >TOWORD.pmaxlineheight){
            console.log(beforeheight+":pageHeightBeforeNode 这不是扯淡吗，本页当前节点之前的高度还大于最大页面高度，那不早分页了");
            return ;
        }
        var nodeHeight=getNodeHeightByNode(node);
        var realnodemaxheight=TOWORD.pagemaxheight-TOWORD.pmaxlineheight-beforeheight;//这个节点真实的最大高度
        if(nodeHeight <= realnodemaxheight){
            console.log(nodeHeight+":nodeHeight 暂时不处理节点本身高度小于(div最大高度-本页当前节点之前的高度)");
            return ;
        }

        var newnodetext='';//多出来的节点的文本

        var style=null;//子节点的样式
        var nodetag='';
        var specialDeal='';//特殊的属性
        var spannode='';//子节点的HTML
        do{
            var node_innerhtml=node.innerText;
            if(!isNotEmpty(node_innerhtml)||node_innerhtml.length==0){
                console.log(node_innerhtml+":nodetest node节点文字为空，偶买噶");
                return ;
            }

            var dealnode;

            //去掉可能存在的空子节点，这里不需要处理其他事件
            var nodelastElementChild=node.lastElementChild;
            if(isNotEmpty(nodelastElementChild)&&nodelastElementChild!=3){//不是文本标签
                if(nodelastElementChild.innerText==""){
                    //空节点
                    node.removeChild(nodelastElementChild);
                    if(spannode!=''){
                        if(nodetag!=''){//这里就需要看他是否有子节点
                            spannode='<'+nodetag+' '+specialDeal+' style="'+style+'">'+spannode+'</'+nodetag+'>';
                            specialDeal='';
                        }
                        newnodetext=spannode+newnodetext;
                    }
                    style=null;//重置样式
                    spannode='';
                    console.log("这种情况出现就需要删除这个子节点，并新跑上一个节点")
                    continue;//跳过继续
                }else{ //在最后一个子节点上处理
                    dealnode=nodelastElementChild;
                    nodetag=nodelastElementChild.nodeName;
                }
            }else{ //说明没有子节点，只能在当前节点上处理
                dealnode=node;
            }

            var starttime=dealnode.getAttribute("starttime");
            if(isNotEmpty(starttime)){
                specialDeal="starttime='"+starttime+"'";
                dealnode.removeAttribute("starttime");
            }
            var uesrtype=dealnode.getAttribute("uesrtype");
            if(isNotEmpty(uesrtype)){
                specialDeal+=" uesrtype='"+uesrtype+"'";
                dealnode.removeAttribute("uesrtype");
            }

            if(null==style){
                style=dealnode.style.cssText;//当前节点样式
            }

            var dealtext=dealnode.innerText;
            var dealinnerhtml=dealnode.innerHTML;

            var leastchar=dealtext.charAt(dealtext.length-1);//获取最后一个字
            spannode=leastchar+spannode;//拼接删除的字符
            var m =dealinnerhtml.length;
            var n = dealinnerhtml.lastIndexOf(leastchar);
            dealinnerhtml=dealinnerhtml.substring(0,n);
            var j = dealinnerhtml.substring(n+1);
            dealinnerhtml=dealinnerhtml+j;
            dealnode.innerHTML=dealinnerhtml;

            nodeHeight=getNodeHeightByNode(node);//重新查询节点高度
            console.log(nodeHeight+":nodeHeight--node.style"+node.style);
        }while(nodeHeight >= realnodemaxheight);

        if(spannode!=''){
            if(nodetag!=''){//这里就需要看他是否有子节点
                spannode='<'+nodetag+' '+specialDeal+' style="'+style+'">'+spannode+'</'+nodetag+'>';
            }
            newnodetext=spannode+newnodetext;
        }
        if(newnodetext!=''){

            //可能也会超过div最大高度，再来一遍就是了
            var newp=document.createElement(node.nodeName);
            newp.innerHTML=newnodetext;
            newp.style=node.style.cssText;
            newp.style.textIndent='';//不要首行缩进，因为这一段只是上一段的补充

            insertAfterChildNode(newp,node,false);


            var starttime=node.getAttribute('starttime');
            if(isNotEmpty(starttime)){
                newp.setAttribute('starttime',starttime);
                node.removeAttribute('starttime');
            }
            var usertype=node.getAttribute('usertype');
            if(isNotEmpty(usertype)){
                newp.setAttribute('usertype',usertype);
                node.removeAttribute('usertype');
            }


        }
    }



})();