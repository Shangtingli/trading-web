	function $(tag, options) {
        if (!options) {
            return document.getElementById(tag);
        }

        var element = document.createElement(tag);

        for (var option in options) {
            if (options.hasOwnProperty(option)) {
                element[option] = options[option];
            }
        }

        return element;
    }
	
	function showElement(element){
        element.style.display = 'block';
        console.log(element.style.display);
    }

    function hideElement(element){
        element.style.display = 'none';
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
    
    function ajax(method, url, data, callback, errorHandler) {
        var xhr = new XMLHttpRequest();

        xhr.open(method, url, true);

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
   