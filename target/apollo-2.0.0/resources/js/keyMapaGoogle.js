	
	var currentMarker = null;
	
	function handlePointClick(event) {
	    try {
	        currentMarker.setMap(null);
	        currentMarker = null;
	
	        if (currentMarker === null) {
	
	            document.getElementById('latitud').value = event.latLng.lat();
	            document.getElementById('longuitud').value = event.latLng.lng();
	            currentMarker = new google.maps.Marker({
	                position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())
	            });
	            document.form.latitud.value = event.latLng.lat()
	            document.form.longuitud.value = event.latLng.lng()
	            PF('map').addOverlay(currentMarker);
	        }
	    } catch (e) {
	        if (currentMarker === null) {
	            document.getElementById('latitud').value = event.latLng.lat();
	            document.getElementById('longuitud').value = event.latLng.lng();
	            currentMarker = new google.maps.Marker({
	                position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())
	            });
	            document.form.latitud.value = event.latLng.lat()
	            document.form.longuitud.value = event.latLng.lng()
	            PF('map').addOverlay(currentMarker);
	        }
	    }
	}
	
	function markerAddComplete() {
	    var title = document.getElementById('title');
	    currentMarker.setTitle(title.value);
	    title.value = "";
	
	    currentMarker = null;
	    PF('dlg').hide();
	}
	
	function sub() {
	    try {
	        currentMarker.setMap(null);
	        currentMarker = null;
	        if (currentMarker === null) {
	            latitud = document.getElementsByName("latitud")[0].value;
	            longuitud = document.getElementsByName("longuitud")[0].value;
	            currentMarker = new google.maps.Marker({
	                position: new google.maps.LatLng(latitud, longuitud)
	            });
	            PF('map').addOverlay(currentMarker);
	        }
	    } catch (e) {
	        if (currentMarker === null) {
	            latitud = document.getElementsByName("latitud")[0].value;
	            longuitud = document.getElementsByName("longuitud")[0].value;
	            currentMarker = new google.maps.Marker({
	                position: new google.maps.LatLng(latitud, longuitud)
	            });
	            PF('map').addOverlay(currentMarker);
	        }
	    }
	}

	function cancel() {
	    document.form.latitud.value = ""
	    document.form.longuitud.value = ""
	    currentMarker.setMap(null);
	    currentMarker = null;
	
	    return false;
	}