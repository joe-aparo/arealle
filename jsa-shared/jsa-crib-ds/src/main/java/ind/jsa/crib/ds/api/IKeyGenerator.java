package ind.jsa.crib.ds.api;

/**
 * Interface for supporting custom DataSet key generation schemes.
 * 
 */
public interface IKeyGenerator {

    /**
     * Generate a key value.
     * 
     * @param dataSetName The name of the DataSet for which the value is being generated
     * @param keyField The name of the key field for which the value if being generated
     * 
     * @return An object representing the value
     */
    Object generateKeyValue(String dataSetName, String keyField);
}
