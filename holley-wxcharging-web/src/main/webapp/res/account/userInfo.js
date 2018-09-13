 $(function() {

     FastClick.attach(document.body);
     var storage= window.localStorage;
     storage = storage ? storage : [];

     var flag='- -';
     var tartUrl="common%2fnearby.action";
     $.showLoading('loading');
   
     console.log(storage);
     var imgIcon='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAC4AAAAuCAMAAABgZ9sFAAAAVFBMVEXx8fHMzMzr6+vn5+fv7+/t7e3d3d2+vr7W1tbHx8eysrKdnZ3p6enk5OTR0dG7u7u3t7ejo6PY2Njh4eHf39/T09PExMSvr6+goKCqqqqnp6e4uLgcLY/OAAAAnklEQVRIx+3RSRLDIAxE0QYhAbGZPNu5/z0zrXHiqiz5W72FqhqtVuuXAl3iOV7iPV/iSsAqZa9BS7YOmMXnNNX4TWGxRMn3R6SxRNgy0bzXOW8EBO8SAClsPdB3psqlvG+Lw7ONXg/pTld52BjgSSkA3PV2OOemjIDcZQWgVvONw60q7sIpR38EnHPSMDQ4MjDjLPozhAkGrVbr/z0ANjAF4AcbXmYAAAAASUVORK5CYII=';
     if(storage.getItem('headimgurl')===null){//若本地缓存是没有
    	 $.hideLoading();
    	 $.toast('获取用户头像失败','cancel',function() {
    		 $('.head-imgurl').prop('src',imgIcon);
         });
     }else{
    	 $.hideLoading();
    	 $('.head-imgurl').prop('src',(storage.getItem('headimgurl')===null)?imgIcon:storage.getItem('headimgurl'))
     }
    $('.nick-name').text((storage.getItem('nickname')===null)?flag:storage.getItem('nickname'))
    $('.user-city').text((storage.getItem('city')===null)?flag:storage.getItem('city'))
    $.showLoading('Loading');
    getPages("account/account_queryUserInfo.action",
			{
      		    accountKey:storage.getItem('accountKey')
			},function(res){
				 console.log(res);
				
				 if(res.ret===0){//如果成功
					 $('.user-name').text((res.data.userName===null)?flag:res.data.userName)
					 $('.user-phone').text((res.data.phone===null)?flag:res.data.phone)
					 $.hideLoading();
				 }else{
					 $.hideLoading();
                     $.toast(res.msg,"cancel",function(toast) {});
				 }
				
			},function(msg){
				 $.hideLoading();
				 $.toast(msg,"cancel",function(toast) {
                 });
			},function(){
				
			});
    
    $('.register-out').on('click',function(e){
    	   getPages("account/account_logout.action",
    				{
    		   			openId:storage.getItem('openid'),
    	      		    accountKey:storage.getItem('accountKey')
    				},function(res){
    					 console.log(res);					
    					 if(res.ret===0){//如果成功
    						 if(res.data.failReason===0){//成功
	    						 storage.removeItem('accountKey');
	    				     	 storage.removeItem('openid');
	    				    	 storage.removeItem('appId');
	    				    	 storage.removeItem('longitude');
	    				    	 /*window.location.href="common/nearby.action";*/
	    				    	 window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+(redirectUri+tartUrl)+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
    						 }
    						 else{
    							 $.hideLoading();
        	                     $.toast(res.data.failReason,"cancel",function(toast) {});
    						 }
    						
    					 }else{
    						 $.hideLoading();
    	                     $.toast(res.msg,"cancel",function(toast) {});
    					 }
    					
    				},function(msg){
    					 $.hideLoading();
    					 $.toast(msg,"cancel",function(toast) {
    	                 });
    				},function(){
    					
    				});
    	
    })





 })