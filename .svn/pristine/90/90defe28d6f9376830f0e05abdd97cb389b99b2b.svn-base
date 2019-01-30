layui.use(['table','form'], function() {
	var table = layui.table,
		form=layui.form,
		$ = layui.$;
	$(function() {
		render();
	});
	function render() {	
	table.render({
		elem : '#exchangeRate',
		height : 610,
		url : '/ContactsImprove/admin/setup/exchangeRateList',
		title : '订单数据表',
		toolbar: true,
		totalRow: true,
		cols : [ [
			{
				checkbox : true,
				fixed : true
			},
			{
				field : 'currencyType',
				title : '货币类型',
				width : 160,
				fixed : 'left',
				unresize : true,
				sort : true,
				totalRowText : '合计'
			},
			{
				field : 'currencyName',
				title : '货币名称',
				width : 160,
				fixed : 'left',
				unresize : true,
				sort : true,
				totalRowText : '合计'
			},			
			{
				field : 'status',
				title : '自动更新',
				width : 120,
				align : 'center',
				templet : '<div>{{ FormatStatus(d.status)}}</div>'
			},
			{
				field : 'rate',
				title : '当前汇率',
				width : 130,
				align : 'center'
			},
			{
				field : 'createTime',
				title : '最后一次更新时间',
				width : 160,
				templet : '<div>{{ Format(d.createTime,"yyyy-MM-dd hh:mm:ss")}}</div>'
			},
			{
				fixed : 'right',
				title : '操作',
				width : 180,
				align : 'center',
				toolbar : '#barDemo'
			}
		] ],
		id : 'seach_reload',
		page : true
	});
	}

	table.on('tool(exchangeRate)', function(obj) {
		var data = obj.data, //获得当前行数据
			layEvent = obj.event; //获得 lay-event 对应的值		
			if(layEvent === 'setRate'){
			$("#currencyType").val(data.currencyType);
			$("#status").val(data.status);
			$("#rate").val(data.rate);
			form.render('select');
			layer.open({
				type : 1,
				area : [ '460px', '300px' ],
				shade : [ 0.5, '#393D49' ],
				title : "提币审核",
				content : $("#exchangeRate_example"),
				btn : [ '确定', '取消' ],
				scrollbar : false,
				resize : true,
				maxmin: true,
				yes : function(index, layero) {
					$.ajax({
						url : "/ContactsImprove/admin/setup/setExchangeRate",
						type : "post",
						data : $("#exchangeRate_example").serialize(),
						dateType : 'json',
						success : function success(resp) {
							layer.msg(resp.msg);
							layer.close(index);
							render();
						}
					});
				}
			});
		}
	});

});

function FormatStatus(str) {
	if (str != null) {
		if (str == 0) {
			return "否";
		} else if (str == 1) {
			return "是";
		}
	} else {
		return "";
	}
}
function Format(datetime, fmt) {
	if (datetime != null && datetime != "") {
		datetime = new Date(datetime);
		var o = {
			"M+" : datetime.getMonth() + 1, //月份   
			"d+" : datetime.getDate(), //日   
			"h+" : datetime.getHours(), //小时   
			"m+" : datetime.getMinutes(), //分   
			"s+" : datetime.getSeconds(), //秒   
			"q+" : Math.floor((datetime.getMonth() + 3) / 3), //季度   
			"S" : datetime.getMilliseconds() //毫秒   
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (datetime.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	} else {
		return ""
	}
}