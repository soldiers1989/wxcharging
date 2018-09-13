<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java" pageEncoding="UTF-8" %>
<%@ include file="../common/top.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>充电过程数据</title>
    <%@include file="../common/css.jsp"%>
    <link href="<%=request.getContextPath()%>/res/css/animate.css" rel="stylesheet"></link>
     <script>
    
    </script>
</head>
<body ontouchstart>
<div class="pullContent">
	<div class="weui-cell wui-cell-noborder wui-cell-nopadding-top wui-cell-nopadding-left load-before">
		<div class="weui-cell__bd">
			<div class="weui-panel weui-panel_access">
				<div class="charge-box-top flex-just-between text-white" style="padding:15px;">
					<div class="">
						<h2 class="font-wt-500"><span class="pile-code">0</span></h2>
						<p>订单编码:</p>
						<p class="font-r1"><span class="pile-tradeNo">123456789</span></p>
					</div>
					<div>
						<span class="pile-status">- -</span>
					</div>
				</div>
			</div>

			<div class="weui-cell__bd more-list-content">
				<div class="weui-panel weui-panel_access font-15">
		
					<div class="weui-cells my-list-box">
						<a class="weui-cell predict-time hidden" href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<!-- <img src="res/images/recharge_record.png" alt=""> -->
								<p>预计充电总时长(分)</p>
							</div>
							<div class="weui-cell__ft"><span class="pile-preLen">0</span></div>
						</a>
						<a class="weui-cell" href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<!-- <img src="res/images/recharge_record.png" alt=""> -->
								<p>实际充电总时长(分)</p>
							</div>
							<div class="weui-cell__ft"><span class="pile-chaLen">0</span></div>
						</a>
						<a class="weui-cell none-service hidden"  href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>充电总金额(元)</p>
							</div>
							<div class="weui-cell__ft"><span class="pile-chaMoney margin-l5">0.00</span></div>
						</a>
						<a class="weui-cell has-service hidden"  href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<p>充电总金额(元)<span class="pile-chaMoney margin-l5">0.00</span></p>
							</div>
							<div class="weui-cell__ft">服务费(元)<span class="pile-serviceMoney margin-l5">0</span></div>
						</a>
						<a class="weui-cell"  href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<!-- <img src="res/images/charge_record.png" alt=""> -->
								<p>启动充电时间</p>
							</div>
							<div class="weui-cell__ft"><span class="pile-startTime">0</span></div>
						</a>
						<a class="weui-cell pile-chaPower-box hidden"  href="javascript:void(0)">
							<div class="weui-cell__hd"></div>
							<div class="weui-cell__bd">
								<!-- <img src="res/images/charge_record.png" alt=""> -->
								<p>充电电量(度)</p>
							</div>
							<div class="weui-cell__ft"><span class="pile-chaPower">0</span></div>
						</a>
					</div>

				</div>
				<div class="weui-panel weui-panel_access font-15" style="padding-bottom:60px;">
					<div class="weui-media-box weui-media-box_appmsg wui-cell-nopadding-top padding-10">
						<div class="weui-media-box__bd">
							<div class="text-primary text-center direct-current hidden" style="padding:2% 0;">
								<div class="">
								    <!-- <p class="pile-soc">0</p> -->
									<div id="loading" class="">
										<div id="loading-center">
											<div id="loading-center-absolute">
												<div class="object object_four"></div>
												<div class="object object_three" ></div>
											</div>
											<div id="main">
												<div id="wave-top">
													<span><img src="res/images/shangmian.png" ></span>
													<span><img src="res/images/shangmian.png" ></span>
												</div>
												<div id="wave-bottom">
													<span><img src="res/images/dixia.png"></span>
													<span><img src="res/images/dixia.png"></span>
													<span><img src="res/images/dixia.png"></span>
												</div>
												<div id="text">
													<p style="font-size:20px;" class="pile-soc">0</p>
													<!-- <p style="font-size:40px;">500</p> -->
												</div>
											</div>
										</div> 
										
									</div>
								</div>
								
								<div class="text-primary">电池电量百分比(%)
								<!-- <span class="pile-flag pile-soc margin-l5"></span> -->
								</div>
							</div>
				
							<div class="nearby-list-item flex-just-between text-center">
								<div class="text-yellow" style="flex-basis:50%;">
								  <div class="remove-animate">
									    <p class="pile-currentV hidden">0.00</p>
										<div class="dashboard"></div>	
								   </div>								
									<span class="">当前电压(V)<span class="pile-flag pile-currentV margin-l5"></span></span>
								</div>						
								<div style="flex-basis: 50%;" class="text-blue">
								   <div class="remove-animate">
									    <p class="pile-currentA hidden">0.00</p>
										<div class="battery">
										  <div class="cloud">
										  
										  </div>
										</div>
									</div>
									<span>当前电流(A)<span class="pile-flag pile-currentA margin-l5"></span></span>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>




		</div>
	</div>
</div>

<div class="weui-tabbar tabbar_bar tabbar_bar_over">
	<p class="tabbar_btn">
		停止充电
	</p>
</div>
<%@include file="../common/js.jsp"%>
<script src="<%=request.getContextPath()%>/res/js/three.min.js?t=<%=System.currentTimeMillis()%>"></script>
<script>

			var SEPARATION = 100, AMOUNTX = 50, AMOUNTY = 50;

			var container;
			var camera, scene, renderer;

			var particles, particle, count = 0;

			var mouseX = 0, mouseY = 0;
			//var xWidth=window.innerWidth;
			//var yHeight=window.innerHeight;
			
			var xWidth=$('#main').innerWidth();
			var yHeight=$('#main').innerHeight()+40;

			var windowHalfX = xWidth / 2;
			var windowHalfY = yHeight / 2;

			init();
			animate();

			function init() {

				/*container = document.createElement( 'div' );
				document.body.appendChild( container );*/
				container=document.getElementById("main");

				//camera = new THREE.PerspectiveCamera( 75, xWidth / yHeight, 1, 10000 );
				camera = new THREE.PerspectiveCamera( 75, 100/ 100, 1, 10000 );
				camera.position.z = 1000;

				scene = new THREE.Scene();

				particles = new Array();

				var PI2 = Math.PI * 2;
				var material = new THREE.ParticleCanvasMaterial( {

					color: 0xffffff,
					program: function ( context ) {

						context.beginPath();
						context.arc( 0, 0, 1, 0, PI2, true );
						context.fill();

					}

				} );

				var i = 0;

				for ( var ix = 0; ix < AMOUNTX; ix ++ ) {

					for ( var iy = 0; iy < AMOUNTY; iy ++ ) {

						particle = particles[ i ++ ] = new THREE.Particle( material );
						particle.position.x = ix * SEPARATION - ( ( AMOUNTX * SEPARATION ) / 2 );
						particle.position.z = iy * SEPARATION - ( ( AMOUNTY * SEPARATION ) / 2 );
						scene.add( particle );

					}

				}

				renderer = new THREE.CanvasRenderer();
				renderer.setSize( xWidth, yHeight );
				container.appendChild( renderer.domElement );

				/* document.addEventListener( 'mousemove', onDocumentMouseMove, false );
				document.addEventListener( 'touchstart', onDocumentTouchStart, false );
				document.addEventListener( 'touchmove', onDocumentTouchMove, false );*/

				window.addEventListener( 'resize', onWindowResize, false );

			}

			function onWindowResize() {

				windowHalfX = xWidth / 2;
				windowHalfY = yHeight / 2;

				camera.aspect = xWidth / yHeight;
				camera.updateProjectionMatrix();

				renderer.setSize( xWidth, yHeight );

			}


			function onDocumentMouseMove( event ) {

				mouseX = event.clientX - windowHalfX;
				mouseY = event.clientY - windowHalfY;

			}

			function onDocumentTouchStart( event ) {

				if ( event.touches.length === 1 ) {

					event.preventDefault();

					mouseX = event.touches[ 0 ].pageX - windowHalfX;
					mouseY = event.touches[ 0 ].pageY - windowHalfY;

				}

			}

			function onDocumentTouchMove( event ) {

				if ( event.touches.length === 1 ) {

					event.preventDefault();

					mouseX = event.touches[ 0 ].pageX - windowHalfX;
					mouseY = event.touches[ 0 ].pageY - windowHalfY;

				}

			}

			//

			function animate() {

				requestAnimationFrame( animate );

				render();


			}

			function render() {

				camera.position.x += ( mouseX - camera.position.x ) * .05;
				camera.position.y += ( - mouseY - camera.position.y ) * .05;
				camera.lookAt( scene.position );

				var i = 0;

				for ( var ix = 0; ix < AMOUNTX; ix ++ ) {

					for ( var iy = 0; iy < AMOUNTY; iy ++ ) {

						particle = particles[ i++ ];
						particle.position.y = ( Math.sin( ( ix + count ) * 0.3 ) * 50 ) + ( Math.sin( ( iy + count ) * 0.5 ) * 50 );
						particle.scale.x = particle.scale.y = ( Math.sin( ( ix + count ) * 0.3 ) + 1 ) * 2 + ( Math.sin( ( iy + count ) * 0.5 ) + 1 ) * 2;

					}

				}

				renderer.render( scene, camera );

				count += 0.1;

			}

		</script>
<script src="<%=request.getContextPath()%>/res/charging/chargingProcessData.js?v=${version}"></script>
</body>
</html>