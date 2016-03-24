package com.bitdagger.dungeon.client.events;

public final class KeyReleaseEvent extends KeyEvent
{	
	public KeyReleaseEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	public KeyReleaseEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
