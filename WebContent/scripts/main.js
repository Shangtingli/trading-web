(function(){	
	function init(){
		onLogout();
	}
    
	function onLogout(){
		var register_btn = $("register-btn");
		var login_btn = $("login-btn");
		var logout_btn = $("logout-btn");
		register_btn.addEventListener('click',showRegisterPopUp);
		login_btn.addEventListener('click',showLoginPopUp);
		logout_btn.addEventListener('click',destroyLogin);
		initBalanceChart();
		initSearchBar();
		first_call_loadDefaultWatchList();
		var time = undefined;
		if (time !== undefined)
		{var timeInt = setInterval(loadDefaultWatchList,time*1000);}
	}
	
	function onLogin(){
		
	}
	
	function destroyLogin(){
		showElement($('login-button'));
		hideElement($('logout-button'));
		hideElement($('welcome-message'));
	}
    window.onload = init;
})();


//    "Note": "Thank you for using Alpha Vantage! Our standard API call 
//    frequency is 5 calls per minute and 500 calls per day. 
//     Please visit https://www.alphavantage.co/premium/ 
//   if you would like to target a higher API call frequency."
