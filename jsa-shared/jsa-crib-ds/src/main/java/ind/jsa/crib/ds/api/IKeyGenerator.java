package ind.jsa.crib.ds.api;

/**
 * Interface for supporting custom DataSet key generation schemes.
 * 
 */
public interface IKeyGenerator {

    /**
     * Generate a key value.
     * 
     * @param dataSet A dataSet object to support generation of the key value
     * 
     * @return An object representing the value
     */
    Object generateKeyValue(IDataSet dataSet);
}
