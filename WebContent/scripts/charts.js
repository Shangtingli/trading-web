    function first_call_loadDefaultWatchList(){
    	var url = './price';
    	loadDefaultWatchList($('watchlist-login'),$('watchlist-login'),$('watchlist-login-prompt'),$('dummy'),url,false);
    }
    
	function loadDefaultWatchList(renderElement,loadingElement,promptElement, refElement,url){
    	debugger;
		renderElement.innerHTML = '';
    	var params = constructParams(refElement);
    	showLoading('Refreshing WatchList',loadingElement);
    	var userid = refElement.innerHTML;
    	var showButtons = (userid !== 'none');
        var req = JSON.stringify({});
        // make AJAX call
        ajax(
           'GET',
           url + '?' + params,
             req,
           // successful callback
           function(res) {
             var result = JSON.parse(res);
         	 showPrice(result,renderElement,promptElement,showButtons);
           },
           // failed callback
           function() {
              console.log("Loading Default WatchList is Not Successful");
        });
        var userid = refElement.innerHTML;
        if (userid !== 'none'){
        	hideElement(promptElement);
        }
        else{
        	showElement(promptElement);
        }
        
    }
	
	function removeFromWatchList(){
		debugger;
		var userid = $('dummy').innerHTML;
//		var symbol = $('search-text-input').value;
//		var url = './watchlist';
//		var params = 'method=' + 'add&' + 'userid=' + userid + '&symbol=' + symbol;
//		var req = JSON.stringify({});
//		var success = false;
//		$('search-text-input').value = '';
//		ajax('POST', url + '?' + params, req,
//				// successful callback
//				function(res) {
//					var result = JSON.parse(res);
//					if (result.result === 'failed'){
//						console.log("Failed");
//						showElement($('ticker-exist-notice'));
//					}
//					else{
//						success = true;
//						hideElement($('ticker-exist-notice'));
//					}
//				},
//
//				// error
//				function() {
//					console.log("Something is Wrong");
//				},false);
//		debugger;
//		if (success)
//		{
//			var url = './price';
//			loadDefaultWatchList($('watchlist-login'),$('watchlist-login'),$('watchlist-login-prompt'),$('dummy'),url);
//		}
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
	
	function showPrice(results,renderElement,promptElement,showButtons) {
		renderElement.innerHTML = '';
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
			
			var btn_container = $('div', {
	            className: 'watchlist-item-button-container',
	        }); 
			var rm_btn = $('button', {
	            className: 'remove-from-watchlist-button',
	        });
			rm_btn.innerHTML = 'Remove';
			btn_container.appendChild(rm_btn);
			btn_container.style.display = (showButtons) ? ("block"):("none");
			container.appendChild(btn_container);
			renderElement.appendChild(container);
			debugger;
			rm_btn.addEventListener('click',function(e){
				debugger;
				e.preventDefault();
			});
		}
	}
	
	function showLoading(msg,loadingElement){
		var blk = $('div', {
			className: 'loading-container'
		});
		blk.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' +
        msg + '</p>'
		
		loadingElement.appendChild(blk);
	}