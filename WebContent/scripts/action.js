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
		
		$('#long-button').on('click',function(e){
			e.preventDefault();
			onClickLong();
		})
		
		$('#short-button').on('click',function(e){
			e.preventDefault();
			onClickShort();
		})
	}
	
	function onChangeBalance(method){
		var amount = $('#capital-amount-input').val();
		var userid = $('#username',window.opener.document).val();
		var url = '../usermeta';
		var params = 'userid='+userid + '&amount='+amount + '&method=' + method;
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
					'Only positive numbers and . are allowed' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		$('#change-balance-notice').html('');
		showLoading('Adding Balance...',$('#change-balance-notice'));
		ajax('POST', url + '?' + params, req,
				function(res){
					var result = JSON.parse(res);
					$('input').val('');
					if (result.result === 'failure'){
						$('#change-balance-notice').html(
								'<span id = "change-balance-failure-notice">'+
								'Change Balance Not Successful. <br> '+
								'Amount Entered is '+
								'which exceeds current balance.' + 
								'</span>'
						);
						$('#reload-metadata',window.opener.document).triggerHandler('change');
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
				});
	}
	function onTransacton(method){
		var amount = $('#stock-amount-input').val();
		var symbol = $('#stock-symbol-input').val();
		var userid = $('#username',window.opener.document).val();
		var url = '../transaction';
		var params = 'userid=' + userid + '&action=' + method + '&symbol=' + symbol + '&amount=' + amount;
		var preCheck = checkAmountInput(amount);
		var req = JSON.stringify({});
		if (preCheck === 'empty'){
			$('#long-short-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Amount Input Cannot be Empty' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		else if (preCheck === 'invalid'){
			$('#long-short-notice').html(
					'<span class = "invalid-input-notice"> ' +
					'Only poritive numbers and . are allowed' + 
					'</span>'
			);
			$('input').val('');
			return;
		}
		$('#long-short-notice').html('');
		showLoading('Submitting Transaction...',$('#long-short-notice'));
		ajax('POST', url + '?' + params, req,
				function(res){
					var result = JSON.parse(res);
					$('input').val('');
					if (result.result === 'failure'){
						if (result.reason === 'symbol'){
							$('#long-short-notice').html(
									'<span id = "symbol-not-exist-notice">'+
									'The Symbol You Entered ' + symbol + ' Does not Exist in Database' +
									'</span>'
							);
						}
						else if (result.reason === 'amount'){
							$('#long-short-notice').html(
									'<span id = "balance-not-enough-notice">'+
									'You Do not have enough capital' +
									'</span>'
							);
						}
						
						return;
					}
					else{
						$('#long-short-notice').html(
							'<span id = "long-short-success-notice">'+
								'Transaction Successful'	+
								'</span>'
						);
						$('#reload-metadata',window.opener.document).triggerHandler('change');
						
					}
				},
				function(e){
					console.log("Something is Wrong");
				});
	}
	
	function onClickShort(){
		onTransacton('sell');
	}
	
	function onClickLong(){
		onTransacton('buy');
	}
	function onClickDecrease(){
		onChangeBalance('remove');
	}
	
	function onClickIncrease(){
		onChangeBalance('add');
	}
	
	function checkAmountInput(amount){
		if (amount.length === 0){
			return 'empty';
		}
		if (isNaN(amount) || parseFloat(amount) === 0.0){
			return 'invalid';
		}
		
		return 'valid';
	}
	
	window.onload = init_action;
})();