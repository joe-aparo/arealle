package net.jsa.arealle.task.process;

import java.util.Map;

public interface IProcessStep {
	String getName();
	boolean start(Map<String, Object> context);
	boolean nextItem();
	void cancel();
	float getPctComplete();
	void finish();
	Map<String, Object> getContext();
	StepState getState();
}
