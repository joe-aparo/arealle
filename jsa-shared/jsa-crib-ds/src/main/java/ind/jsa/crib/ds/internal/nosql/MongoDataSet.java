package ind.jsa.crib.ds.internal.nosql;

import java.util.Map;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetResultHandler;
import ind.jsa.crib.ds.internal.AbstractDataSet;

/**
 * Interacts with a Mongo collection through the lens of a metadata spec.
 * 
 * @author jsaparo
 *
 */
public class MongoDataSet extends AbstractDataSet {
	private IDataSetMetaData metaData;
	
	public MongoDataSet(String domain, String entity, IDataSetMetaData metaData) {
		super(domain, entity);
		
		this.metaData = metaData;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.internal.AbstractDataSet#initMetaData()
	 */
	@Override
	protected IDataSetMetaData initMetaData() {
		// simply return value passed in constructor
		return metaData;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#retrieve(ind.jsa.crib.ds.api.DataSetQuery, ind.jsa.crib.ds.api.IDataSetResultHandler)
	 */
	@Override
	public void retrieve(DataSetQuery query, IDataSetResultHandler handler) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#create(ind.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public IDataSetItem create(IDataSetItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#update(ind.jsa.crib.ds.api.DataSetQuery, ind.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public void update(DataSetQuery query, IDataSetItem item) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#update(ind.jsa.crib.ds.api.IDataSetItem)
	 */
	@Override
	public IDataSetItem update(IDataSetItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#delete(java.util.Map)
	 */
	@Override
	public void delete(Map<String, Object> keys) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see ind.jsa.crib.ds.api.IDataSet#getItemCount(ind.jsa.crib.ds.api.DataSetQuery)
	 */
	@Override
	public int getItemCount(DataSetQuery query) {
		// TODO Auto-generated method stub
		return 0;
	}
}
