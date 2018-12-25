    function loadDefaultWatchList(){
    	var params = '';
    	showLoading('Loading WatchList');
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
        // The request parameters
    	console.log(params);
        var url = './price';

        var req = JSON.stringify({});
        debugger;
        // make AJAX call
        ajax(
           'GET',
           url + '?' + params,
             req,
           // successful callback
           function(res) {
             debugger;
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
			var asset = res.asset;
			var price = res.price;
			var text = $('p', {
	            className: 'watchlist-item'
	        });
			text.innerHTML = asset + '\n' + price;
			element.appendChild(text);
		}
	}
	
	function showLoading(msg){
		element = $('watchlist-login');
		var blk = $('div', {
			className: 'loading-container'
		});
		blk.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' +
        msg + '</p>'
		
		element.appendChild(blk);
		debugger;
	}