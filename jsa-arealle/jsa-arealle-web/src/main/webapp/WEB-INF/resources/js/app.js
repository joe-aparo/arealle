/*global $*/

define(
  ['jquery', 'backbone', 'marionette', 'views/MainLayout', 'models/ViewState'],
  function($, Backbone, Marionette, MainLayout, ViewState) {
    "use strict";

    var gApp = new Marionette.Application();
    var gViewState = new ViewState();
    
    gApp.addRegions({
    	'appRegion' : "#app",
    });
    
    gApp.addInitializer(function() {
        this.appRegion.show(new MainLayout({model: gViewState}));
    });

    return gApp;
  }
);