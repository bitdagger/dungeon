package com.bitdagger.dungeon.client.events;

public final class KeyPressEvent extends KeyEvent
{
	public KeyPressEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	public KeyPressEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
