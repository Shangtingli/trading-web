	function loadUserMetaData(){
		var url = './usermeta';
		var params = 'userid=' + $('#username').val();
		var req = JSON.stringify({});
		ajax('GET',url + '?' + params,req,
				function(res){
			var result = JSON.parse(res);
			var holdings_container = $('#assets-holdings');
			var balance_container = $('#balance-and-assets');
			holdings_container.html('');
			balance_container.html('');
			holdings_container.append($('<div id = "holdings-section-title-container"> <span id = "holdings-section-title">Your Holdings: </span></div>'));
			balance_container.append($('<div id = "balance-section-title-container"> <span id = "balance-section-title">Your Balance and Assets: </span></div>'))
			var keys1 = Object.keys(result[0]);
			var list1 = $('<ul></ul>');
			for (var key of keys1){
				var item = $('<li></li>');
				item.html(key + ' : ' + result[0][key]);
				list1.append(item);
			}
			holdings_container.append(list1);
			hideElement($('#user-balance-login-prompt'));
			var keys2 = Object.keys(result[1]);
			var list2 = $('<ul></ul>');
			for (var key of keys2){
				var item = $('<li></li>');
				item.html(key + ' : ' + result[1][key]);
				list2.append(item);
			}
			balance_container.append(list2);
			balance_container.css('border-right','1.5px solid white');

		},
		function (e){
			console.log("Something went wrong")
		},false)
	}
	
	function onChangeMeta(){
		loadUserMetaData();
	}
	
	function initBalanceChart(){
		var userid = $('#username').val()
		var interval = '1';
		var url = './chart';
		var params = 'userid=' + userid + '&interval=' + interval;
		if (userid.length ===0)
		{
			for (let i =0; i < DEFAULT_PORTFOLIO.length; i++){
				var asset = DEFAULT_PORTFOLIO[i];
				params += '&asset' + i.toString() + '=' + asset;
			}
		}
		var req = JSON.stringify({});
		var user = (userid.length===0) ? ('Default Portfolio'):(userid) ;
		var title_text = 'Total Value Change Chart For '+ user;
		showLoading('Loading User\'s Chart...', $('#total-value-chart'));
		ajax('GET', url + '?' + params, req,
				function(res){
					$('#total-value-chart').html('');
					var result = JSON.parse(res);
					var y_array = calculateWorth(result);
					debugger;
					var x_array = [];
					x_array.length = y_array.length;
					for (let i=1 ; i < x_array.length + 1; i++){
						x_array[i-1] = i;
					}
					var trace1 = {
							x: x_array,
							y: y_array,
							mode: 'lines+markers'
					};
					var data = [trace1];
					var layout = {
						x_axis: {
							title:'time',
							titlefont: {
							      family: 'Courier New, monospace',
							      size: 18,
							      color: '#7f7f7f'
							    }
						},
						y_axis: {
							title:'price',
							titlefont: {
							      family: 'Courier New, monospace',
							      size: 18,
							      color: '#7f7f7f'
							    }
						},
						title: title_text
					};
					Plotly.newPlot('total-value-chart', data, layout, {displayModeBar: false});
				},
				function(e){
					console.log("Something is Wrong!!!");
				}
		);

	    		
	}
	
	function calculateWorth(result){
		if (result.length === 1){
			var array = [];
			array.length = 100;
			for (let i =0; i< array.length; i++){
				array[i] = result[0]['capital'];
			}
			return array;
		}
		var userid = $('#username').val();
		var first_asset = result[0];
		var len = first_asset[Object.keys(first_asset)[0]].length;
		var array = [];
		var capital = (userid.length ===0 ) ? (0): (parseFloat(result[result.length-1]['capital']));
		array.length = len;
		for (let i =0; i< array.length; i++){
			array[i] = 0;
		}
		for (let j= 0; j < result.length - 1; j++){
			var asset = result[j];
			var timeseries = asset[Object.keys(asset)[0]];
			for (let i=0; i < timeseries.length; i++){
				if (userid.length === 0)
				{
					array[i] += parseFloat(timeseries[i]['price']) * DEFAULT_HOLDINGS[j];
				}
				else{
					price = parseFloat(timeseries[i]['price']);
					holding = parseFloat(asset['holdings']);
					array[i] += price * holding;
				}
			}
		}
		
		for (let i=0; i < array.length; i++){
			array[i] += capital;
		}
		debugger;
		return array;
	}