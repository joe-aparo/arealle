package net.jsa.arealle.task.process;

import java.util.Map;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;

public abstract class AbstractProcessStep implements IProcessStep {
	private Logger log = LogUtils.getLogger();
	private String name;
	private Map<String, Object> context;
	private StepState state = StepState.IDLE;
		
	public AbstractProcessStep(String name) {
		this.name = name;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public boolean start(Map<String, Object> context) {
		if (state != StepState.IDLE) {
			return false;
		}
		
		state = StepState.RUNNING;
		this.context = context;
		
		return true;
	}
	
	@Override
	public void cancel() {
		if (state != StepState.RUNNING) {
			return;
		}
		
		state = StepState.IDLE;
		context = null;
	}
	
	@Override
	public void finish() {
		if (state != StepState.RUNNING) {
			return;
		}
		
		state = StepState.IDLE;
		context = null;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public StepState getState() {
		return state;
	}
	
	protected Logger getLog() {
		return log;
	}
}
