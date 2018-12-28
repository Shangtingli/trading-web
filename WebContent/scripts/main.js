(function(){	
	function init(){
		onNoLogin();
	}
    
	function onNoLogin(){
		var register_btn = $("#register-btn");
		var login_btn = $("#login-btn");
		var logout_btn = $("#logout-btn");
		var action_btn = $("#action-button");
		register_btn.on('click',function(e){
				e.preventDefault();
				showRegisterPopUp();
			}
		);
		login_btn.on('click',function(e){
			e.preventDefault();
			showLoginPopUp()
		});
		logout_btn.on('click',function(e){
			e.preventDefault();
			destroyLogin();
		});
		action_btn.on('click',function(e){
			e.preventDefault();
			showActionPopUp();
		});
		initSearchBar();
		loadChart();
		showElement($('#user-balance-login-prompt'));
		first_call_loadWatchList();
		var time = undefined;
		if (time !== undefined)
		{
			var timeChange = setInterval(triggerChange,time*1000);
		}
	}
	
	function triggerChange(){
		$('#username').trigger('change');
	}
	function destroyLogin(){
		showElement($('#search-bar-login-prompt'));
		showElement($("#login-button"));
		hideElement($('#logout-button'));
		hideElement($('#welcome-message'));
		hideElement($('#search-buttons-container'));
		hideElement($('#search-title'));
		hideElement($('#search-form'));
		hideElement($('#ticker-exist-notice'))
		$('#username').val('');
		var url = './price';
    	var params = constructParams($('#username'));
    	$('#user-meta-data-container').html('<p id = "user-balance-login-prompt">' + 
		'Login to see user balance information'+
		'</p>' + 
		'<div id = "user-actual-data-container">' + 
			'<div id = "balance-and-assets"> ' + 
			'</div> ' + 
			'<div id = "assets-holdings">' +		
			'</div></div>');
		loadWatchList();
		loadChart();
	}
    window.onload = init;
})();


//    "Note": "Thank you for using Alpha Vantage! Our standard API call 
//    frequency is 5 calls per minute and 500 calls per day. 
//     Please visit https://www.alphavantage.co/premium/ 
//   if you would like to target a higher API call frequency."
