package com.bitdagger.dungeon.events;

public abstract class Event
{
	public static final int MIN_PRIORITY = 10;
	public static final int MAX_PRIORITY = 0;
	
	protected int priority;
	
	protected long timestamp;
	
	public Event()
	{
		this(MIN_PRIORITY);
	}
	
	public Event(int priority)
	{
		this.priority = Math.min(MIN_PRIORITY, Math.max(MAX_PRIORITY, priority));
		this.timestamp = System.nanoTime();
	}
	
	public int getPriority()
	{
		return this.priority;
	}
	
	public long getTimestamp()
	{
		return this.timestamp;
	}
}
