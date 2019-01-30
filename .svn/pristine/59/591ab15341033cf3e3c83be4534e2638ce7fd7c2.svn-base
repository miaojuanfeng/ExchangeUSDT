layui.use(['table', 'form' ], function() {
	var $ = layui.$,
		table = layui.table,
		form = layui.form;
	$(function() {
		render();
	});
	

	function render() {
		table.render({
			elem : '#bankmark',
			height : 610,
			url : '/ContactsImprove/admin/bank/bankmarkList',
			title : '银行简介表',
			toolbar: true,
			totalRow: true,
			cols : [ [
				{
					checkbox : true,
					fixed : true
				},
				{
					field : 'id',
					title : 'ID',
					width : '5%'
				},
				{
					field : 'bankName',
					title : '银行名称',
					width : '15%'
				},
				{
					field : 'bankMark',
					title : '银行简写',
					width : '20%'
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

	table.on('tool(bankmark)', function(obj) {
		var data = obj.data, //获得当前行数据
			layEvent = obj.event; //获得 lay-event 对应的值		
			if(layEvent === 'edit'){
			$("#id").val(data.id);
			$("#bankName").val(data.bankName);
			$("#bankMark").val(data.bankMark);	
			layer.open({
				type : 1,
				area : [ '460px', '300px' ],
				shade : [ 0.5, '#393D49' ],
				title : "编辑简介",
				content : $("#bankmark_example"),
				btn : [ '确定', '取消' ],
				scrollbar : false,
				resize : true,
				maxmin: true,
				yes : function(index, layero) {
					$.ajax({
						url : "/ContactsImprove/admin/bank/update",
						type : "post",
						data : $("#bankmark_example").serialize(),
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
	
	$('#add').on('click', function() {
		$("#id").val("");
		$("#bankName").val("");
		$("#bankMark").val("");				
		layer.open({
			type : 1,
			area : [ '420px', '300px' ],
			shade : [ 0.5, '#393D49' ],
			title : "添加银行简介",
			content : $("#bankmark_example"),
			btn : [ '确定', '取消' ],
			scrollbar : false,
			resize : true,
			maxmin : true,
			yes : function(index, layero) {
				$.ajax({
					url : "/ContactsImprove/admin/bank/add",
					type : "post",
					data : $("#bankmark_example").serialize(),
					dateType : 'json',
					success : function success(resp) {
							layer.msg(resp.msg);
							layer.close(index);
							render();
					}
				});
			}
		});
	});

	});

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