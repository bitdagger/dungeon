package com.bitdagger.dungeon.client.events;

import com.bitdagger.dungeon.events.Event;

public abstract class KeyEvent extends Event
{
	private int key;
	private int scancode;
	private int mods;
	
	public KeyEvent(int key, int scancode, int mods)
	{
		super();
		
		this.key = key;
		this.scancode = scancode;
		this.mods = mods;
	}
	
	public KeyEvent(int key, int scancode, int mods, int priority)
	{
		super(priority);
		
		this.key = key;
		this.scancode = scancode;
		this.mods = mods;
	}
	
	public int getKey()
	{
		return this.key;
	}
	
	public int getScancode()
	{
		return this.scancode;
	}
	
	public int getMods()
	{
		return this.mods;
	}
}
