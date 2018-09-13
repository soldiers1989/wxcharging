$(function() {
    FastClick.attach(document.body);
    var text=$('.text-show');
//            window.localStorage.clear();//清除本地数据缓存
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var isClick=false;
    var tartUrl="common/nearby.action";

   if(GetQueryString('tartHref')){
	   tartUrl=GetQueryString('tartHref');
   }
    $('.weui-btn_primary').on('click',function(e){//点击登录
        if(isClick){return}
        isClick=true;
        text.text('');
        var user=$('.userName').val();
        var pwd=$('.userPwd').val();
        var utext=$('.userName').closest('.weui-cell').find('.weui-label').text();
        var upwd=$('.userPwd').closest('.weui-cell').find('.weui-label').text();
        if(user===undefined||user.trim().length===0){
            text.text(utext+'不能为空');
            isClick=false;
        }else{
            if(pwd===undefined||pwd.trim().length===0){
            	text.text(upwd+'不能为空');
        	    isClick=false;
            }else{
            	$.showLoading('登陆中');
                //登录成功
            	getPages("frame/frame_queryAccountLogin.action",
          			{
            		 	openId:storage.getItem('openid'),
            		    account:$('.userName').val(),//账户
            		    password:$('.userPwd').val()//密码
          			},function(res){
          				 
          				 if(res.ret===0){//如果成功
          					 if(res.data.failReason===0){
          						 $.hideLoading();
          						 $.toast('登录成功',800,function() {
          							storage.setItem('accountKey',res.data.accountKey);//缓存accountKey
                                    /*window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+(redirectUri+tartUrl)+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";*/
          							window.location.href=tartUrl;
          							isClick=false;
                                 });
          						
          					 }else{
          						 $.hideLoading();
          						 $.toast(res.data.failReasonDesc,"cancel",function() {
          							isClick=false;
          						 });
          					 }
                              
          				 }else{
          					 $.hideLoading();
                              $.toast(res.msg,"cancel",function(toast) {
                            	  isClick=false;
                              });
          				 }
          				
          			},function(msg){
          				 $.hideLoading();
          				 $.toast(msg,"cancel",function(toast) {
          					isClick=false;
                           });
          			},function(){
          				
          			});
            }
        }

    })
});