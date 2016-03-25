package com.bitdagger.dungeon.client.events;

/**
 * Event for a key release
 */
public final class KeyReleaseEvent extends KeyEvent
{	
	/**
	 * Construct a new KeyReleaseEvent object with default priority
	 * 
	 * @param key
	 * @param scancode
	 * @param mods
	 */
	public KeyReleaseEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	/**
	 * Construct a new KeyReleaseEvent object with specified priority
	 * 
	 * @param key Key code
	 * @param scancode Scancode
	 * @param mods Key modifiers
	 * @param priority Event priority
	 */
	public KeyReleaseEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
