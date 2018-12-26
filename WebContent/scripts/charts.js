    function first_call_loadDefaultWatchList(){
    	var url = './price';
    	loadDefaultWatchList(true);
    }
    
//	function loadDefaultWatchList(renderElement,loadingElement,promptElement, refElement,url){
    function loadDefaultWatchList(){
    	var renderElement = (parentPage) ? ($('#watchlist-login')): ($("#watchlist-login",window.opener.document));
    	var refElement = (parentPage) ? ($('username')): ($('username',window.opener.document));
    	var promptElement = (parentPage) ? ($('#watchlist-login-prompt')): ($('#watchlist-login-prompt',window.opener.document));
    	var url = (parentPage) ? ('./price') : ('../price');
		renderElement.innerHTML = '';
    	var params = constructParams(refElement);
    	showLoading('Refreshing WatchList',renderElement);
    	var userid = refElement.val();
    	var showButtons = (userid.length > 0);
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
         	 addRemoveListeners(parentPage);
           },
           // failed callback
           function() {
              console.log("Loading Default WatchList is Not Successful");
        });
        var userid = refElement.val();
        if (userid.length > 0){
        	hideElement(promptElement);
        }
        else{
        	showElement(promptElement);
        }
        
    }
	
	function removeFromWatchList(asset){
		var dummyObject = $('dummy');
		var userid = dummyObject.innerHTML;
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
		loadDefaultWatchList(true);
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
		renderElement.html('');
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
	
	function addRemoveListeners(parentPage){
		var elements = (parentPage) ? 
				(document.getElementsByClassName('remove-from-watchlist-button')):
					(window.opener.document.getElementsByClassName('remove-from-watchlist-button'));
		for (var ele of elements){
			ele.addEventListener('click',function(e){
				debugger;
				var asset = e.target.id.replace(e.target.className + '-', '');
				e.preventDefault();
				removeFromWatchList(asset);
			});
		}
	}
	
	function myFunction(){
		alert("hello");
	}