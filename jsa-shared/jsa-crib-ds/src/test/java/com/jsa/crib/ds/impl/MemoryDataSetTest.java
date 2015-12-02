package com.jsa.crib.ds.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
  * Simple subclass for applying memory context configuration.
  */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:memoryzzDataSetContext-test.xml" })
public class MemoryDataSetTest extends AbstractDataSetTest {

	@Test
    public void runTest() {
    	this.orchestratedDataSetTest();
    }
}