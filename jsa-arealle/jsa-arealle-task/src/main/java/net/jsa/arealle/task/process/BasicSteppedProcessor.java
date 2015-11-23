package net.jsa.arealle.task.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;

public class BasicSteppedProcessor implements ISteppedProcessor {
	
	private Logger log = LogUtils.getLogger();
	private String name;
	private Map<String, Object> context;
	private List<IProcessStep> steps;
	private int currentStep = 0;
	private ProcessorState state = ProcessorState.IDLE;
		
	public BasicSteppedProcessor(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setSteps(List<IProcessStep> steps) {
		this.steps = steps;
	}

	@Override
	public int getStepCount() {
		return steps != null ? steps.size() : 0;
	}
	
	@Override
	public void initRun(Map<String, Object> context) {
		if (state != ProcessorState.IDLE) {
			return;
		}
		
		state = ProcessorState.RUNNING;
		currentStep = 0;
		this.context = context;
	}
	
	@Override
	public boolean nextStep() {
		if (state != ProcessorState.RUNNING || currentStep >= getStepCount()) {
			return false;
		}

		state = ProcessorState.STEP_PENDING;
		currentStep++;
		
		return true;
	}

	public int getStepNo() {
		return currentStep;
	}

	public boolean startStep() {
		if (state != ProcessorState.STEP_PENDING) {
			return false;
		}
		
		state = ProcessorState.STEP_RUNNING;
		return getCurrentStep().start(context);
	}
	
	@Override
	public void cancelStep() {
		if (state != ProcessorState.STEP_RUNNING) {
			return;
		}
		
		getCurrentStep().cancel();
		state = ProcessorState.RUNNING;
	}
	
	@Override
	public void finishStep() {
		if (state != ProcessorState.STEP_RUNNING) {
			return;
		}

		getCurrentStep().finish();
		state = ProcessorState.RUNNING;
	}

	@Override
	public void finishRun() {
		if (state != ProcessorState.RUNNING) {
			return;
		}
		
		state = ProcessorState.IDLE;
		currentStep = 0;
	}
	
	@Override
	public Map<String, Object> getContext() {
		return context;
	}

	@Override
	public List<String> getStepNames() {
		List<String> names = new ArrayList<String>(steps.size());
		for (IProcessStep step : steps) {
			names.add(step.getName());
		}
		
		return names;
	}

	@Override
	public String getStepName() {
		return inStep() ? getCurrentStep().getName() : null;
	}

	@Override
	public boolean nextStepItem() {
		if (state != ProcessorState.STEP_RUNNING) {
			return false;
		}
		
		return getCurrentStep().nextItem();
	}

	@Override
	public float getStepPctComplete() {
		if (state != ProcessorState.STEP_RUNNING) {
			return 0.0f;
		}
		
		return getCurrentStep().getPctComplete();
	}
	
	@Override
	public ProcessorState getState() {
		return state;
	}

	protected Logger getLog() {
		return log;
	}
	
	protected IProcessStep getCurrentStep() {
		if (!inStep()) {
			return null;
		}
		
		return steps.get(currentStep-1); // Step is 1-based
	}

	protected boolean inStep() {
		return state == ProcessorState.STEP_PENDING || state == ProcessorState.STEP_RUNNING;
	}
	
}
