package net.jsa.arealle.web.controller;

import java.util.List;

import javax.annotation.Resource;

import net.jsa.arealle.dto.Feature;
import net.jsa.arealle.web.facade.IDataFacade;
import net.jsa.arealle.web.pojo.PickListItem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

	@Resource
	IDataFacade dataFacade;
	    
    @RequestMapping(value = "/townPicks", method = RequestMethod.GET)
    public @ResponseBody List<PickListItem> getTownSelection(@RequestParam(value="term", defaultValue="*") String term) {
    	List<PickListItem> picks = dataFacade.getTownSelections(term);
    	return picks;
    }
    
    @RequestMapping(value = "/townFeature", method = RequestMethod.GET)
    public @ResponseBody Feature getTownFeature(@RequestParam(value="id") String id) {
    	return dataFacade.retrieveFeatureById(id);
    }
}