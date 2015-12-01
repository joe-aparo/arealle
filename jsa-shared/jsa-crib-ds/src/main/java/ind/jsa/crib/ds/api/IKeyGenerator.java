package ind.jsa.crib.ds.api;

/**
 * Interface for supporting custom DataSet key generation schemes.
 * 
 */
public interface IKeyGenerator {

    /**
     * Generate a key value.
     * 
     * @param metaData A metadata object to support generation of the key value
     * @param keyField The name of the key field for which the value should be generated
     * 
     * @return An object representing the value
     */
    Object generateKeyValue(DataSetMetaData metaData, String keyField);
}
