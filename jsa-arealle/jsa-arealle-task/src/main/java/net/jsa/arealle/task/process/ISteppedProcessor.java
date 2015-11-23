package net.jsa.arealle.task.process;

import java.util.List;
import java.util.Map;

public interface ISteppedProcessor {
	String getName();
	List<String> getStepNames();
	int getStepCount();
	void initRun(Map<String, Object> context);	
	int getStepNo();
	String getStepName();
	boolean startStep();
	boolean nextStepItem();
	float getStepPctComplete();
	void finishStep();
	boolean nextStep();
	void cancelStep();
	void finishRun();
	Map<String, Object> getContext();
	ProcessorState getState();
}
