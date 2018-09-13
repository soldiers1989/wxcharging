$(function() {
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var text=$('.text-show');
    var otart=$('.weui-vcode-btn');
    var otel;
    var time=sendCodeTime;
    var ocode=$('.code');
    var ocodeVal;

    var regPhone=/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9])|(17[0-9]))\d{8}$/;
    $(document).on('input','#telephone',function (e) {
        otel=this.value;
        if((otel.length===11)&&(regPhone.test(otel))&&(time===sendCodeTime)){
            otart.addClass('text-primary');   
        }else{
            otart.removeClass('text-primary');
        }
    })
    ocode.on('input',function(e){
    	ocode=this.value;
    	if(ocode.length>0){
    		$('.exper-user').addClass('weui-btn_primary');
    		
    	}else{
    		$('.exper-user').removeClass('weui-btn_primary');
    	}
    		
    })
   


    function repEmpty(obj) {
        if(obj.val().trim().length===0){
            return true;
        }else{
            return false;
        }
    }
   
    otart.on('click',function(e){//点击验证码
    	 var self=$(this);
    	 if(time<60){
    		 return;
    	 }
        getPages("frame/frame_queryPhoneCode.action",
    			{
    				phone:$('#telephone').val()//手机号
    			},function(res){
    				 if(res.ret===0){//如果成功
    					 if(res.data.failReason===0){
    						 $.toast('发送成功',800,function() {
    							  text.text('');
    						        if(self.hasClass('text-primary')){
    						          
    						                var getTime=function(){
    						                    if (time == 0) {
    						                        self.text("获取验证码");
    						                        time=sendCodeTime;
    						                        self.addClass('text-primary');
    						                        return;
    						                    } else {
    						                        self.text("("+ time + "s)重新获取");
    						                        time--;
    						                        if(time>(sendCodeTime-2)){
    						                        	
    						                            
    						                        }
    						                    }
    						                    setTimeout(function () {
    						                        getTime();
    						                    }, 1000)
    						                }
    						                self.removeClass('text-primary');
    						                getTime();
    						         
    						        }
    						 });
    					 }else{
    						 $.toast(res.data.failReasonDesc,"cancel",function() {
                                 
                             });
    					 }
                        
    				 }else{
                        $.toast(res.msg,"cancel",function(toast) {});
    				 }
    				console.log(res);
    			},function(msg){
    				 $.toast(msg,"cancel",function(toast) {
                         
                     });
    			},function(){
    				
    			});

    })
    $('.exper-user').on('click',function(e){//点击登录

        text.text('');
        $('.weui-cells_form .weui-input').each(function (i) {
        	
              var isEpVal=$(this);
              var rpLabel=isEpVal.closest('.weui-cell').find('.weui-label');
              var oRole= parseInt(isEpVal.data('role'));
              if(repEmpty(isEpVal)){
                  text.text(rpLabel.text()+'不能为空');
                  return false;
              }else{
                  if(oRole===2){
                      if(!(regPhone.test(isEpVal.val()))){
                          text.text(rpLabel.text()+'格式错误');
                          return false;
                      }
                  }else{
                	 
                            //手机验证码登陆成功
                        	getPages("frame/frame_queryPhoneCodeLogin.action",
                      			{
                        			openId:storage.getItem('openid'),
                        		    phone:$('#telephone').val(),//手机号
                        		    phoneCode:$('.code').val()
                      				
                      			},function(res){
                      				 if(res.ret===0){//如果成功
                      					 if(res.data.failReason===0){
                      						 $.toast('登陆成功',800,function() {
		                  							storage.setItem('accountKey',res.data.accountKey);//缓存accountKey
		                                            console.log(storage);
		                                            window.location.href="common/nearby.action";
                                               });
                      					 }else{
                      						 $.toast(res.data.failReasonDesc,"cancel",function() {});
                      					 }
                                          
                      				 }else{
                                          $.toast(res.msg,"cancel",function(toast) {});
                      				 }
                      				console.log(res);
                      			},function(msg){
                      				 $.toast(msg,"cancel",function(toast) {
                                           
                                       });
                      			},function(){
                      				
                      			});

                   
                  }
              }  
              
        })
          

       // $.showLoading();
      
    })
});