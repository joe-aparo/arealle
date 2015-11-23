/*global define*/

define(['backbone'], 
	function (Backbone) {

	"use strict";

	return Backbone.Model.extend({
		defaults: {
		    id : null,
		    name : null,
		    typeId : null,
		    stateId : null,
		    townId : null,
		    attrs : null,
		    geoWkt : null,
		    gmapJson : null,
		    targetModel : null,
		    targetProperty : null
		},

	    url: function() {
	    	return "/jsa-arealle-web/townFeature?id=" + this.get("id");
	    },
	    
		parse: function(response) {
			this.get("targetModel").set(this.get("targetProperty"), response);
		}
	});
});