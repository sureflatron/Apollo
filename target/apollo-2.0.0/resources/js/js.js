function loadXMLDoc() {
	// alert("ingresando a load servlet");
	
//	netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
//	netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
//	netscape.security.PrivilegeManager.enablePrivilege("UniversalFileRead");
//	netscape.security.PrivilegeManager.enablePrivilege("CapabilityPreferencesAccess ");
//	netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserAccess"); 

	var xmlhttp;
	// var url = document.getElementById("formServlet:ihUrlServlet").value;
	// var url = document.getElementById("formLogin:ihUrlServlet").value;
	// var url = document.getElementById("formLogin:ihUrlServlet").innerHTML;
	var url = remplazarTexto(document.getElementById("ihUrlServlet").value);
	
	if(url.startsWith('http')){
		return;
	}
	
//	var ip = document.getElementById("formLogin:ihIp").value;
//	alert("ip: " + ip);

//	 alert("url: " + url);
	xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			// alert("responseText: " + xmlhttp.responseText);
			// document.getElementById("formServlet:ihIp").value = xmlhttp.responseText;

			document.getElementById("formLogin:ihIp").value = xmlhttp.responseText;
			// document.getElementById("formLogin:ihTestIp").innerHTML = xmlhttp.responseText;
			// document.getElementById("ihIpUrl").value = xmlhttp.responseText;
			// document.getElementById("formLogin:ihIp").value = xmlhttp.responseText;

			// document.getElementById("ihIp").value = xmlhttp.responseText;
			// document.getElementById("formLogin:formServlet:ihIp").value = xmlhttp.responseText;
		} else {
			// alert("not onready state change: " + xmlhttp.readyState);
		}
	}
	if (xmlhttp) {
		xmlhttp.open('POST', url, true);
		xmlhttp.onerror = function() {
			redirect(remplazarTexto(document.getElementById("ihUrlServletVolver").value));
		};
		xmlhttp.send();
	}
}

function remplazarTexto(url){
	url = url.replace(/A4853A/gi, 'https');
	url = url.replace(/A48A/gi, 'http');
	url = url.replace(/A2FA/gi, '/');
	url = url.replace(/A2EA/gi, '.');
	url = url.replace(/A3AA/gi, ':');
	
	return url;
}


function validarNroEntero(e) {
	var key = undefined;

	if (window.event) {//
		// alert('IE');
		key = e.keyCode;
	} else if (e.which) {
		// alert('FIREFOX');
		key = e.which;
	}

	// alert('tipo: ' + tipo);
	// alert('tecla: ' + tecla);
	// alert('key:' + key + ":");
	// key: 46: .
	// key: 44: ,
	// key: 13: enter

	// CTRL + V
	if (e.ctrlKey && event.keyCode == 86) {
		return true;
	}
	// CTRL + C
	if (e.ctrlKey && event.keyCode == 67) {
		return true;
	}
	// CTRL + X
	/*
	 * if (e.ctrlKey && event.keyCode == 88) { return false; }
	 */

	if (key == 13) {
		rc();
		return false;
	}
	if (key == 13 || key == 46 || key == 44) {
		return false;
	}
	if (key == undefined)
		return true;
	if (key > 57 || key < 48) {
		// if (key == 46 || key == 8 || key == 44 || key == 37 || key == 39) {
		if (key == 8 || key == 37 || key == 39) {
			return true;
		} else {
			return false;
		}
	}
	return true;
}

function validarEnter(e) {
	var key = undefined;

	if (window.event) {//
		// alert('IE');
		key = e.keyCode;
	} else if (e.which) {
		// alert('FIREFOX');
		key = e.which;
	}

	// alert('tipo: ' + tipo);
	// alert('tecla: ' + tecla);
	// alert('key:' + key + ":");
	// key: 46: .
	// key: 44: ,
	// key: 13: enter

	if (key == 13) {
		return false;
	}
	return true;
}

function redirect(url) {
	var ua = navigator.userAgent.toLowerCase(), isIE = ua.indexOf('msie') !== -1, version = parseInt(ua.substr(4, 2), 10);
	// Internet Explorer 8 and lower
	if (isIE && version < 9) {
		var link = document.createElement('a');
		link.href = url;
		document.body.appendChild(link);
		link.click();
	}
	// All other browsers can use the standard window.location.href (they don't lose HTTP_REFERER like IE8 & lower does)
	else {
		window.location.href = url;
	}
}


// DN
function disableCtrlKeyCombinationEnter(e) {
	// list all CTRL + key combinations you want to disable
	var forbiddenKeys = new Array('a', 'c', 'x', 'v');
	var key;
	var isCtrl;
	if (window.event) {
		key = window.event.keyCode; // IE
		if (window.event.ctrlKey)
			isCtrl = true;
		else
			isCtrl = false;
	} else {
		key = e.which; // firefox
		if (e.ctrlKey)
			isCtrl = true;
		else
			isCtrl = false;
	}
	console.log("key: " + key);
	// if ENTER
	if (key == 13) {
		return false;
	}

	// if ctrl is pressed check if other key is in forbidenKeys array
	if (isCtrl) {
		for (i = 0; i < forbiddenKeys.length; i++) {
			// case-insensitive comparation
			if (forbiddenKeys[i].toLowerCase() == String.fromCharCode(key).toLowerCase()) {
				// alert('Key combination CTRL + ' + String.fromCharCode(key) +
				// ' has been disabled.');
				return false;
			}
		}
	}
//	return true;
	return false;
}

function mischandler() {
	return false;
}