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
				showElement($('login-success-notice'));
				navBarOnLogin();
			}
			else{
				showElement($('login-error-notice'));
			}
		},

		// error
		function() {
			console.log("Something is Wrong");
			debugger;
		});
		
	}
	
	function navBarOnLogin(){
		showElement(window.opener.document.getElementById('logout-button'));
		showElement(window.opener.document.getElementById('welcome-message'));
		hideElement(window.opener.document.getElementById('login-button'));
	}
    window.onload = init_login;
})();