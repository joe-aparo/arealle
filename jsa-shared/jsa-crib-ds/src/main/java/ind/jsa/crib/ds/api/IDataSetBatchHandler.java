package ind.jsa.crib.ds.api;

import java.util.List;

public interface IDataSetBatchHandler {
	void handleBatch(List<IDataSetItem> items);
}
