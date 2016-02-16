package ind.jsa.crib.ds.impl;

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
		displayMetaData();
    }
}
