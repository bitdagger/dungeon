package com.bitdagger.dungeon.events;

/**
 * Base Event
 */
public abstract class Event
{
	/**
	 * Minimum priority
	 */
	public static final int MIN_PRIORITY = 10;
	
	/**
	 * Maximum priority
	 */
	public static final int MAX_PRIORITY = 0;
	
	/**
	 * Event priority
	 * 
	 * Events with a higher priority will be handled first 
	 */
	protected int priority;
	
	/**
	 * Time the event was raised in nanoseconds
	 * 
	 * Events with equal priority will be handled in the order they were raised 
	 */
	protected long timestamp;
	
	/**
	 * Construct a new Event object with default priority
	 */
	public Event()
	{
		this(MIN_PRIORITY);
	}
	
	/**
	 * Construct a new Event object with a specific priority
	 * 
	 * @param priority Event priority
	 */
	public Event(int priority)
	{
		this.priority = Math.min(MIN_PRIORITY, Math.max(MAX_PRIORITY, priority));
		this.timestamp = System.nanoTime();
	}
	
	/**
	 * Priority accessor
	 * 
	 * @return Event priority
	 */
	public int getPriority()
	{
		return this.priority;
	}
	
	/**
	 * Timestamp accessor
	 * 
	 * @return Event timestamp
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}
}
