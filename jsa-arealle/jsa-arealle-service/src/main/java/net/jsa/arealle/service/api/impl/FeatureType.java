package net.jsa.arealle.service.api.impl;

/**
 * Represents a feature type
 * @author jsaparo
 *
 */
public enum FeatureType {
	BIKETRAIL("Biking Trails"),
	BUSSTOP("Bus Stops"),
	COAST("Coast Line"),
	SPACE("Parks, Beaches & Recreation"),
	POND("Ponds and Lakes"),
	PARCEL("Property Parcels"),
	TSTOP("Rapid Transit Stops"),
	RIVER("Rivers & Streams"),
	TOWN("Towns"),
 	STATION("Train/Commuter Rail Stations");

    private String label;

    /**
     * Constructor.
     * 
     * @param label Displayable label
      */
    FeatureType(String label) {
        this.label = label;
    }

    /**
     * Get displayable label.
     * 
     * @return A string
     */
    public String getLabel() {
        return label;
    }
}
