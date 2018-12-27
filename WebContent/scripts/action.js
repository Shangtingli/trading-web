(function(){
	function init_action(){
		$('#increase-capital-button').on('click',function(e){
			e.preventDefault();
			onClickIncrease();
		});
		
		$('#reduce-capital-button').on('click',function(e){
			e.preventDefault();
			onClickDecrease();
		});
	}
	
	function onClickIncrease(){
		var amount = $('#capital-amount-input').val();
		var userid = $('#username',window.opener.document).val();
		var url = '../usermeta';
		var params = 'userid='+userid + '&amount='+amount + '&method=add';
		var req = JSON.stringify({});
		var preCheck = checkAmountInput(amount);
		if (preCheck === 'empty'){
			$('#change-balance-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Amount Input Cannot be Empty' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		else if (preCheck === 'invalid'){
			$('#change-balance-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Only numbers and . are allowed' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		ajax('POST', url + '?' + params, req,
				function(res){
					var result = JSON.parse(res);
					if (result.result === 'failure'){
						$('#change-balance-notice').html(
								'<span id = "change-balance-failure-notice">'+
								'Change Balance Not Successful. <br> '+
								'Amount Entered is '+
								'which exceeds current balance.' + 
								'</span>'
						);
						return;
					}
					else{
						$('#change-balance-notice').html(
							'<span id = "change-balance-success-notice">'+
								'Change Balance Successful'	+
								'</span>'
						);
						
					}
				},
				function(e){
					console.log("Something is Wrong");
				},false);
		$('input').val('');
		$('#reload-metadata',window.opener.document).triggerHandler('change');
	}
	
	function onClickDecrease(){
		var amount = $('#capital-amount-input').val();
		var userid = $('#username',window.opener.document).val();
		var url = '../usermeta';
		var params = 'userid='+userid + '&amount='+amount + '&method=remove';
		var req = JSON.stringify({});
		var preCheck = checkAmountInput(amount);
		if (preCheck === 'empty'){
			$('#change-balance-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Amount Input Cannot be Empty' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		else if (preCheck === 'invalid'){
			$('#change-balance-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Only numbers and . are allowed' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		ajax('POST', url + '?' + params, req,
				function(res){
					var result = JSON.parse(res);
					if (result.result === 'failure'){
						$('#change-balance-notice').html(
								'<span id = "change-balance-failure-notice">'+
								'Change Balance Not Successful. <br> '+
								'Amount Entered is '+ amount+
								' <br> Which exceeds current balance.' + 
								'</span>'
						);
						return;
					}
					else{
						$('#change-balance-notice').html(
							'<span id = "change-balance-success-notice">'+
								'Change Balance Successful'	+
								'</span>'
						);
						
					}
				},
				function(e){
					console.log("Something is Wrong");
				},false);
		$('input').val('');
		$('#reload-metadata',window.opener.document).triggerHandler('change');
	}
	
	function checkAmountInput(amount){
		if (amount.length === 0){
			return 'empty';
		}
		if (isNaN(parseFloat(amount))){
			return 'invalid';
		}
		
		return 'valid';
	}
	window.onload = init_action;
})();