/*global define*/

define(['marionette', 'underscore', 'jquery', 'jqueryui', 'views/templates'], 
        function (Marionette, _, $, ui, templates) {
	
	"use strict";

	return Marionette.ItemView.extend({
		template : templates.filterView,
		
		// Bind object methods to this instance
		onRender: function() {
			_.bindAll(this, 
				"initDialog", "syncResize", "syncMove", "checkLocationChange",
				"toggleDialog", "minimizeDialog", "maximizeDialog", "moveDialog");
		},
		
		// Initialize the dialog object when the DOM is ready
		onShow: function() {
			this.initDialog();
			this.maximizeDialog();
		},

		ui: {
			location: "#location",
			townId: "#townId"
		},
		
		events: {
			"blur #location" : "checkLocationChange"
		},
		
		// Set up the dialog
		initDialog: function() {
			// Attach dialog to filter region
			var dlg = $("#filter-region").dialog({
				id: "filter-dlg",
				autoOpen : true,
				resizeStop: this.syncResize,
				dragStop: this.syncMove,
				show: {effect: "blind", duration: 800}
			});

			// Store dialog object/s in model
			this.model.set("dlg", dlg);
			var dlgWin = dlg.parent();
			this.model.set("dlgWin", dlgWin);
			
			// Capture initial size/position
			var pos = dlgWin.offset();
			this.model.set("dlgPos", {top: pos.top, left: pos.left});
			this.model.set("dlgSize", {width: dlgWin.width(), height: dlgWin.height()});
			
			// Hijack default dialog close button and store in nodel
			var btn = $(".ui-dialog-titlebar").find("[role='button']");
			btn.off('click');
			btn.on('click', this.toggleDialog);			
			this.model.set("dlgMinMaxBtn", btn);
			
			// Set up town/state selector
			$("#location").autocomplete({
				source: "townPicks",
				
			    select: function(event, ui) {
			        event.preventDefault();
			        $("#location").val(ui.item.label);
			        $("#townId").val(ui.item.value);
			    },
			    
			    focus: function(event, ui) {
			        event.preventDefault();
			        $("#location").val(ui.item.label);
			    }
			});
		},
		
		// Record manual resize event
		syncResize: function(event, ui) {
			this.model.set("dlgSize", ui.size);
		},
		
		// Record manual move event
		syncMove: function(event, ui) {
			this.model.set("dlgPos", ui.position);
		},
		
		// Toggle display of dialog
		toggleDialog: function() {
			// Base toggle on label value
			var btn = this.model.get("dlgMinMaxBtn");
			var label = btn.button( "option", "label" );
			
			if (label == 'Expand') {
				this.maximizeDialog();
			} else {
				this.minimizeDialog();
			}
		},
		
		// Minimize the dialog
		minimizeDialog: function() {
			var btn = this.model.get("dlgMinMaxBtn");
			this.model.get("dlgMinMaxBtn").button({
				icons: {primary: "ui-icon-plusthick"},
				text: false,
				label: "Expand"
			});
			
			this.moveDialog({top: this.model.get("dlgPos").top, left: 0}, this.model.get("dlgMinSize"));
		},
		
		// Maximize the dialog
		maximizeDialog: function() {
			this.model.get("dlgMinMaxBtn").button({
				icons: {primary: "ui-icon-minusthick"},
				text: false,
				label: "Shrink"
			});
			
			this.moveDialog(this.model.get("dlgPos"), this.model.get("dlgSize"));
		},
		
		// Move the dialog to the given position and size
		moveDialog: function(pos, size) {
			var dlgWin = this.model.get("dlgWin");
			dlgWin.offset({top: pos.top, left: pos.left});
			dlgWin.width(size.width);
			dlgWin.height(size.height);
		},
		
		// Location change is triggered by the events hash and uses the ui hash
		checkLocationChange: function() {
			var fromVal = this.model.get("townId");
			var toVal = this.ui.townId.val();
			
			if (toVal != "" && fromVal != toVal) {
				this.model.set("townId", toVal);
			}
		}
	});
});