(function(){	
	function init_login(){
		$("login-submit").addEventListener('click',function(event){
			event.preventDefault();
			login();
		});
	}
    
	function login() {
		var username = $('login-username-input').value;
		var password = $('login-password-input').value;
		var url = '../login';
		var params = 'userid=' + username + '&password=' + password;
		var req = JSON.stringify({});
		ajax('GET', url + '?' + params, req,
		function(res) {
			var result = JSON.parse(res);
			if (result.result == 'success') {
				$$('dummy').innerHTML = username;
				onLoginNavBar(result.user_id);
				onSearchBar();
				onWatchList();
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
		loadDefaultWatchList(false);
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
		showElement($$('action-button'));
	}
    window.onload = init_login;
})();