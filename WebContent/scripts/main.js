(function(){	
	function init(){
		var register_btn = $("register-button");
		var login_btn = $("login-button");
		register_btn.addEventListener('click',showRegisterPopUp);
		login_btn.addEventListener('click',showLoginPopUp);
		initBalanceChart();
		initWatchList();
		loadDefaultWatchList();
	}
    
    function initWatchList(){
    	console.log(DEFAULT_WATCHLIST);
    }
    window.onload = init;
})();


//    "Note": "Thank you for using Alpha Vantage! Our standard API call 
//    frequency is 5 calls per minute and 500 calls per day. 
//     Please visit https://www.alphavantage.co/premium/ 
//   if you would like to target a higher API call frequency."
