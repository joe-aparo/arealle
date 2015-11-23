package net.jsa.arealle.task.process;

import java.util.Map;

import net.jsa.arealle.task.process.IProcessInitiator;

public class SteppedProcessRunner implements IProcessInitiator, Runnable {

	private ISteppedProcessor processor;
	private Map<String, Object> context;
	private IProcessMonitor monitor;
	float pctComplete;
	private boolean started = false;
	private boolean cancelled = false;	
	
	public SteppedProcessRunner(ISteppedProcessor processor, Map<String, Object> context, IProcessMonitor monitor) {
		this.processor = processor;
		this.context = context;
		this.monitor = monitor;
	}

	@Override
	public void onProcessStart() {
		started = true;
		cancelled = false;

		monitor.onStart();
	}

	@Override
	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
		monitor.onCancel();
	}
	
	@Override
	public void setPctComplete(float pctComplete) {
		this.pctComplete = pctComplete;
		
		monitor.onSetPctComplete(pctComplete);
		
		if (pctComplete == 1.0f && processor.getStepNo() == processor.getStepCount()) {
			onProcessFinish();
		}
	}
	
	public float getPctComplete() {
		return pctComplete;
	}

	@Override
	public void onProcessFinish() {
		started = false;
		
		monitor.onFinish();
	}

	@Override
	public void onStartStep(int stepNo, String stepName) {
		monitor.onStepStart(stepNo, stepName);
	}
	
    public void run() {
    	if (started) {
    		return;
    	}
    	
		onProcessStart();
		ProcessRunner runner = new ProcessRunner(this, processor, context);
		runner.run();
    }
    
    static class ProcessRunner implements Runnable {
    	
    	private IProcessInitiator initiator;
    	private ISteppedProcessor processor;
    	private Map<String, Object> context;
    	
    	public ProcessRunner(IProcessInitiator initiator, ISteppedProcessor processor, Map<String, Object> context) {
    		this.initiator = initiator;
    		this.processor = processor;
    		this.context = context;
    	}
    	
 		@Override
		public void run() {
			float pct = 0.0f;

			processor.initRun(context);
	    	
			while (processor.nextStep()) {
				initiator.onStartStep(processor.getStepNo(), processor.getStepName());
				processor.startStep();
				
		    	while (processor.nextStepItem()) {
		    		if (initiator.getCancelled()) {
		    			processor.cancelStep();
		    			break;
		    		}
		 
		    		pct = processor.getStepPctComplete();
		    		
		    		initiator.setPctComplete(pct);
		    	}
	
		    	// Ensure 100% is ultimately sent to initiator if not already
		    	if (pct != 1.0f) {
		    		initiator.setPctComplete(1.0f);
		    	}

		    	if (initiator.getCancelled()) {
		    		break;
		    	}
		    		
		    	processor.finishStep();
			}
			
			processor.finishRun();
	    }
    	
    }
}
