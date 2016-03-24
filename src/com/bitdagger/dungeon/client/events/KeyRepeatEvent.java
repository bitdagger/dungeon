package com.bitdagger.dungeon.client.events;

public final class KeyRepeatEvent extends KeyEvent
{	
	public KeyRepeatEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	public KeyRepeatEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
