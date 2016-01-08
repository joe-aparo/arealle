package ind.jsa.crib.ds.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ind.jsa.crib.ds.api.DataSetQuery;
import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetItem;
import ind.jsa.crib.ds.api.DataSetQuery.FilterOperator;
import ind.jsa.crib.ds.internal.ListDataSetResultHandler;

import org.junit.Assert;

/**
 * DataSet test.
 */
public abstract class AbstractDataSetTest {

    private static final Map<String, Object> TEST_CREATE_RECORD = new LinkedHashMap<String, Object>();
    private static final Map<String, Object> TEST_UPDATE_RECORD = new LinkedHashMap<String, Object>();
    private static final int TEST_ROW_COUNT = 10;
    private static final String UPDATED_NAME_VAL = "Updated Name";

    static {
        TEST_CREATE_RECORD.put(TestConstants.NAME_COL, "Inserted record");
        TEST_CREATE_RECORD.put(TestConstants.DATE_COL, new Date());
        TEST_CREATE_RECORD.put(TestConstants.DOUBLE_COL, new Double(250));
        TEST_CREATE_RECORD.put(TestConstants.BOOLEAN_COL, Boolean.TRUE);

        TEST_UPDATE_RECORD.put(TestConstants.NAME_COL, "Updated record");
        TEST_UPDATE_RECORD.put(TestConstants.DOUBLE_COL, new Double(225));
        TEST_CREATE_RECORD.put(TestConstants.BOOLEAN_COL, Boolean.FALSE);
    }

    @Resource(name="testDs")
    private IDataSet dataSet;

    private List<IDataSetItem> addedItems = new ArrayList<IDataSetItem>();

    /**
     * Integrated test.
     */
    protected void orchestratedDataSetTest() {
        createTest();
        retrieveAllTest();
        queryRetrieveTest();
        idRetrieveTest();
        ordinalRetrieveTest();
        idUpdateTest();
        itemCountTest();
        queryUpdateTest();
        deleteTest();
    }

    protected IDataSet getDataSet() {
    	return dataSet;
    }
    
    /**
     * Create test.
     */
    private void createTest() {
        int i;
        for (i = 0; i < TEST_ROW_COUNT; i++) {
            IDataSetItem newItem = dataSet.create(new HashMap<String, Object>(TEST_CREATE_RECORD));

            Assert.assertTrue(newItem != null);

            addedItems.add(newItem); // record new record
        }
    }

    /**
     * Retrieve test.
     */
    private void retrieveAllTest() {
        List<IDataSetItem> items = dataSet.retrieve();

        Assert.assertTrue(items.size() == TEST_ROW_COUNT);
    }

    /**
     * Retrieve test.
     */
    private void queryRetrieveTest() {
        // set up a simple query on name field
        DataSetQuery query = new DataSetQuery();
        query.putFilter(TestConstants.NAME_COL, FilterOperator.CONTAINS, "Inserted");

        // set up handler
        ListDataSetResultHandler handler = new ListDataSetResultHandler(dataSet, query);

        // retrieve data and pass result to handler
        dataSet.retrieve(query, handler);

        Assert.assertTrue(handler.getItems().size() > 0);
    }

    /**
     * Retrieve test.
     */
    private void idRetrieveTest() {
        Map<String, Object> key = new HashMap<String, Object>();
        key.put(TestConstants.ID_COL, addedItems.get(0).getString(TestConstants.ID_COL));

        IDataSetItem item = dataSet.retrieve(key);

        Assert.assertTrue(item != null);
    }

    /**
     * Retrieve test.
     */
    private void ordinalRetrieveTest() {
        IDataSetItem item = dataSet.retrieve(5);

        Assert.assertTrue(item != null);
    }

    /**
     * Update test.
     */
    private void idUpdateTest() {
        Map<String, Object> vals = new HashMap<String, Object>(TEST_UPDATE_RECORD);

        vals.put(TestConstants.ID_COL, addedItems.get(0).getString(TestConstants.ID_COL));

        IDataSetItem item = dataSet.update(vals);

        Assert.assertTrue(item != null);
    }

    /**
     * Count test.
     */
    private void itemCountTest() {
        int ct = dataSet.getItemCount();

        Assert.assertTrue(ct == TEST_ROW_COUNT);
    }
    
    /**
     * Update test.
     */
    private void queryUpdateTest() {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put(TestConstants.NAME_COL, UPDATED_NAME_VAL);
        values.put(TestConstants.DOUBLE_COL, new Double(333));

        // update the first item based on a query
        DataSetQuery query = new DataSetQuery();
        String idVal = addedItems.get(0).getString(TestConstants.ID_COL);
        query.putFilter(TestConstants.ID_COL, FilterOperator.EQUAL, idVal);

        dataSet.update(query, values);

        ListDataSetResultHandler handler = new ListDataSetResultHandler(dataSet, query);
        dataSet.retrieve(query, handler);

        Assert.assertTrue(handler.getItems().size() == 1);

        IDataSetItem item = handler.getItems().get(0);

        Assert.assertTrue(UPDATED_NAME_VAL.equals(item.getString(TestConstants.NAME_COL)));
    }

    /**
     * Delete test.
     */
    public void deleteTest() {
        Map<String, Object> keys = new HashMap<String, Object>();
        for (IDataSetItem item : addedItems) {
            keys.put(TestConstants.ID_COL, item.getString(TestConstants.ID_COL));
            dataSet.delete(keys);
        }

        ListDataSetResultHandler handler = new ListDataSetResultHandler(dataSet, new DataSetQuery());
        dataSet.retrieve(handler);

        Assert.assertTrue(handler.getItems().size() == 0);
    }
}
