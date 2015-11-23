package net.jsa.arealle.web.facade;

import java.util.List;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.web.pojo.PickListItem;

/**
 * Front-end data facade
 * 
 * @author jsaparo
 *
 */
public interface IDataFacade {

	/**
	 * Fetch a picklist of towns matching the given term.
	 * 
	 * @param term A term used to search towns by name
	 * @return A list of pick list items
	 */
	List<PickListItem> getTownSelections(String term);
	
	/**
	 * Retrieve a feature object for a given id.
	 * @param id The id of the feature to retrieve
	 * @return A feature, or null if not found
	 */
	Feature retrieveFeatureById(String id);
}
