layui.use([ 'layer' ], function() {
	var $ = layui.$;
	
	var myChart = echarts.init(document.getElementById('main-line'));
	var option = {
		title : {
			text : '订单数量统计'
		},
		tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'cross',
	            crossStyle: {
	                color: '#999'
	            }
	        }
	    },
	    toolbox: {
	        feature: {
	            dataView: {show: true, readOnly: false},
	            magicType: {show: true, type: ['line','bar']},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
	    },
		legend : {
			data : [ '成功订单数','取消订单数','失败订单数','总订单数' ]
		},
		xAxis : {
			data : []
		},
		yAxis : {}
	}
	myChart.setOption(option, true);
	
	$.ajax({
		url : "/ContactsImprove/admin/echarts/selectOrder",
		type : "post",
		dateType : 'json',
		data : {
			"year" : "2018"
		},
		success : function success(data) {
			var map = data.data;
			var total = data.totla;
			
			var tol = [];
			var num = [];
			var success = [];
			var fail = [];
			var title = [];
			
			for(var k=0;k<total.length;k++) {
				for(var j in total[k]) {
					tol.push(total[k][j]);
				}
			}
			
			for(var i=0;i<map.length;i++) {
				for(var k in map[i]) {
					if(map[i].status == '2' && k != 'status') {
						success.push(map[i][k]);
					} else if(map[i].status == '3' && k != 'status'){
						num.push(map[i][k]);
					}  else if (map[i].status == '4' && k != 'status') {
						fail.push(map[i][k]);
					}
					
					if(k != 'status' && title.length < 12) {
						title.push(k + "月");
					}
				}
			}
			
			myChart.setOption({
				xAxis : {
					type: 'category',
					data : title,
					axisPointer: {
		                type: 'shadow'
		            }
				},
				series : [ {
					name : '成功订单数',
					type : 'bar',
					data : success
				},{
					name : '取消订单数',
					type : 'bar',
					data : num
				},{
					name : '失败订单数',
					type : 'bar',
					data : fail
				},{
					name : '总订单数',
					type : 'line',
					data : tol
				}]
			});
		}
	});
	
	var totalmyChart = echarts.init(document.getElementById('main-line-total'));
	
	var toloption = {
		title : {
			text : '订单金额统计'
		},
		tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'cross',
	            crossStyle: {
	                color: '#999'
	            }
	        }
	    },
	    toolbox: {
	        feature: {
	            dataView: {show: true, readOnly: false},
	            magicType: {show: true, type: ['line','bar']},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
	    },
		legend : {
			data : [ "订单金额总数" ]
		},
		xAxis : {
			data : []
		},
		yAxis : {}
	}
	totalmyChart.setOption(toloption, true);
	
	$.ajax({
		url : "/ContactsImprove/admin/echarts/selectOrderTotal",
		type : "post",
		dateType : 'json',
		data : {
			"year" : "2018"
		},
		success : function success(data) {
			var total = data.data;
			var title = [];
			var tol = [];
			for(var i=0;i<total.length;i++) {
				for(var k in total[i]) {
					tol.push(total[i][k]);
					
					if(title.length < 12) {
						title.push(k + "月");
					}
				}
			}
			
			totalmyChart.setOption({
				xAxis : {
					type: 'category',
					data : title,
					axisPointer: {
		                type: 'shadow'
		            }
				},
				series : [ {
					name : '订单金额总数',
					type : 'bar',
					data : tol
				}]
			});
		}
	})
});