package net.jsa.arealle.web.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.service.api.IFeatureService;
import net.jsa.arealle.service.api.impl.FeatureType;
import net.jsa.arealle.web.facade.IDataFacade;
import net.jsa.arealle.web.pojo.PickListItem;

@Component
public class DataFacade implements IDataFacade {

	private static final int DEFAULT_PICKLIST_START = 0;
	private static final int DEFAULT_PICKLIST_SIZE = 5;
	
	@Resource
	IFeatureService featureService;
	
	@Override
	public List<PickListItem> getTownSelections(String term) {
		
		List<Feature> features = featureService.searchFeaturesByName(
			FeatureType.TOWN.toString(), term, DEFAULT_PICKLIST_START, DEFAULT_PICKLIST_SIZE);
		
		List<PickListItem> picks = new ArrayList<PickListItem>(features.size());
		for (Feature feature : features) {
			picks.add(
				new PickListItem(feature.getId(), feature.getName() + " " + feature.getStateId()));
		}
		
		return picks;
	}

	@Override
	public Feature retrieveFeatureById(String id) {
		return featureService.retrieveFeatureById(id);
	}
}
