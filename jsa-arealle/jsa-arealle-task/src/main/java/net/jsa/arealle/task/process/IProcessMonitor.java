package net.jsa.arealle.task.process;

public interface IProcessMonitor {
	void onStart();
	void onCancel();
	void onStepStart(int sequence, String stepName);
	void onSetPctComplete(float pctComplete);
	void onFinish();
}
