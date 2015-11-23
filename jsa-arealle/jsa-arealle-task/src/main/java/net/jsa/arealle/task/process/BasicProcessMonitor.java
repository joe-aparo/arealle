package net.jsa.arealle.task.process;

import org.slf4j.Logger;

import net.jsa.common.logging.LogUtils;

public class BasicProcessMonitor implements IProcessMonitor {
	private Logger log = LogUtils.getLogger();
	
	@Override
	public void onStart() {
		log.debug("Process started");
	}

	@Override
	public void onCancel() {
		log.debug("Process cancelled");
	}

	@Override
	public void onStepStart(int sequence, String stepName) {
		log.debug("Starting step: " + sequence + ":" + stepName);
	}

	@Override
	public void onSetPctComplete(float pctComplete) {
	}

	@Override
	public void onFinish() {
		log.debug("Process finished");
	}
}
