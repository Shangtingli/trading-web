function initSearchBar(){
	var text_input = $('#search-text-input');
	text_input.on('input',function(e){
		e.preventDefault();
		findSuggestions();
	});
	var search_submit = $('#search-submit-button');
	search_submit.on('click',function(e){
		e.preventDefault();
		addtoWatchList();
	});
}

function addtoWatchList(){
	var userid = $('#username').val();
	var symbol = $('#search-text-input').val();
	var url = './watchlist';
	var params = 'method=' + 'add&' + 'userid=' + userid + '&symbol=' + symbol;
	var req = JSON.stringify({});
	var success = false;
	$('#search-text-input').val('');
	ajax('POST', url + '?' + params, req,
			// successful callback
			function(res) {
				var result = JSON.parse(res);
				if (result.result === 'failed'){
					console.log("Failed");
					showElement($('#ticker-exist-notice'));
				}
				else{
					success = true;
					hideElement($('#ticker-exist-notice'));
				}
			},

			// error
			function() {
				console.log("Something is Wrong");
			},false);
	if (success)
	{
		loadWatchList();
	}
}

function findSuggestions(){
	var input_val =$('#search-text-input').val();
	if (input_val.length === 0){
		$('#search-suggestions').html('');
		return;
	}
	var url = './suggestion';
	var req = JSON.stringify({});
	var params = 'fragment=' + input_val;
	ajax('GET', url + '?' + params, req,
			// successful callback
			function(res) {
				var result = JSON.parse(res);
				showSuggestions(result);
			},

			// error
			function() {
				console.log("Something is Wrong");
			});
}

function showSuggestions(result){
	var sug_blk = $('#search-suggestions');
	sug_blk.html('');
	var keys = Object.keys(result);
	var sugUl = $('<ul></ul>');
	sugUl.attr('class','suggestions-listing-ul');
	for(var i=0;i<keys.length;i++){
	    var symbol = keys[i];
	    var name = result[symbol];
		var sugLi = $('<li></li>');
		sugLi.attr('class','suggestion-item');
		sugLi.html('<span>' + symbol+ ' : <br>' + name + '</span>');
	    sugUl.append(sugLi);
	}
	
	sug_blk.append(sugUl);
	var eles = $('.suggestion-item');
	eles.on('click',function(e){
		e.preventDefault();
		var symbol = e.target.innerText.split(':')[0].trim();
		$('#search-suggestions').html('');
		$('#search-text-input').val(symbol);
	});

}

