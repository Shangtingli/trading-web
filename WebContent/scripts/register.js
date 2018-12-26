(function(){
	function init_register(){
		$('register-submit').addEventListener('click',function(event){
			event.preventDefault();
			register();
		});
	}
	
	function register(){
		var username = $('register-username-input').value;
		var password = $('register-password-input').value;
		var password2 = $('register-password-input2').value;
		var firstname = $('register-firstname-input').value;
		var lastname = $('register-lastname-input').value;
		if (firstname.length ===0 || password.length ===0 || lastname.length ===0 || username.length ===0){
			showElement($('register-empty-field-notice'));
			hideElement($('register-password-not-match-notice'));
			return;
		}
		else if (password !== password2){
			showElement($('register-password-not-match-notice'));
			hideElement($('register-empty-field-notice'));
			return;
		}
		var url = '../register';
		var params = 'userid='+username+'&password='+password+'&firstname='+firstname+'&lastname='+lastname;
		var req = JSON.stringify({});
		ajax('POST', url + '?' + params, req,
		// successful callback
		function(res) {
			var result = JSON.parse(res);
			if (result.exists === 'true'){
				hideElement($('register-empty-field-notice'));
				hideElement($('register-password-not-match-notice'));
				showElement($('duplicate-username-notice'));
			}
			else{
				hideElement($('register-empty-field-notice'));
				hideElement($('register-password-not-match-notice'));
				hideElement($('duplicate-username-notice'));
				showElement($('register-success-notice'));
				clearInput();
				setTimeout(window.close,2000);
			}
		},

		// error
		function() {
			console.log("Something is Wrong");
		});
		return;
	}
	
	function clearInput(){
		$('register-username-input').value = '';
		$('register-password-input').value = '';
		$('register-password-input2').value = '';
		$('register-firstname-input').value = '';
		$('register-lastname-input').value = '';
	}
	window.onload = init_register;
})();