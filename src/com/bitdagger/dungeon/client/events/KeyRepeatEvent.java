package com.bitdagger.dungeon.client.events;

/**
 * Event for a key repeat
 */
public final class KeyRepeatEvent extends KeyEvent
{	
	/**
	 * Construct a new KeyRepeatEvent object with default priority
	 * 
	 * @param key
	 * @param scancode
	 * @param mods
	 */
	public KeyRepeatEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	/**
	 * Construct a new KeyRepeatEvent object with specified priority
	 * 
	 * @param key Key code
	 * @param scancode Scancode
	 * @param mods Key modifiers
	 * @param priority Event priority
	 */
	public KeyRepeatEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
