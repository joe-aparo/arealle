package net.jsa.arealle.task.process;

import java.util.HashMap;
import java.util.Map;

import net.jsa.arealle.task.pojo.ListingInfo;

//@Component
public class ScrapedListingInitiator extends AbstractProcessStep {

	public static final String SCRAPED_LISTINGS_MAP = "scraped.listings.map";
	
	ScrapedListingInitiator() {
		super("Initiate scraping process");
	}
	
	public boolean start(Map<String, Object> context) {
		if (!super.start(context)) {
			return false;
		}
	
		context.put(SCRAPED_LISTINGS_MAP, new HashMap<String, Map<String, ListingInfo>>());
		
		return true;
	}
	
	@Override
	public boolean nextItem() {
		// no-op
		return false;
	}

	@Override
	public float getPctComplete() {
		// no-op
		return 1.0f;
	}
}
