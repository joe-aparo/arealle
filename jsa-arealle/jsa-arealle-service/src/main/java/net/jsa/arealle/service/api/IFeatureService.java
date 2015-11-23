package net.jsa.arealle.service.api;

import java.util.List;

import net.jsa.arealle.dto.Feature;

/**
 * Service interface for accessing feature information.
 * 
 * @author jsaparo
 *
 */
public interface IFeatureService {

	/**
	 * Search for features of a particular type and which match a given term.
	 * 
	 * @param typeId The feature type to search within
	 * @param term A term to search for within the name of features of the specified type
	 * @param start The starting record to fetch from
	 * @param start The number of records to fetch
	 * 
	 * @return A list of features, or null if none found
	 */
	List<Feature> searchFeaturesByName(String typeId, String term, int start, int count);
	
	/**
	 * Retrieve a feature object.
	 * 
	 * @param id The id of the feature to retrieve
	 * @return A feature object or null if not found
	 */
	Feature retrieveFeatureById(String id);
}