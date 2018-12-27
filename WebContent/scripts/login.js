(function(){	
	function init_login(){
		$("#login-submit").on('click',function(e){
			e.preventDefault();
			login();
		});
	}
    
	function login() {
		var username = $('#login-username-input').val();
		var password = $('#login-password-input').val();
		var url = '../login';
		var params = 'userid=' + username + '&password=' + password;
		var req = JSON.stringify({});
		ajax('GET', url + '?' + params, req,
		function(res) {
			var result = JSON.parse(res);
			if (result.result == 'success') {
				$('#username',window.opener.document).val(username).triggerHandler('change');
				onLoginNavBar(result.user_id);
				onSearchBar();
				onWatchList();
				setTimeout(window.close,1500);
			}
			else{
				showElement($('#login-error-notice'));
			}
		},
		// error
		function() {
			console.log("Something is Wrong");
		}, false);
		if ($('#username',window.opener.document).val().length === 0){
			return;
		}
		loadDefaultWatchList();
	}
	
	function onLoginNavBar(userid){
		showElement($('#login-success-notice'));
		hideElement($('#login-error-notice'));
		var welcome = $('#welcome-message',window.opener.document);
		var logout_btn = $('#logout-button',window.opener.document);
		welcome.html('<span>Welcome ' + userid + '</span>');
		showElement(logout_btn);
		showElement(welcome);
		hideElement($('#login-button',window.opener.document));
	}
	
	function onSearchBar(){
		showElement($('#search-title',window.opener.document));
		showElement($('#search-form',window.opener.document));
		hideElement($('#search-bar-login-prompt',window.opener.document));
	}
	
	function onWatchList(){
		showElement($('#action-button',window.opener.document));
	}
    window.onload = init_login;
})();