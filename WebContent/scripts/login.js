(function(){	
	function init(){
		$("login-submit").addEventListener('click',login);
	}
    
	function login() {
		var username = $('login-username-input').value;
		var password = $('login-password-input').value;

		// The request parameters
		var url = '../login';
		var params = 'userid=' + username + '&password=' + password;
		var req = JSON.stringify({});
		debugger;
		ajax('GET', url + '?' + params, req,
		// successful callback
		function(res) {
			debugger;
			var result = JSON.parse(res);

			// successfully logged in
			if (result.result === 'success') {
				console.log("SUCCESS");
				window.close();
			}
			else{
				console.log("Invalid Password");
			}
		},

		// error
		function() {
			console.log("Something is Wrong");
		});
	}
    window.onload = init;
})();