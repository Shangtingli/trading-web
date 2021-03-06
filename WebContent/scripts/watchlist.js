    function first_call_loadWatchList(){
    	loadWatchList();
    }
    
    function loadWatchList(){
    	var renderElement = $('#watchlist-login');
    	var refElement = $('#username');
    	var promptElement = $('#watchlist-login-prompt');
    	var url = './price';
		renderElement.html('');
    	var params = constructParams(refElement);
    	showLoading('Refreshing WatchList',renderElement);
    	var userid = refElement.val();
    	var showButtons = (userid.length > 0);
        var req = JSON.stringify({});
        var query = url + '?' + params;
        // make AJAX call
        ajax(
           'GET',
           	query,
             req,
           // successful callback
           function(res) {
             var result = JSON.parse(res);
         	 showPrice(result,renderElement,promptElement,showButtons);
         	 addRemoveListeners();
             var userid = refElement.val();
             if (userid.length > 0){
             	hideElement(promptElement);
             }
             else{
             	showElement(promptElement);
             }
           },
           // failed callback
           function() {
              console.log("Loading Default WatchList is Not Successful");
        });
        
    }
	
	function removeFromWatchList(asset){
		var userObj = $('#username');
		var userid = userObj.val();
		var url = './watchlist';
		var params = 'method=' + 'remove&' + 'userid=' + userid + '&symbol=' + asset;
		var target_id = 'watchlist-item-button-container-' + asset;
		var req = JSON.stringify({});
		ajax('POST', url + '?' + params, req,
				function(res) {
					var result = JSON.parse(res);
					$('#'+target_id).remove();
				},
				function() {
					console.log("Something is Wrong");
				},false);
		loadWatchList();
	}
	
	function showPrice(results,renderElement,promptElement,showButtons) {
		renderElement.html('');
		for (var res of results){
			var container = $('<div></div>');
			container.attr('class', 'watchlist-item-container');
			var trend = res.trend;
			var asset = res.asset;
			var price = res.price;
			var text = $('<p></p>');
			text.attr('class','watchlist-item');
			if (trend === 'up'){
				var trend_icon = $('<img src= "assets/up-arrow.svg">');
				trend_icon.attr('class','trend-icon');
			}
			else if (trend === 'down'){
				var trend_icon = $('<img src= "assets/down-arrow.svg">');
				trend_icon.attr('class','trend-icon');
			}
			else{
				var trend_icon = $('<img src="assets/circle.png">');
				trend_icon.attr('class','trend-icon');
			}
			text.html('<span class = "asset-name">' + asset + '</span>' + '<br/>' + '<span class = "asset-price">' + price + '</span>');
			container.append(text);
			container.append(trend_icon);
			
			var btn_container = $('<div></div>');
			btn_container.attr('class','watchlist-item-button-container');
			var rm_btn = $('<button></button>');
			rm_btn.attr('class','remove-from-watchlist-button');
			rm_btn.html('Remove');
			rm_btn.attr('id','remove-from-watchlist-button-' + asset);
			btn_container.append(rm_btn);
			btn_container.attr('id','watchlist-item-button-container-' + asset);
			btn_container.css("display",(showButtons) ? ("block"):("none"));
			container.append(btn_container);
			renderElement.append(container);
		}
	}
	
	function addRemoveListeners(){
		var elements = $('.remove-from-watchlist-button');
		elements.on('click',function(e){
			e.preventDefault();
			var asset = e.target.id.replace(e.target.className + '-', '');
			removeFromWatchList(asset);
		})
	}
	
	function onChangeUserId(){
		loadUserMetaData();
		loadWatchList();
		loadChart();
	}