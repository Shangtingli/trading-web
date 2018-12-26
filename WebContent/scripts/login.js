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
		// The request parameters
		var url = '../login';
		var params = 'userid=' + username + '&password=' + password;
		var req = JSON.stringify({});
		ajax('GET', url + '?' + params, req,
		// successful callback
		function(res) {
			var result = JSON.parse(res);
			if (result.result == 'success') {
				$$('dummy').innerHTML = username;
				onLoginNavBar(result.user_id);
				onSearchBar();
				onWatchList();
				userid = username;
				setTimeout(window.close,2000);
			}
			else{
				showElement($('login-error-notice'));
			}
		},
		// error
		function() {
			console.log("Something is Wrong");
		}, false);
		if ($$('dummy').innerHTML === 'none'){
			return;
		}
		url = '../price';
		loadDefaultWatchList($$('watchlist-login'),$$('watchlist-login'), $$('watchlist-login-prompt'),$$('dummy'),url);
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