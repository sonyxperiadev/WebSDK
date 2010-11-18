var current_pane = "thePulse";
var map;

function show(pane, parent_pane) {
	if ((pane != current_pane) || (typeof parent_pane != "undefined")) {
		// update navigation
		$$("nav ul li.selected").removeClass('selected');
		
		// if it's a sub-pane, set the appropriate parent pane in the nav
		if (typeof parent_pane == "undefined") {
			$("nav_"+pane).addClass('selected');
		} else {
			$("nav_"+parent_pane).addClass('selected');		
		}
		
		// hide current pane
		$(current_pane).addClass('hideContent');

		// display pane
		$(pane).removeClass('hideContent');

		// update visible pane
		current_pane = pane;
	}
}

function showSurfer(surfer) {
	// set pane to new content
	// navigator.file.read("./surfers/"+surfer+".html", function(data){$("the24Single").set("html", data);});
	$("surferProfile").set("src", "./surfers/"+surfer+".html");

	// set nav, show pane
	show("the24Single", "the24list");	
}


function userLocated (p) {
	// set map to current location
	var currentLocation = new google.maps.LatLng(p.coords.latitude, p.coords.longitude);  
	map.setCenter(currentLocation, 13);
	
	// calculate directions to Half Moon Bay
	var directions = new google.maps.Directions(map);
	GEvent.addListener(directions, "load", function() {
		$("proximityText").set("html", directions.getDistance().html + ", " + directions.getDuration().html);
	});
	directions.load("from: "+p.coords.latitude+","+p.coords.longitude+" to: 37.49883,-122.498034", {"preserveViewport":true});
};
var userLocationNotFound = function() {
	// User location unknown, so let's just show where Half Moon Bay is located
	var currentLocation = new google.maps.LatLng(37.49883, -122.498034);  
	map.setCenter(currentLocation, 13);
	$("proximityText").set("html", "<em>Unable to Locate</em>");
};

function smsPrompt() {
	navigator.sms.send(81595, 'MAVERICKS');
	return false;
}