package net.jsa.arealle.task.process;

import java.util.Map;

/**
 * Base class for any step that involves a total item
 * count and a current item index while processing.
 * 
 * @author jsaparo
 *
 */
public abstract class CountedProcessStep extends AbstractProcessStep {
	private static final float COMPLETE = 1.0f;
	private int currentItem;
	private int totalItems;

	public CountedProcessStep(String name) {
		super(name);
	}
	
	@Override
	public boolean start(Map<String, Object> context) {
		super.start(context);
		
		currentItem = 0;
		return true;
	}
	
	@Override
	public boolean nextItem() {
		if (currentItem >= totalItems) {
			return false;
		}
		
		advanceCurrentItem();

		return true;
	}

	@Override
	public float getPctComplete() {
		if (totalItems == 0) {
			return COMPLETE;
		}
		
		return (float) currentItem / totalItems;
	}
	
	/**
	 * Set the total item count for this step.
	 * 
	 * @param total A total item count.
	 */
	protected void setTotalItems(int total) {
		totalItems = total;
	}
	
	/**
	 * Get the total item count set for this step.
	 * 
	 * @return A total item count.
	 */
	protected int getTotalItems() {
		return totalItems;
	}

	/**
	 * Return index of item currently being processed.
	 * 
	 * @return A 1-based item index, or zero if not yet started.
	 */
	public int getCurrentItem() {
		return currentItem;
	}
	
	/**
	 * Return index of item currently being processed.
	 * 
	 * @return A 1-based item index, or zero if not yet started.
	 */
	public int advanceCurrentItem() {
		return currentItem++;
	}
}
