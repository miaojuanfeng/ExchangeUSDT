package com.contactsImprove.constant;

public class ResponseCode {
	
	public static enum StatusCode {
		_0(0, "数据表格加载成功"),
		_200(200, "成功"),
		_201(201, "失败"),
		_202(202, "手机号码已注册"),
		_203(203, "账号不存在"),
		_204(204, "密码错误"),
		_205(205, "手机格式不正确"),
		_206(206, "USDT數量不足"),		
		_300(300, "商户号不能为空"),
		_301(301, "无此商户号"),
		_302(302, "缺少金额参数或数量错误"),
		_303(303, "缺少参数"),
		_304(304, "签名错误"),
		_305(305, "缺少参数商户订单号"),	
		_306(306, "缺少参数订单时间"),
		_307(307, "缺少参数数字货币类型"),		
		_308(308, "缺少参数交易流水号"),	
		_309(309, "参数错误"),		
		_310(310, "身份越权"),
		_311(311, "交易失败"),
		_312(312, "会话过期"),
		_313(313, "邮箱已存在"),
		_314(314, "订单不存在"),	
		_315(315, "f(f(x)) = f(x)"),
		_316(316, "额度不足,请减少额度分批出金！"),
		_317(317, "单笔金额限额"+SystemConst.limitCNYOut+"元"),
		_318(318, "单笔USDT限量"+SystemConst.limitUSDTOut+"个"),
		_319(319, "错误的订单状态"),	
		_320(320, "请选择银联支付方式提交!"),
		_321(321, "入金金额太少！"),
		_322(322, "邀请人手机号不存在"),
		_402(402, "请联系客户审核"),
		_401(401, "该账户已锁定"),
		_400(400, "toekn验证失败"),
		_500(500, "服务器错误，请联系管理员");
		
		/**
         * 状态码
         */
       public int status;
        /**
         * 描述
         */
       public String msg;

       StatusCode(int status, String resMsg) {
			this.status = status;
			this.msg = resMsg;
       }

       public int getStatus() {
    	   return status;
       }

       public String getMsg() {
    	   return msg;
       }
	}
}
