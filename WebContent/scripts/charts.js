    function first_call_loadDefaultWatchList(){
    	loadDefaultWatchList();
    }
    
	function loadDefaultWatchList(){
    	var params = '';
    	var element = $('watchlist-login');
    	element.innerHTML = '';
    	showLoading('Refreshing WatchList');
    	for (let i=0; i < DEFAULT_WATCHLIST.length;i++){
    		var assetlabel = 'asset' + i.toString() + '=';
    		if (i != DEFAULT_WATCHLIST.length - 1)
    		{
    			params += assetlabel + DEFAULT_WATCHLIST[i] + '&';
    		}
    		else{
    			params += assetlabel + DEFAULT_WATCHLIST[i];
    		}
    	}
        var url = './price';

        var req = JSON.stringify({});
        // make AJAX call
        ajax(
           'GET',
           url + '?' + params,
             req,
           // successful callback
           function(res) {
             var result = JSON.parse(res);
         	 showPrice(result);
           },
           // failed callback
           function() {
              console.log("Loading Default WatchList is Not Successful");
        });
       
    }

	function initBalanceChart(){
	    var trace1 = {
	    		  x: [1, 2, 3, 4],
	    		  y: [10, 15, 13, 17],
	    		  mode: 'markers'
	    		};

	    		var trace2 = {
	    		  x: [2, 3, 4, 5],
	    		  y: [16, 5, 11, 10],
	    		  mode: 'lines'
	    		};

	    		var trace3 = {
	    		  x: [1, 2, 3, 4],
	    		  y: [12, 9, 15, 12],
	    		  mode: 'lines+markers'
	    		};

	    		var data = [ trace1, trace2, trace3 ];

	    		var layout = {};

	    		Plotly.newPlot('underlying-asset-chart', data, layout, {showSendToCloud: true});
	}
	
	function showPrice(results) {
		element = $('watchlist-login');
		element.innerHTML = '';
		for (var res of results){
			var container = $('div',{
				className :'watchlist-item-container'
			});
			var trend = res.trend;
			var asset = res.asset;
			var price = res.price;
			var text = $('p', {
	            className: 'watchlist-item'
	        });
			if (trend === 'up'){
				var trend_icon = $('img',{
					className: "trend-icon",
					src: 'assets/uparrow.png'
				});
			}
			else if (trend === 'down'){
				var trend_icon = $('img',{
					className: "trend-icon",
					src: 'assets/downarrow.png'
				});
			}
			else{
				var trend_icon = $('img',{
					className: "trend-icon",
					src: 'assets/circle.png'
				});
			}
			text.innerHTML = '<span class = "asset-name">' + asset + '</span>' + '<br/>' + '<span class = "asset-price">' + price + '</span>'
			container.appendChild(text);
			container.appendChild(trend_icon);
			element.appendChild(container);
		}
		showElement($('watchlist-login-prompt'));
	}
	
	function showLoading(msg){
		element = $('watchlist-login');
		var blk = $('div', {
			className: 'loading-container'
		});
		blk.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' +
        msg + '</p>'
		
		element.appendChild(blk);
	}