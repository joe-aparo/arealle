package ind.jsa.crib.ds.impl;

import ind.jsa.crib.ds.api.IDataSet;
import ind.jsa.crib.ds.api.IDataSetMetaData;
import ind.jsa.crib.ds.api.IDataSetProperty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple subclass for applying memory context configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:sqlDataSetContext-test.xml" })
public class SqlDataSetTest extends AbstractDataSetTest {
	
	@Test
    public void runTest() {
		IDataSet dataSet = getDataSet();
		IDataSetMetaData metaData = dataSet.getMetaData();
		
		for (IDataSetProperty prop : metaData.getProperties()) {
			System.out.println(prop.getName() + ":" + prop.getType().getName());
		}
    }
}
