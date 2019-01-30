layui.use([ 'layer' ], function() {
	var $ = layui.$;
	
	var myChart = echarts.init(document.getElementById('main-line'));
	var option = {
		title : {
			text : '充币/提币统计'
		},
		tooltip : {},
		legend : {
			data : [ '充币总数', '提币总数']
		},
		xAxis : {
			data : []
		},
		yAxis : {}
	}
	myChart.setOption(option, true);
	
	$.ajax({
		url : "/ContactsImprove/admin/echarts/selectJurnalAccount",
		type : "post",
		dateType : 'json',
		data : {
			"year" : "2018"
		},
		success : function success(data) {
			var map = data.data;
			
			var recharge = [];
			var withdrawal = [];
			var title = [];
			
			for(var i=0;i<map.length;i++) {
				for(var k in map[i]) {
					if(map[i].type == '3' && k != 'type') {
						recharge.push(map[i][k]);
					} else if(map[i].type == '5' && k != 'type'){
						withdrawal.push(map[i][k]);
					}
					
					if(k != 'type' && title.length < 12) {
						title.push(k + "月");
					}
				}
			}
			
			myChart.setOption({
				xAxis : {
					data : title
				},
				series : [ {
					name : '充币总数',
					type : 'bar',
					data : recharge
				},{
					name : '提币总数',
					type : 'bar',
					data : withdrawal
				}]
			});
		}
	});
});