package net.jsa.arealle.task.process;

public interface IProcessInitiator {
	void onProcessStart();
	boolean getCancelled();
	void setPctComplete(float pct);
	void onProcessFinish();
	void onStartStep(int stepNo, String stepName);
}
