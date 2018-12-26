//	function $(tag, options) {
//        if (!options) {
//            return document.getElementById(tag);
//        }
//
//        var element = document.createElement(tag);
//
//        for (var option in options) {
//            if (options.hasOwnProperty(option)) {
//                element[option] = options[option];
//            }
//        }
//
//        return element;
//    }
	
//	function $$(tag, options) {
//        if (!options) {
//            return window.opener.document.getElementById(tag);
//        }
//
//        var element = window.opener.document.createElement(tag);
//
//        for (var option in options) {
//            if (options.hasOwnProperty(option)) {
//                element[option] = options[option];
//            }
//        }
//
//        return element;
//    }
	function showElement(element){
        element.css("display","block");
    }

    function hideElement(element){
    	element.css("display","none");
    }
    
    function showRegisterPopUp(){
    	popupWindow = window.open(
                'pages/register.html','popUpWindow',
                'height=600,width=800,left=10,top=10,resizable=yes,\
                scrollbars=yes,toolbar=yes,menubar=no,location=no,\
                directories=no,status=yes');
    }
    
    function showLoginPopUp(){
    	popupWindow = window.open(
                'pages/login.html','popUpWindow',
                'height=600,width=800,left=10,top=10,resizable=yes,\
                scrollbars=yes,toolbar=yes,menubar=no,location=no,\
                directories=no,status=yes');
    }
    
    function showActionPopUp(){
    	popupWindow = window.open(
                'pages/action.html','popUpWindow',
                'height=600,width=800,left=10,top=10,resizable=yes,\
                scrollbars=yes,toolbar=yes,menubar=no,location=no,\
                directories=no,status=yes');
    }
    function ajax(method, url, data, callback, errorHandler, async = true) {
        var xhr = new XMLHttpRequest();

        xhr.open(method, url, async);

        xhr.onload = function() {
			if (xhr.status === 200) {
				callback(xhr.responseText);
			} else if (xhr.status === 403) {
				onSessionInvalid();
			} else {
				errorHandler();
			}
		};

        xhr.onerror = function() {
            console.error("The request couldn't be completed.");
            errorHandler();
        };

        if (data === null) {
            xhr.send();
        } else {
            console.log(data);
            xhr.setRequestHeader("Content-Type",
                "application/json;charset=utf-8");
            xhr.send(data);
        }
    }
    
    function constructParams(refElement){
    	var userid = refElement.val();
    	if (userid.length === 0){
	    	var params = 'userid=' + '&';
	    	for (let i=0; i < DEFAULT_WATCHLIST.length;i++){
	    		var assetlabel = 'asset' + i.toString() + '=';
	    		if (i != DEFAULT_WATCHLIST.length - 1)
	    		{
	    			params += assetlabel + DEFAULT_WATCHLIST[i] + '&';
	    		}
	    		else{
	    			params += assetlabel + DEFAULT_WATCHLIST[i];
	    		}
	    	}
    	}
    	
    	else{
    		var params = 'userid=' + userid;
    	}
    	return params;
    }
    
   