define(['backbone'], 
	function (Backbone) {
	"use strict";

	return Backbone.Model.extend({
		defaults: {
			item: null
		},

		load: function(model, restUrl, responseProperty, modelProperty) {

			var cls = Backbone.Model.extend({
				url: restUrl, 
				parse: function(response) {
					model.set(modelProperty, response[responseProperty]);
				}
			});
			
			var obj = new cls();
			
			// load states
			obj.fetch({
				error: function(obj,resp,opts) {
					console.log("Error loading object for url: " + restUrl + " and item key: " + responseProperty + ". " + resp);
				}});
			
			return obj;
		},
	});
});