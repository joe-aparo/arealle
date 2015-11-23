require.config({
	paths : {
		underscore  : 'libs/underscore-min',
		jquery      : 'libs/jquery.min',
		jqueryui    : 'libs/jquery-ui.min',
		backbone    : 'libs/backbone-min',
		json2       : 'libs/json2.min',
		marionette  : 'libs/backbone.marionette.min',
		tpl         : 'libs/tpl',
		async       : 'libs/async'
	},
	shim : {
		underscore : {
			exports : '_'
		},
		backbone : {
			exports : 'Backbone',
			deps : ['jquery','underscore']
		},
		jqueryui: {
	        deps: ['jquery'] 
	    }
	},
	deps : ['jquery','underscore'],
});

// convert Google Maps into an AMD module with name 'gmaps'
define('gmaps', ['async!http://maps.google.com/maps/api/js?v=3&sensor=false'],
function() {
    // return the gmaps namespace for brevity
    return window.google.maps;
});

require(['app'], function(app) {
  "use strict";

  app.start();
});