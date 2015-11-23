package net.jsa.arealle.task;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.jsa.arealle.task.process.BasicProcessMonitor;
import net.jsa.arealle.task.process.ISteppedProcessor;
import net.jsa.arealle.task.process.SteppedProcessRunner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Central test class for invoking stagers and loaders.
 * 
 * @author jsaparo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:/test-context.xml", 
	"classpath:/test-dataSetContext.xml", 
	"classpath:/test-taskContext.xml"})
public class TaskTest {

	@Resource(name="stageTownFeaturesProcessor")
	private ISteppedProcessor stageTownFeaturesProcessor;
	
	@Resource(name="stageWaterFeaturesProcessor")
	private ISteppedProcessor stageWaterFeaturesProcessor;
	
	@Resource(name="stageRecreationFeaturesProcessor")
	private ISteppedProcessor stageRecreationFeaturesProcessor;
	
	@Resource(name="stagePubTransFeaturesProcessor")
	private ISteppedProcessor stagePubTransFeaturesProcessor;
	
	@Resource(name="stageParcelFeaturesProcessor")
	private ISteppedProcessor stageParcelFeaturesProcessor;
	
	@Resource(name="stagePropertyFeaturesProcessor")
	private ISteppedProcessor stagePopertyFeaturesProcessor;
	
	@Resource(name="loadFeaturesProcessor")
	private ISteppedProcessor loadFeaturesProcessor;
	
	@Resource(name="loadPropertiesProcessor")
	private ISteppedProcessor loadPropertiesProcessor;
	
	@Test
	@Ignore
	public void stageTownFeaturesTest() {
		runProcessor(stageTownFeaturesProcessor);
	}
	
	@Test
	@Ignore
	public void stageWaterFeaturesTest() {
		runProcessor(stageWaterFeaturesProcessor);
	}
	
	@Test
	@Ignore
	public void stageRecFeaturesTest() {
		runProcessor(stageRecreationFeaturesProcessor);
	}

	@Test
	@Ignore
	public void stagePubTransFeaturesTest() {
		runProcessor(stagePubTransFeaturesProcessor);
	}
	
	@Test
	@Ignore
	public void stageParcelFeaturesTest() {
		runProcessor(stageParcelFeaturesProcessor);
	}

	@Test
	@Ignore
	public void stagePropertiesTest() {
		runProcessor(stagePopertyFeaturesProcessor);
	}
	
	@Test
	@Ignore
	public void loadFeaturesTest() {
		runProcessor(loadFeaturesProcessor);
	}
	
	@Test
	@Ignore
	public void loadPropertiesTest() {
		runProcessor(loadPropertiesProcessor);
	}
	
	private void runProcessor(ISteppedProcessor processor) {
		SteppedProcessRunner runner = 
			new SteppedProcessRunner(processor, getTestContext(), new BasicProcessMonitor());
		
		runner.run();
	}
	
	private Map<String, Object> getTestContext() {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("STATE_ID", "MA"); // Mass. default
		context.put("CRS_ID", "EPSG:26986"); // Mass. State Plane default
		context.put("TOWN_ID", "06884716-9bb7-4a60-b773-b060094f2be8"); // Gloucester default
		
		return context;
	}
}
