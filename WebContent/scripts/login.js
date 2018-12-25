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

			// successfully logged in
			if (result.result == 'success') {
				OnLogin(result.user_id);
				setTimeout(window.close,3000);
			}
			else{
				showElement($('login-error-notice'));
			}
		},

		// error
		function() {
			console.log("Something is Wrong");
		});
		
	}
	
	function OnLogin(userid){
		showElement($('login-success-notice'));
		hideElement($('login-error-notice'));
		var welcome = $$('welcome-message');
		var logout_btn = $$('logout-button');
		welcome.innerHTML = '<span>Welcome ' + userid + '</span>';
		showElement(logout_btn);
		showElement(welcome);
		hideElement($$('login-button'));
	}
    window.onload = init_login;
})();