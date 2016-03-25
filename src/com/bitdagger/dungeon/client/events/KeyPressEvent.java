package com.bitdagger.dungeon.client.events;

/**
 * Event for a key press
 */
public final class KeyPressEvent extends KeyEvent
{
	/**
	 * Construct a new KeyPressEvent object with default priority
	 * 
	 * @param key
	 * @param scancode
	 * @param mods
	 */
	public KeyPressEvent(int key, int scancode, int mods)
	{
		super(key, scancode, mods);
	}
	
	/**
	 * Construct a new KeyPressEvent object with specified priority
	 * 
	 * @param key Key code
	 * @param scancode Scancode
	 * @param mods Key modifiers
	 * @param priority Event priority
	 */
	public KeyPressEvent(int key, int scancode, int mods, int priority)
	{
		super(key, scancode, mods, priority);
	}
}
