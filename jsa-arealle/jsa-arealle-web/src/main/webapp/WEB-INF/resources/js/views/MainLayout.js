/*global define*/

define(
	['marionette', 'underscore', 'jquery', 'views/templates', 'views/MapView', 'views/FilterView'], 
	function (Marionette, _, $, templates, MapView, FilterView) {
	
	"use strict";

	return Marionette.LayoutView.extend({
		className: 'full-size',
		template : templates.mainLayout,

		regions : {
			mapRegion: "#map-region",
			filterRegion: "#filter-region"
		},

		initialize: function() {
			this.mapView = new MapView({model: this.model});
			this.filterView = new FilterView({model: this.model});
		},
		
		onShow: function() {
			this.mapRegion.show(this.mapView);
			this.filterRegion.show(this.filterView);
		}
	});
});