/*global define*/

define(['backbone', 'models/Feature', 'models/InstanceLoader'], 
	function (Backbone, Feature, InstanceLoader) {

	"use strict";

	return Backbone.Model.extend({
		defaults: {
			dlg: null,
			dlgWin: null,
			dlgPos: {top: 0, left: 0},
			dlgSize: {width: 300, height: 200},
			dlgMinSize: {width: 150, height: 45},
			dlgMinMaxBtn: null,
			map: null,
			townId: null,
			townInfo: null
		},
		
		initialize: function() {
		    this.on('change:townId', function() {
		    	this.syncTownId();
		    });

		    this.on('change:townInfo', function() {
		    	this.syncTownInfo();
		    });
		},
		
		syncTownId: function() {
			var feature = new Feature({
				id : this.get("townId"),
				targetModel : this,
				targetProperty : "townInfo"
			});
			
			feature.fetch();
		},
		
		syncTownInfo: function() {
			
        	// Detach existing town polygons from the map
        	var townPolys = this.get("townPolygons");
        	if (townPolys) {
	            for (var ix in townPolys) {
	            	var poly = jsnObj[ix];
	            	poly.setMap(null);
	            }
        	}

			// Feature shapes are represented as a json string
        	var jsn = this.get("townInfo").gmapJson;
        	if (!jsn) {
        		return;
        	}
        	
        	// Eval the google map json string. Should be a list of polygons.
        	var jsnObj = eval(jsn);
        	if (!jsnObj) {
        		return;
        	}
        	
    		// Tweak the polygons for display
        	for (var ix in jsnObj) {
        		var polygon = jsnObj[ix];
        		
        		polygon.setOptions({
        			geodesic : true,
        			strokeColor : "#0000FF",
        			strokeWeight : 3,
        			fillOpacity : 0.0
        		});
        	}
        	
        	// Release large json string in the town object - no longer needed
        	var town = this.get("townInfo");
        	town.gmapJson = null;
        	
        	// Store data in model
        	this.set("townPolygons", jsnObj);
		}
	});
});