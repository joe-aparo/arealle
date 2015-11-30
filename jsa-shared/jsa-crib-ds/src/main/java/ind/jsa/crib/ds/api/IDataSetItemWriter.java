package ind.jsa.crib.ds.api;

/**
 * Interface for writing the contents of a data set item to an output result.
 * 
 * @author jsaparo
 *
 */
public interface IDataSetItemWriter {
	/**
	 * Initiate the output process.
	 * 
	 * @param state Output state object
	 */
	public void writeStart();	
	
	/**
	 * Write an item to the output result.
	 * 
	 * @param item The item to write
	 * @param state Output state object
	 */
	public void writeItem(IDataSetItem item);

	/**
	 * Terminate the output process.
	 * 
	 * @param state Output state object
	 */
	public void writeEnd();	
}
