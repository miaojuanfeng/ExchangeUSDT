<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path=request.getContextPath();
String scheme="http";
if(request.getServerPort()==443) {
	scheme="https";
}
String basePath=scheme+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
request.setAttribute("basePath", basePath);
%>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>PayOtc收银台</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="stylesheet" href="${basePath}/css/order/amazeui.min.css">

  <title>支付页面</title>
        <style>
        	.head {
        		width:100%;
        		height:44px;    
        		padding:7px 0 7px 10px;  
        		border-bottom: 1px solid #eee;  		
        	}
        	.logo{
        		width:50px;        		
        	}
        	.prompt{
        		width:80%;
        		max-width: 320px;
        		margin:0 auto;
        	}
        	.prompt_2 {
        		border:0;
        		height:50px;
        		line-height: 50px;
        		color:#fff;
        		width:100%;
        		border-radius: 10px;
        	}
            p{
                text-align:center;
                font-size:12px;
                color:#333;
            }
            .textInfo{
                text-align:center;
                font-size:20px;
                color:#f33c73;
            }
            .amount{
                font-size:30px;
                font-weight:bold;
            }
            p span{
                font-size:32px;
                font-weight:bold;
                color:red;
            }
            .rmb{
                font-size:32px;
            }
            .appTips{
				font-size:16px;
				font-weight:bold;
				width:250px;
			    text-align:center;
			    margin: auto;
			}
            .appTips div{
			    text-align:center;
			    margin: 10px auto;
			}
            .appTipsText{
			    font-size:14px;
			    text-align:left;
			    margin: 10px auto;
			}
			.bottom {
				text-align: right;
				height:44px;
				line-height: 44px;
				padding-right:20px;
			}
			.bottom .countTime {
				font-weight: 600;
			}
            #qrCodeImg_warp {  width: 200px; height: 200px; margin: 5px auto 15px; text-align: center; }
            #qrCodeImg_warp img { width: 200px; height: 200px; margin: 0 auto; }
            			
            .wechatImg {
            	margin:auto;
            	display: flex;
            	padding:10px 50px;
            	border-top:1px solid #eee;
            }
            .wechatImg_ico {
			    display: inline-block;
			    width: 59px;
			    height: 55px;
			    background: url(${basePath}/images/order/img/wechat-pay.png) 0 0 no-repeat;
			    vertical-align: middle;
			    margin-top:20px;
			}
			.tip_text p {
				line-height:24px;
				margin:0;
			}
			.footer_text p {
				line-height:24px;
				margin:0;
			}
			
			@media screen and (max-width: 320px){
            	.wechatImg {
			    	display: block;
            		padding:10px 50px;
            		margin:0 auto;
				}
				.wechatImg_ico {
				    display:block;
				    width: 59px;
				    height: 55px;
				    background: url(${basePath}/images/order/img/wechat-pay.png) 0 0 no-repeat;
				    vertical-align: middle;
				    margin:0 auto;
				}
            }
        </style>
	</head>
	<body>
		<div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
			<div class="head">
				<img class="logo" src="${basePath}/images/order/img/alipay1.jpg" />
			</div>
			<div class="info">
				<div class="sec-1" style="padding:5px;">
					<p style="margin:0 0 5px;"><img id="alipayIcon" src="${basePath}/images/order/img/alipay-icon.jpg" style="width:80px"></p><p class="rmb" style="margin:0 0 5px;"><span>¥&nbsp;<b class="amount">${amount }</b></span></p>
				</div>
			</div>
			<div style="position:relative" id="qrcode_container">
				
				<div id="qrCodeImg_warp_box">
					<div id="qrCodeImg_warp">
	                	<img id="qrCodeImg" src="${basePath}/api/qrcodeRedirect/${tradeNumber}/${sign}" style="" />
	                </div>
				</div>
                
            	<div class="prompt" id="prompt_1">
					<input type="button" style="background-color:#00a3f0;font-size: 18px;" value="点此启动支付宝进行支付" class="prompt_2" id="prompt_2"></input>
				</div>
			</div>
			<div class="appTips">
			    <div><span>充值方式:</span></div>
			    <p class="appTipsText">步骤一、将二维码截图保存至相册</p>
			    <p class="appTipsText">步骤二、打开支付宝，扫一扫相册</p>
			    <p class="appTipsText">步骤三、支付后等待<span style="font-size:18px;color:red;">2分钟</span>左右即可到账,请放心支付</p>
		    </div>
			<div class="bottom">
				<div class="countTime">
					付款倒计时：
					<span class="time" style="color:red;font-weight: 600;">
						00:00
					</span>
				</div>
			</div>
			<div class="wechatImg">
				<div class="wechatImg_ico"></div>
				<div>
					<div class="tip_text">
					<p id="shoutext">打开支付宝 [扫一扫]</p>
					</div>
					<div class="footer_text">
						<p id="shoutext_p1">若无法调起支付，请保存二维码到手机</p>
						<p id="shoutext_p2">支付宝扫一扫点击右上角-从相册选取</p>
						<p id="shoutext_p3">切勿重复支付</p>
					</div>
				</div>
				
			</div>
		</div>
		<script src="${basePath}/scripts/order/js/jquery.min.js"></script>
		<script>
			var timer = null;
			var differMs = ${payTimeOut};//相差的毫秒数
			var currentTime = differMs;
            var url = "";
            
            var userAgent = navigator.userAgent.toLowerCase();
            
            if(userAgent.match(/AlipayClient/i) == 'alipayclient'){
                url = '${payUrl}';
            } else {
				$('#prompt_2').val('请保存二维码使用支付宝支付');
				$('#prompt_2').attr("disabled",'true');
				$('#prompt_2').css("background",'#ddd');
            }            
            function isMobile() {
	            var ua = navigator.userAgent.toLowerCase();
	            _long_matches = 'googlebot-mobile|android|avantgo|blackberry|blazer|elaine|hiptop|ip(hone|od)|kindle|midp|mmp|mobile|o2|opera mini|palm( os)?|pda|plucker|pocket|psp|smartphone|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce; (iemobile|ppc)|xiino|maemo|fennec';
	            _long_matches = new RegExp(_long_matches);
	            _short_matches = '1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|e\-|e\/|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\-|2|g)|yas\-|your|zeto|zte\-';
	            _short_matches = new RegExp(_short_matches);
	            if (_long_matches.test(ua)) {
	                return 1;
	            }
	            user_agent = ua.substring(0, 4);
	            if (_short_matches.test(user_agent)) {
	                return 1;
	            }
	            return 0;
	        }
            
            function isWeiXin(){
		        var ua = window.navigator.userAgent.toLowerCase();
		        if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		            return true;
		        }else{
		            return false;
		        }
		    };
            $(document).ready(function(){
                $("#prompt_2").click(function(){                        
                  toalipay();
                })  
                
                //倒计时
                if (timer) return ;		 
				//开启定时器
				timer = setInterval(function(){		
					currentTime--;
					var minutes =parseInt(currentTime/60);//分钟
					var seconds = parseInt(currentTime) - parseInt(minutes*60);
	
					minutes = minutes<10?"0"+minutes:minutes;
					seconds = seconds<10?"0"+seconds:seconds;
					
					$(".time").html(minutes+"分"+ seconds+"秒");
								
					if (currentTime < 1) {	    
				    clearInterval(timer);
				    timer = null;
				    currentTime = differMs/1000;
				    window.location.href="${basePath}/api/error?code=201"+"&msg=交易已过期";;
				  }
					
				},1000);
                
            })


		   	function toalipay(){
				try {
					window.open(url);
				} catch(e) {
					console.log(e)
				}
			}
		</script>

	</body>
</html>
	