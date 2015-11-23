/*global define*/

define(['marionette', 'underscore', 'jquery', 'views/templates', 'gmaps'], 
        function (Marionette, _, $, templates, gmaps) {
	
	"use strict";

	return Marionette.ItemView.extend({
		className: 'full-size',

		// Bind object methods to this instance
		onRender: function() {
			_.bindAll(this, "outlineTown");
		},
		
		modelEvents: {
			"change:townPolygons": "outlineTown"
		},

        render: function() {
            var options = {
                center:  new google.maps.LatLng(38.959442, -95.265305),
                zoom: 5,
                mapTypeId: gmaps.MapTypeId.ROADMAP
            };
            
            this.model.set("map", new gmaps.Map(this.el, options));
        },
        
        outlineTown: function() {
        	// Determine if we have a current town selected
        	var townPolys = this.model.get("townPolygons");
        	if (!townPolys) {
        		return;
        	}
        	
        	var map = this.model.get("map");
        	var townBounds = new google.maps.LatLngBounds();
        	
        	for (var ix in townPolys) {
        		var townPoly = townPolys[ix];
        		
        		// Zero in on the center of the town
        		townBounds.extend(this.getPolygonCenter(townPoly));
        		
        		// Add town polygon to map
        		townPoly.setMap(map);
			}
        	
        	// Zoom to center of town
            map.setOptions({
                center:  townBounds.getCenter(),
                zoom: 12,
                mapTypeId: gmaps.MapTypeId.ROADMAP
            });
        },
        
        getPolygonCenter: function(polygon) {
        	var bounds = new google.maps.LatLngBounds();
        	var paths = polygon.getPaths();
       	
        	paths.forEach(function(path, idx) {
        		path.forEach(function(pt, idx) {
        			bounds.extend(pt);
        		})
		    })
		    
		    return bounds.getCenter();
        }
	});
});