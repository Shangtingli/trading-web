    function first_call_loadDefaultWatchList(){
    	var url = './price';
    	loadDefaultWatchList(true);
    }
    
//	function loadDefaultWatchList(renderElement,loadingElement,promptElement, refElement,url){
    function loadDefaultWatchList(parentPage){
    	var renderElement = (parentPage) ? ($('watchlist-login')): ($$('watchlist-login'));
    	var refElement = (parentPage) ? ($('dummy')): ($$('dummy'));
    	var promptElement = (parentPage) ? ($('watchlist-login-prompt')): ($$('watchlist-login-prompt'));
    	var url = (parentPage) ? ('./price') : ('../price');
		renderElement.innerHTML = '';
    	var params = constructParams(refElement);
    	showLoading('Refreshing WatchList',renderElement);
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
         	 showPrice(result,renderElement,promptElement,showButtons,parentPage);
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
	
	function removeFromWatchList(asset){
		var userid = $('dummy').innerHTML;
		var url = './watchlist';
		var params = 'method=' + 'remove&' + 'userid=' + userid + '&symbol=' + asset;
		var target_id = 'watchlist-item-button-container-' + asset;
		var req = JSON.stringify({});
		ajax('POST', url + '?' + params, req,
				function(res) {
					var result = JSON.parse(res);
					$(target_id).remove();
				},
				function() {
					console.log("Something is Wrong");
				},false);
		var url = './price';
		loadDefaultWatchList($('watchlist-login'),$('watchlist-login'),$('watchlist-login-prompt'),$('dummy'),url);
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
	
	function showPrice(results,renderElement,promptElement,showButtons,parentPage) {
		renderElement.innerHTML = '';
		for (var res of results){
			var container = (parentPage) ? 
			$('div',{
				className :'watchlist-item-container'
			}) : $$('div',{
				className :'watchlist-item-container'
			})
			;
			var trend = res.trend;
			var asset = res.asset;
			var price = res.price;
			var text = (parentPage) ? 
			$('p', {
	            className: 'watchlist-item'
	        }) : $$('p', {
	            className: 'watchlist-item'
	        }) ;
			if (trend === 'up'){
				var trend_icon = (parentPage)?$('img',{
					className: "trend-icon",
					src: 'assets/uparrow.png'
				}) : $$('img',{
					className: "trend-icon",
					src: 'assets/uparrow.png'
				});
			}
			else if (trend === 'down'){
				var trend_icon = (parentPage)?$('img',{
					className: "trend-icon",
					src: 'assets/downarrow.png'
				}) : $$('img',{
					className: "trend-icon",
					src: 'assets/downarrow.png'
				});
			}
			else{
				var trend_icon = (parentPage)?$('img',{
					className: "trend-icon",
					src: 'assets/circle.png'
				}) : $$('img',{
					className: "trend-icon",
					src: 'assets/circle.png'
				});
			}
			text.innerHTML = '<span class = "asset-name">' + asset + '</span>' + '<br/>' + '<span class = "asset-price">' + price + '</span>'
			container.appendChild(text);
			container.appendChild(trend_icon);
			
			var btn_container = (parentPage) ? $('div', {
	            className: 'watchlist-item-button-container',
	        }) : $$('div', {
	            className: 'watchlist-item-button-container',
	        }); 
			var rm_btn = (parentPage)?$('button', {
	            className: 'remove-from-watchlist-button',
	        }) : $$('button', {
	            className: 'remove-from-watchlist-button',
	        });
			rm_btn.innerHTML = 'Remove';
			rm_btn.setAttribute('id','remove-from-watchlist-button-' + asset);
			rm_btn.addEventListener('click',function(e){
				var asset = e.target.id.replace(e.target.className + '-', '');
				e.preventDefault();
				removeFromWatchList(asset);
			});
			btn_container.appendChild(rm_btn);
			btn_container.setAttribute('id','watchlist-item-button-container-' + asset);
			btn_container.style.display = (showButtons) ? ("block"):("none");
			container.appendChild(btn_container);
			renderElement.appendChild(container);
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