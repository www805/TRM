YUI({
	combine:true,comboBase:"http://img6.cache.netease.com/service/combo?",groups:[{
		base:"http://img6.cache.netease.com/video/js/build/",modules:{
			"unicorn-tabs":{
				requires:["widget","arraylist","plugin","transition"]
			}
		}
	}
	]
}).use("node-screen","unicorn-tabs","jsonp","json-parse","cookie","io-base","event","anim","overlay","slider","event-valuechange",function(b){
	var G=b.one("#js_VideoList"),d=b.one("#js_videoShare"),c=location.pathname,F,q=true,D=b.Cookie.get("CONTINUE_PLAY"),y=/(iPhone|iPad|iPod)/ig.test(navigator.userAgent),E,C,x,L=b.one("#moviePoint"),w=b.one("#js_continuePlay"),v=b.one("#myVideo"),u=b.one("#continuePlayTips"),s=b.one("#continuePlayTips .time"),h,t,N,l,M=b.one("#vote .ding"),O=b.one("#vote .cai"),J=b.one("#video"),a=b.one("#switchButton"),I;
	if(window.location.href.indexOf("zongyi")>0){
		pltype=4
	}
	else{
		if(window.location.href.indexOf("zixun")>0){
			pltype=6
		}
	}
	if(L){
		var A=L.one(".ui-point-placeholder"),g=L.one(".ui-point-text"),j=L.one(".ui-point-submit"),n=L.one(".ui-point-word"),k=false,o,B=L.one(".ui-point-tip"),z=b.Node.create('<div class="ui-overlay hidden" id="helpOverlay"><h2 class="f14px"></h2><div class="ui-overlay-buttons"><a href="#" class="f14px ok" hidefocus="true" >确定</a></div></div>'),H;
		g.set("value","");
		z.appendTo("body");
		H=new b.Overlay({
			srcNode:z,visible:false,zIndex:10000,render:true
		});
		z.removeClass("hidden")
	}
	window.getSource=function(){
		var P=location.href,Q=null;
		if(P.lastIndexOf("?")>0){
			Q=P.substr(P.lastIndexOf("?")+1)
		}
		return Q
	};
	window.continuePlay=function(){
		var P=b.one("#js_continuePlay").get("checked");
		if(P&&t){
			window.location=N
		}
	};
	window.flashLoadTime=function(){
		flashLoadEnd=new Date()
	};
	window.sendRecord=function(){
		if(logined){
			var T="http://me.CuPlayer.com/v/usercenter/addUserMovieDetail.do",Q=0,S,P,R=document.FPlayer||window.FPlayer;
			sendInterval=b.later(60000,this,function(){
				S=R.getTimeStr();
				S=S.split("_");
				if(S[2]=="true"){
					Q=1
				}
				b.jsonp(T+"?callback={callback}&userId="+userId+"&movieId="+S[0]+"&viewedTimeSpan="+S[1]+"&endTag="+Q+"&pltype="+S[3],function(){
					return true
				})
			}
			,"",true)
		}
	};
	window.hideTips=function(){
		if(t){
			u.transition({
				opacity:0,duration:0.8
			}
			,function(){
				this.addClass("hidden")
			});
			h&&h.cancel()
		}
	};
	window.showTips=function(Q){
		var S=b.one("#js_continuePlay").get("checked");
		if(t&&S){
			var R=Q-1,P=b.one(".movie-area").getDOMNode().getBoundingClientRect().top;
			s.setContent(Q);
			u.one(".title").setContent(l);
			u.one("a").set("href",N);
			u.removeClass("hidden").transition({
				opacity:1,duration:0.8
			});
			u.setStyle("marginLeft",-1*u.get("offsetWidth")/2-27+"px");
			if(P>0){
				u.setStyle("top",P+5)
			}
			else{
				if(b.UA.ie&&b.UA.ie<=6){
					u.setStyle("top",b.one(document).get("scrollTop")+5)
				}
			}
			h=b.later(1000,this,function(){
				s.setContent(R);
				R--;
				if(R<0){
					h.cancel()
				}
				u.one(".close").on("click",function(){
					hideTips()
				})
			}
			,null,true)
		}
	};
	function p(){
		var P=J.one(".video");
		if(J.getStyle("width")=="640px"){
			J.setStyle("overflow","hidden");
			P.setStyle("width","960px");
			J.transition({
				duration:0.4,width:"960px"
			}
			,function(){
				a.addClass("video-movie-switch-on")
			});
			L&&L.transition({
				duration:0.4,width:"958px"
			})
		}
		else{
			if(J.getStyle("width")=="960px"){
				a.removeClass("video-movie-switch-on");
				P.setStyle("width","640px");
				J.transition({
					duration:0.4,width:"640px"
				}
				,function(){
					J.setStyle("overflow","visible")
				});
				L&&L.transition({
					duration:0.4,width:"638px"
				})
			}
		}
	}
	function f(Q){
		var S=0,P=0,R=0,T;
		if(Q>=3600){
			S=Math.floor(Q/3600);
			if(Q-S*3600>0){
				P=Math.floor((Q-S*3600)/60);
				if(Q-S*3600-P*60>0){
					R=Q-S*3600-P*60
				}
			}
			T=S+"小时"+P+"分"+R+"秒"
		}
		else{
			if(Q>=60){
				P=Math.floor(Q/60);
				if(Q-P*60>0){
					R=Q-P*60
				}
				T=P+"分"+R+"秒"
			}
			else{
				R=Q;
				T=R+"秒"
			}
		}
		return T
	}
	window.Slider=function(S,U,T,V,Q,P,R){
		this.sliderAnim=new b.Anim({
			node:S,duration:0.3,to:{
				left:0
			}
		});
		this.node=S;
		this.currIndex=0;
		this.amount=Q-V;
		this.flip=U;
		this.padding=T;
		this.countShowed=V;
		this.indexNum=P;
		S.setStyle("width",Q*this.flip);
		R&&R.setContent(Q)
	};
	Slider.prototype.loadImage=function(){
		var P=this;
		if(!P.node.hasClass("imageLoaded")){
			P.node.all("img").each(function(R){
				var Q=R.getAttribute("data-src");
				if(Q){
					R.setAttribute("src",Q)
				}
			});
			P.node.addClass("imageLoaded")
		}
	};
	Slider.prototype.scrollLeft=function(R,Q){
		var P=this;
		R.on("click",function(T){
			T.preventDefault();
			if(P.amount>1){
				P.currIndex-=-Q;
				if(P.currIndex>0){
					if(P.currIndex<Q){
						P.currIndex=0
					}
					else{
						P.currIndex=1-P.amount
					}
				}
				var S=P.flip*P.currIndex;
				P.sliderAnim.set("to",{
					left:S+P.padding
				}).run();
				P.indexNum&&P.indexNum.setContent(-1*P.currIndex+1)
			}
			P.loadImage()
		})
	};
	Slider.prototype.scrollRight=function(R,Q){
		var P=this;
		R.on("click",function(T){
			T.preventDefault();
			if(P.amount>1){
				if(P.currIndex%Q!=0){
					P.currIndex=0
				}
				else{
					P.currIndex-=Q;
					if(P.currIndex<1-P.amount){
						if((P.amount-1)%(P.countShowed+1)!=0){
							P.currIndex=P.currIndex+Q-(P.amount-1)%(P.countShowed+1)
						}
						else{
							P.currIndex=0
						}
					}
				}
				var S=P.flip*P.currIndex;
				P.sliderAnim.set("to",{
					left:S+P.padding
				}).run();
				P.indexNum&&P.indexNum.setContent(-1*P.currIndex+1)
			}
			P.loadImage()
		})
	};
	function r(){
		var U=g.get("value"),S=U.length,Q,P=/[^x00-xff]/g,T=U.replace(P,"aa").length,R;
		if(T>50){
			for(R=25;R<S;R++){
				Q=U.substr(0,R).replace(P,"**");
				if(Q.length>=50){
					g.set("value",U.substr(0,R));
					break
				}
			}
			T=50
		}
		n.one("span").setContent(25-Math.floor(T/2))
	}
	function i(P){
		P.preventDefault();
		H.hide();
		mask.setStyle("display","none");
		closeEvent.detach()
	}
	window.shanSanXia=function(){
		L.setStyle("visibility","visible").transition({
			opacity:1,duration:0.8
		});
		if(!b.Cookie.get("VIDEO_POINT")){
			B.removeClass("hidden")
		}
	};
	function m(){
		var S=g.get("value"),R=L.one("form"),Q=document.FPlayer||window.FPlayer,P;
		if(S){
			if(!k){
				k=true;
				b.later(5000,this,function(){
					k=false
				});
				new b.Get.script(sendPointUrl+"&longth="+o+"&comment="+encodeURIComponent(S)+"&pltype="+pltype,{
					onSuccess:function(){
						if(P){
							if(P.info!=="提交成功"){
								z.one("h2").setContent(P.info);
								mask.setStyle("display","block");
								H.centered();
								H.show();
								closeEvent=b.on("click",i,"#helpOverlay .ok")
							}
						}
					}
					,onFailure:function(){},timeout:3000
				});
				Q.addReply(S,o);
				if(logined){
					g.set("value",S+"[来自视频看点"+f(o)+"]");
					R.submit()
				}
			}
			else{
				z.one("h2").setContent("请不要频繁提交！");
				mask.setStyle("display","block");
				H.centered();
				H.show();
				closeEvent=b.on("click",i,"#helpOverlay .ok")
			}
		}
		g.blur();
		g.set("value","");
		L.removeClass("ui-point-on");
		n.addClass("hidden");
		j.addClass("ui-point-submit-disable")
	}
	if(D=="0"&&w){
		w.replace(b.Node.create('<input type="checkbox" name="continuePlay" id="js_continuePlay"/>'))
	}
	function K(){
		var P;
		E=b.all("#js_VideoList>ul");
		C=b.all("#js_VideoList li");
		if(C){
			x=b.all("#js_VideoList li").size();
			G.all("li").on("hover",function(){
				if(!this.hasClass("on")){
					this.addClass("hover")
				}
			}
			,function(){
				this.removeClass("hover")
			});
			G.all("li").some(function(R){
				var Q=R.one("a").getAttribute("href");
				if(Q.indexOf("#")>0){
					trimedUrl=Q.substr(0,Q.indexOf("#"))
				}
				else{
					trimedUrl=Q
				}
				if(trimedUrl==c){
					F=R;
					R.addClass("on");
					R.all("a").each(function(S){
						S.setAttribute("href","javascript:;")
					});
					t=F.next("li");
					return true
				}
				else{
					t=G.one("li")
				}
			});
			if(t){
				N=t.one("a").getAttribute("href");
				l=t.one("h3").getContent();
				P=N.indexOf("#");
				if(P>0){
					N=N.substr(0,P)+"#zixunplay_continueplay"
					
				}
			}
			
		}
	}
	function e(){
		var P=this;
		P.body=G.one("ul");
		P.container=G.one("div");
		P._setSize()
	}
	e.prototype={
		_setSize:function(){
			var P=this,Q=P.body.get("offsetHeight"),R,S=P.container.get("offsetHeight"),T;
			if(Q>S){
				P.videoListSlider=new b.Slider({
					axis:"y",length:G.one("div").get("offsetHeight")-7,thumbUrl:"http://www.cuplayer.com/Player/CuSunPlayerV2/CuSunDemos/163/slider4.png"
				});
				R=S*S/Q;
				if(R<80){
					R=80
				}
				else{
					T='<span class="yui3-slider-thumb-bg" alt="Slider thumb bg" style=" height:'+(R-80)+'px">'
				}
				if(!P.step){
					P.step=111
				}
				P.videoListSlider.render("#videoListSlider");
				P.videoListSlider.thumb.setStyle("height",R);
				T&&P.videoListSlider.thumb.append(T);
				P._bindEvt()
			}
		}
		,_scrollBodyToPosition:function(Q){
			var S=this,R=S.container,P=S.body,T=P.get("offsetHeight")-R.get("offsetHeight");
			if(T<0){
				return
			}
			if(Q>0){
				Q=0
			}
			if(-1*Q>T){
				Q=-T
			}
			P.setStyle("top",Q)
		}
		,scrollByDistance:function(R){
			var Q=this,P=parseInt(Q.body.getStyle("top").replace("px",""))+R;
			Q._scrollBodyToPosition(P);
			Q._updateBar()
		}
		,scrollByPercent:function(Q){
			var P=this;
			Q=parseFloat(Q,10);
			if(isNaN(Q)||Q>1||Q<0){
				return
			}
			var R=(P.body.get("offsetHeight")-P.container.get("offsetHeight"))*(-Q);
			P._scrollBodyToPosition(R,1)
		}
		,_updateBar:function(){
			var R=this,U=R.videoListSlider,T=U.rail.get("offsetHeight")-U.thumb.get("offsetHeight"),P=R.body,Q=R.container,S=-1*parseInt(P.getStyle("top"))/(P.get("offsetHeight")-Q.get("offsetHeight"));
			U.thumb.setStyle("top",S*T)
		}
		,_bindContainer:function(){
			var R=this,P=R.body,Q=R.container,S=function(U){
				var T=parseInt(P.getStyle("top").replace("px",""));
				if(U>0&&T>=0){
					return false
				}
				if(U<0&&T+P.get("offsetHeight")<=Q.get("offsetHeight")){
					return false
				}
				return true
			};
			b.on("mousewheel",function(U){
				if(R.container.contains(U.target)){
					if(S(U.wheelDelta)){
						U.halt()
					}
					var T=R.step;
					R.scrollByDistance(U.wheelDelta>0?T:-T)
				}
			})
		}
		,_bindTrack:function(){
			var Q=this,R=Q.videoListSlider,P=R.rail;
			R.on("railMouseDown",function(S){
				Q.scrollByPercent((S.ev.pageY-P.getY())/(P.get("offsetHeight")))
			});
			P.on("mousedown",function(S){
				S.preventDefault()
			})
		}
		,_bindDrag:function(){
			var W=b.one(document),Q=this,R,V=0,U=Q.videoListSlider,S=U.thumb,P=U.rail,T=function(aa){
				var Y=P.get("offsetHeight"),ab=S.get("offsetHeight"),Z=Y-ab,X=V+(aa.pageY-R);
				if(X<0){
					X=0
				}
				if(X>Z){
					X=Z
				}
				Q.scrollByPercent(X/Z)
			};
			S.on("mousedown",function(X){
				V=parseInt(S.getStyle("top").replace("px",""))||0;
				R=X.pageY;
				W.on("mousemove",T);
				W.on("mouseup",function(){
					W.detach("mousemove",T);
					W.detach("mouseup",arguments.callee);
					R=0
				})
			})
		}
		,_bindEvt:function(){
			var P=this;
			P._bindContainer();
			P._bindTrack();
			P._bindDrag()
		}
	};
	
 
	 
	if(G){
		K();
		I=new e()
	}
	b.on("click",function(P){
		P.preventDefault();
		p()
	}
	,"#switchButton");
	b.on("click",function(){
		window.open(this.getAttribute("href"))
	}
	,".ui-rank .top50");
	b.all(".ui-rank-tab").each(function(P){
		new b.Tabs({
			srcNode:P,triggerEvent:"mouse",render:true
		})
	});
	b.all(".ui-rank").each(function(Q){
		var R=Q.all(".top50"),P=new b.Tabs({
			srcNode:Q,triggers:"h3",panels:".ui-rank-panel",render:true
		});
		P.after("selectedIndexChange",function(S){
			R.item(S.prevVal).addClass("hidden");
			R.item(S.newVal).removeClass("hidden")
		})
	});
	b.on("mouseover",function(P){
		if(!this.hasClass("yui3-tabs-trigger-selected")){
			this.addClass("on")
		}
	}
	,".ui-rank>h3");
	b.on("mouseout",function(P){
		this.removeClass("on")
	}
	,".ui-rank>h3");
	if(d){
		b.on("click",function(S){
			var T,Q,R=document.location,P;
			if(b.one("#js_Set").previous("a")){
				Q="《"+b.one("#js_Set").previous("a").get("innerHTML")+"》"+b.one("#js_Set").get("innerHTML")
			}
			else{
				Q="《"+b.one("#js_Set").get("innerHTML").replace("在线观看","")+"》"
			}
			T=encodeURIComponent("我在#酷播视频#观看了"+Q+"，"+R+"，很不错，分享给大家。");
			if(this.hasClass("shareBtn_tsohu")){
				P="http://t.sohu.com/third/post.jsp?url=&title="+encodeURIComponent("我在#酷播视频#观看了"+Q+"，"+R+"#sns_sohu，很不错，分享给大家。")+"&content=utf-8&pic="+moviePicture
			}
			else{
				if(this.hasClass("shareBtn_douban")){
					P="http://www.douban.com/recommend/?url="+encodeURIComponent(R+"#sns_douban")+"&title="+encodeURIComponent("我在酷播视频观看了"+Q+"，很不错，分享给大家。")+"&image="+moviePicture
				}
				else{
					if(this.hasClass("shareBtn_renren")){
						P="http://widget.renren.com/dialog/share?resourceUrl="+encodeURIComponent(R+"#sns_renren")+"&title="+encodeURIComponent("我在酷播视频观看了"+Q+"，很不错，分享给大家。")+"&images="+moviePicture+"&description="+encodeURIComponent(movieDescription)
					}
					else{
						if(this.hasClass("shareBtn_kaixin001")){
							P="http://www.kaixin001.com/repaste/bshare.php?rtitle="+encodeURIComponent(Q)+"&rcontent="+encodeURIComponent("我在酷播视频观看了"+Q+"，"+R+"#sns_kaixin，很不错，分享给大家。")+"&rurl="+encodeURIComponent(R+"#sns_kaixin")
						}
						else{
							if(this.hasClass("shareBtn_tCuPlayer")){
								P="http://t.CuPlayer.com/article/user/checkLogin.do?link=http://v.CuPlayer.com/&source="+encodeURIComponent("酷播视频")+"&info="+encodeURIComponent("我在#酷播视频#观看了"+Q+"，"+R+"，很不错，分享给大家。")+"&images="+moviePicture
							}
							else{
								if(this.hasClass("shareBtn_tsina")){
									P="http://v.t.sina.com.cn/share/share.php?&title="+encodeURIComponent("我在#酷播视频#观看了"+Q+"，"+R+"，很不错，分享给大家。")+"&pic="+moviePicture+"&appkey=2209444680"
								}
								else{
									if(this.hasClass("shareBtn_tqq")){
										P="http://v.t.qq.com/share/share.php?title="+encodeURIComponent("我在#酷播视频#观看了"+Q+"，很不错，分享给大家。")+"&pic="+moviePicture+"&url="+encodeURIComponent(R+"#sns_qqweibo")
									}
									else{
										if(this.hasClass("shareBtn_qzone")){
											P="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+encodeURIComponent(R+"#sns_qzone")+"&title="+encodeURIComponent("我在酷播视频观看了"+Q+"，很不错，分享给大家。")+"&pics="+moviePicture+"&summary="+encodeURIComponent(movieDescription)
										}
									}
								}
							}
						}
					}
				}
			}
			window.open(P,"_blank","width=700,height=600")
		}
		,"#js_videoShare a")
	}
	b.on("change",function(P){
		q=this.get("checked");
		if(q){
			b.Cookie.set("CONTINUE_PLAY","1",{
				expires:new Date("January 12, 2100"),path:"/"
			})
		}
		else{
			hideTips();
			b.Cookie.set("CONTINUE_PLAY","0",{
				expires:new Date("January 12, 2100"),path:"/"
			})
		}
	}
	,"#js_continuePlay");
	if(L){
		A.on("click",function(){
			var P=document.FPlayer||window.FPlayer;
			L.addClass("ui-point-on");
			g.focus();
			o=P.getSubTime()
		});
		g.on("blur",function(){
			var P=document.FPlayer||window.FPlayer;
			if(this.get("value")==""){
				L.removeClass("ui-point-on");
				j.addClass("ui-point-submit-disable");
				P.resumeMV()
			}
		});
		g.on("keyup",function(P){
			if(P.charCode==13){
				m()
			}
		});
		g.on("valueChange",function(P){
			if(this.get("value")){
				j.removeClass("ui-point-submit-disable");
				n.removeClass("hidden");
				r()
			}
			else{
				j.addClass("ui-point-submit-disable");
				n.addClass("hidden")
			}
		});
		j.on("click",function(P){
			P.preventDefault();
			m()
		});
		B.one(".close").on("click",function(){
			B.addClass("hidden");
			b.Cookie.set("VIDEO_POINT","popped",{
				expires:new Date("January 12, 2113"),path:"/"
			})
		})
	}
	b.on("click",function(S){
		var R=this,Q=b.all("#vote a"),P;
		S.preventDefault();
		 
		new b.Get.script(P,{
			onSuccess:function(){
				R.setContent(count+1);
				Q.each(function(T){
					if(T.hasClass("ding")){
						T.addClass("dingdisabled")
					}
					else{
						T.addClass("caidisabled")
					}
					T.set("href","javascript:")
				})
			}
		});
		Q.detach("click",this)
	}
	,"#vote a");
	b.on("click",function(Q){
		Q.preventDefault();
		var P=new b.Anim({
			node:b.UA.webkit?b.one("body"):b.one("html"),duration:0.5,easing:"easeOut",to:{
				scrollTop:b.one("#tieArea").getY()
			}
		});
		P.run()
	}
	,"#tieCount");
	b.on("domready",function(){
		if(a){
			if(isWidePlayer){
				p()
			}
		}
		if(F){
			I.scrollByDistance(G.getY()-F.getY())
		}
		if(v){
			if(v.all("li").size()<6){
				v.one(".v-plist-box").setStyle("height",v.all("li").size()*75)
			}
			slider=new Slider(v.one(".v-slider-body"),308,0,0,v.all("ul").size(),v.one(".crt"),v.one(".total"));
			slider.scrollLeft(v.one(".v-loop-left"),1);
			slider.scrollRight(v.one(".v-loop-right"),1);
			
		}
		 
		var P=b.one("#feedback"),Q=b.one("#star");
		P.setStyle("left",(Q.getX()+316)+"px");
		if(b.Node.getDOMNode(Q).getBoundingClientRect().top>0){
			P.setStyle("top",Q.getY()+"px")
		}
		else{
			P.setStyle("top",b.one(document).get("scrollTop"))
		}
		if(b.UA.ie&&b.UA.ie<=6){
			b.on("scroll",function(S){
				var R=b.Node.getDOMNode(Q).getBoundingClientRect().top;
				P.setStyle("left",(Q.getX()+316)+"px");
				if(R>0){
					P.setStyle("position","absolute");
					P.setStyle("top",Q.getY()+"px")
				}
				else{
					P.setStyle("top",b.one(document).get("scrollTop"))
				}
			})
		}
		else{
			b.on("scroll",function(S){
				var R=b.Node.getDOMNode(Q).getBoundingClientRect().top;
				P.setStyle("left",(Q.getX()+316)+"px");
				if(R>0){
					P.setStyle("position","absolute");
					P.setStyle("top",Q.getY()+"px")
				}
				else{
					P.setStyle("position","fixed");
					P.setStyle("top",0)
				}
			})
		}
		if(!y){
			if(!G){
				sendRecord()
			}
			b.later(30000,this,function(){
				var R=-1;
				if(flashLoadEnd&&(flashLoadEnd-flashLoadStart)<20000){
					R=flashLoadEnd-flashLoadStart
				}
			})
		}
	})
});