layui.use([ 'layer','laydate','form' ], function() {
	var $ = layui.$,
	laydate = layui.laydate,
	form = layui.form;
	
	var myChart = echarts.init(document.getElementById('main-line'));
	var option = {
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
			data : ['入金总数','出金总数']
		},
		xAxis : {
			data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
		},
		yAxis : {}
	}
	myChart.setOption(option, true);
	
	$('#seach').on('click', function() {
		seach();
	})
	
	function seach() {
		if($('#phone').val() == null || $('#phone').val() == '') {
			layer.msg("手机号不能为空");
			return;
		}
		
		$.ajax({
			url : "/ContactsImprove/admin/echarts/selectOrderTotal",
			type : "post",
			dateType : 'json',
			data : {
				'phoneNumber' : $('#phone').val()
			},
			success : function success(data) {
				var map = data.data;
				
				var puttol = [];
				var outtol = [];
				
				var puttol1 = [];
				var outtol1 = [];
				var title = [];
				var out = "";
				var put = "";
				for(var i=0;i<map.length;i++) {
					for(var k in map[i]) {
						if(data.type == 2) {
							$('#txt').html('币商')
							if(map[i].type == '0') {// 出金
								outtol.push(map[i][k]);
							} else if(map[i].type == '2') {// 入金
								puttol.push(map[i][k]);
							}
						} else if(data.type == 1){
							$('#txt').html('商户')
							if(map[i].type == '0') {// 入金
								puttol.push(map[i][k]);
							} else if(map[i].type == '2') {// 出金
								outtol.push(map[i][k]);
							}
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
						name : '入金总数',
						type : 'bar',
						data : puttol
					},{
						name : '出金总数',
						type : 'bar',
						data : outtol
					}]
				});
			}
		});
	}
	
	laydate.render({
		elem : '#createTime',
	});
	
	laydate.render({
		elem : '#closeTime',
	});
});