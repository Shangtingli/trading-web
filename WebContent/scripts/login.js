(function(){	
	function init_login(){
		$("login-submit").addEventListener('click',function(event){
			event.preventDefault();
			login();
		});
	}
    
	function login() {
//		event.preventDefault();
		var username = $('login-username-input').value;
		var password = $('login-password-input').value;
		var isLoggedin = false;
		// The request parameters
		var url = '../login';
		var params = 'userid=' + username + '&password=' + password;
		var req = JSON.stringify({});
		ajax('GET', url + '?' + params, req,
		// successful callback
		function(res) {
			var result = JSON.parse(res);
			if (result.result == 'success') {
				onLoginNavBar(result.user_id);
				onSearchBar();
				onWatchList();
				isLoggedin = true;
				setTimeout(window.close,5000);
			}
			else{
				showElement($('login-error-notice'));
			}
		},
		// error
		function() {
			console.log("Something is Wrong");
		}, false);
		
		if (isLoggedin === false){
			return;
		}
		url = '../price';
		var params = 'userid=' + username;
		loadDefaultWatchList($$('watchlist-login'),$$('watchlist-login'), $$('watchlist-login-prompt'),url,params,true,true);
	}
	
	function onLoginNavBar(userid){
		showElement($('login-success-notice'));
		hideElement($('login-error-notice'));
		var welcome = $$('welcome-message');
		var logout_btn = $$('logout-button');
		welcome.innerHTML = '<span>Welcome ' + userid + '</span>';
		showElement(logout_btn);
		showElement(welcome);
		hideElement($$('login-button'));
	}
	
	function onSearchBar(){
		showElement($$('search-title'));
		showElement($$('search-form'));
		hideElement($$('search-bar-login-prompt'));
	}
	
	function onWatchList(){

	}
    window.onload = init_login;
})();