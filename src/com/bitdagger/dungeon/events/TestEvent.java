package com.bitdagger.dungeon.events;

public class TestEvent extends Event
{
	int data;
	
	public TestEvent(int data)
	{
		super();
		
		this.data = data;
	}
	
	public TestEvent(int data, int priority)
	{
		super(priority);
		
		this.data = data;
	}
	
	public int getData()
	{
		return this.data;
	}
}
