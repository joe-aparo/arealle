define(['backbone', 'underscore', 'jquery'], function (Backbone, _, $) {
  "use strict";

  	return Backbone.Model.extend({

  		loadSelect : function (coll, elemId, curValue, dftValue, blankItem) {
  			$(elemId + " option").remove();

			/* new Option (text, value, selectedByDefault, selected) arguments:
			 *  text (required) - the displayed option text
			 *  value - the value of the option
			 *  selectedByDefault - [true/false] - if true, sets option to the default if no other option is selected
			 *  selected - [true/false] - if true, sets option to selected
			 */
			
			if (blankItem) {
				$(elemId).append (new Option("", "", (dftValue == ""), false)); 
			}

			coll.each(function(item) {
				var itemVal = item.get("id");
				var selected = (curValue == itemVal);
				var dftSelected = (dftValue == itemVal);
				var opt = new Option(item.get("title"), itemVal, dftSelected, selected);
				
				$(elemId).append (opt); 
			});
		}
	});
});