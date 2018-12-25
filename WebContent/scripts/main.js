(function(){	
	function init(){
		onLogout();
	}
    
	function onLogout(){
		var register_btn = $("register-btn");
		var login_btn = $("login-btn");
		register_btn.addEventListener('click',showRegisterPopUp);
		login_btn.addEventListener('click',showLoginPopUp);
		initBalanceChart();
		
		first_call_loadDefaultWatchList();
		var time = undefined;
		if (time !== undefined)
		{var timeInt = setInterval(loadDefaultWatchList,time*1000);}
	}
	
	function onLogin(){
		
	}
    window.onload = init;
})();


//    "Note": "Thank you for using Alpha Vantage! Our standard API call 
//    frequency is 5 calls per minute and 500 calls per day. 
//     Please visit https://www.alphavantage.co/premium/ 
//   if you would like to target a higher API call frequency."
