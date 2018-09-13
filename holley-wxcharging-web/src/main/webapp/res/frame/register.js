$(function() {
    var storage= window.localStorage;
    storage = storage ? storage : [];
    var text=$('.text-show');
    var otart=$('.weui-vcode-btn');
    var otel;
    var time=sendCodeTime;
    var ocode=$('.code');
    var ocodeVal;
    var isClick=false;

    var regPhone=/^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9])|(17[0-9]))\d{8}$/;
    $(document).on('input','#telephone',function (e) {
        otel=this.value;
        if((otel.length===11)&&(regPhone.test(otel))&&(time===sendCodeTime)){
            otart.addClass('text-primary');
        }else{
            otart.removeClass('text-primary');
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
        }else{
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
        }
        

    })
    $('.exper-user').on('click',function(e){//点击注册
    	if(isClick)return;
    	isClick=true;
        text.text('');
        $('.weui-cells_form .weui-input').each(function (i) {
              var isEpVal=$(this);
              var rpLabel=isEpVal.closest('.weui-cell').find('.weui-label');
              var oRole= parseInt(isEpVal.data('role'));
              if(repEmpty(isEpVal)){
                  text.text(rpLabel.text()+'不能为空');
                  isClick=false;
                  return false;
              }else{
                  if(oRole===2){
                      if(!(regPhone.test(isEpVal.val()))){
                          text.text(rpLabel.text()+'格式错误');
                          isClick=false;
                          return false;
                      }
                  }else{
                      if(oRole===5){
                          if(!($('#password').val()===isEpVal.val())){
                              text.text('密码输入不一致');
                              isClick=false;
                              return false;
                          }else{
                            //注册成功
                        	$.showLoading();
                        	getPages("frame/frame_queryRegister.action",
                      			{
                        		    userName:$('#userName').val(),
                        		    phone:$('#telephone').val(),//手机号
                        		    password:$('#password').val(),
                        		    confirmPassword:$('#confirmPassword').val(),
                        		    phoneCode:$('.code').val()	
                      			},function(res){
                      				 if(res.ret===0){//如果成功
                      					 if(res.data.failReason===0){
                      						$.hideLoading();
                      						 $.toast('注册成功',800,function() {
                      							 isClick=false;
                      							  window.location.href="frame/login.action"; 
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
                  }
              }  
              
        })
          

      
    })
});