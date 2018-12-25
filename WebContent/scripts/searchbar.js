function initSearchBar(){
	var text_input = $('search-text-input');
	text_input.addEventListener('input',findSuggestions)
}
function findSuggestions(){
	var input_val =$('search-text-input').value;
	if (input_val.length ===0){
		$('search-suggestions').innerHTML = '';
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
	var sug_blk = $('search-suggestions');
	sug_blk.innerHTML = '';
	var keys = Object.keys(result);
	var sugUl = $('ul',{
		className: 'suggestions-listing-ul'
	});
	for(var i=0;i<keys.length;i++){
	    var symbol = keys[i];
	    var name = result[symbol];
	    var sugLi = $('li',{
	    	className: 'suggestion-item'
	    });
	    sugLi.innerHTML = '<span>' + symbol+ ' : ' + name + '</span>';
	    sugUl.appendChild(sugLi);
	}
	
	sug_blk.appendChild(sugUl);
	var eles = document.getElementsByClassName('suggestion-item');
	for (var ele of eles){
		ele.addEventListener('click',function(e){
			e.preventDefault();
			var symbol = e.target.innerText.split(':')[0].trim();
			$('search-suggestions').innerHTML = '';
			$('search-text-input').value = symbol
		});
	}
}

