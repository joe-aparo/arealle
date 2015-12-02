package ind.jsa.crib.ds.internal;


import java.util.UUID;

import net.jsa.crib.ds.api.IKeyGenerator;

/**
 *  GUID key generator implementation.
 */
public class GuidKeyGenerator implements IKeyGenerator {

    /*
     * (non-Javadoc)
     * @see com.jsa.crib.ds.api.IKeyGenerator#generateKeyValue(java.lang.String, java.lang.String)
     */
    @Override
    public Object generateKeyValue(String dataSetName, String keyField) {
        String guid = UUID.randomUUID().toString();

        return guid;
    }
}
