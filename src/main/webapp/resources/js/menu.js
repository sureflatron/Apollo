
$( document ).ready(function() {
	var urlnow = window.location +"";		
	if(urlnow.indexOf('menu.xhtml') !== -1){
		eliminarCookie('colorear');
	}else{
		pintarMenu();
	}
});

function pintarMenu(){		
	var x = leerCookie("colorear");
	//var aux = $("span:contains('"+x+"')").closest("a");
	var aux=$("span:contains('"+x+"')").filter(function(){return $(this).text()==x}).closest("a");
		
	if(aux!=null){		 
		$('a.pintar').css('background', 'white');
		$(aux).css('background', '#FDEBD0');		
	}
}

var crearCookie = function (key, value) {
    expires = new Date();
    expires.setTime(expires.getTime() + 31536000000); // Estableces el tiempo de expiración, genius
    cookie = key + "=" + value + ";expires=" + expires.toUTCString();
    return document.cookie = cookie;
}

// Leer Cookie
var leerCookie = function (key) {
    keyValue = document.cookie.match("(^|;) ?" + key + "=([^;]*)(;|$)");
    if (keyValue) {
        return keyValue[2];
    } else {
        return null;
    }
}

// Eliminar Cookie
var eliminarCookie = function (key) {
    return document.cookie = key + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

$(function() {
	  $('.pintar').click(function() {
		  var aa = $(this).parent();		 
		  crearCookie("colorear", $(this).find("span:last").text());		  
	  });
});

