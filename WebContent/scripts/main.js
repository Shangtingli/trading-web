(function(){	
	function init(){
		onLogout();
	}
    
	function onLogout(){
		var register_btn = $("#register-btn");
		var login_btn = $("#login-btn");
		var logout_btn = $("#logout-btn");
		var action_btn = $("#action-button");
		register_btn.on('click',showRegisterPopUp);
		login_btn.on('click',showLoginPopUp);
		logout_btn.on('click',destroyLogin);
		action_btn.on('click',showActionPopUp);
		initSearchBar();
		debugger;
		first_call_loadDefaultWatchList();
		var time = undefined;
		if (time !== undefined)
		{var timeInt = setInterval(loadDefaultWatchList,time*1000);}
	}
	
	function destroyLogin(){
		showElement($("#login-button"));
		hideElement($('#logout-button'));
		hideElement($('#welcome-message'));
		$('#dummy').innerHTML = 'none';
		var url = './price';
    	var params = constructParams($('#dummy'));
		loadDefaultWatchList(true);
	}
    window.onload = init;
})();


//    "Note": "Thank you for using Alpha Vantage! Our standard API call 
//    frequency is 5 calls per minute and 500 calls per day. 
//     Please visit https://www.alphavantage.co/premium/ 
//   if you would like to target a higher API call frequency."
