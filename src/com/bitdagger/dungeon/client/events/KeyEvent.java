package com.bitdagger.dungeon.client.events;

import com.bitdagger.dungeon.events.Event;

/**
 * Abstract generic key event for other events to inherit
 * 
 * This encapsulates common data in the other key events
 */
public abstract class KeyEvent extends Event
{
	/**
	 * Key value (eg 65 -> A)
	 */
	private int key;
	
	/**
	 * Scancode
	 * 
	 * This is system specific, but always consistent on the same system
	 * This means it is safe to save to disk for keymapping
	 */
	private int scancode;
	
	/**
	 * Key modifiers (bitmask)
	 * 
	 * Shift	= 1
	 * Ctrl		= 2
	 * Alt		= 4
	 * Meta		= 8
	 */
	private int mods;
	
	/**
	 * Construct a new KeyEvent object with default priority
	 * 
	 * @param key Key code
	 * @param scancode Scancode
	 * @param mods Key modifiers
	 */
	public KeyEvent(int key, int scancode, int mods)
	{
		super();
		
		this.key = key;
		this.scancode = scancode;
		this.mods = mods;
	}
	
	/**
	 * Construct a new KeyEvent object with specified priority
	 * 
	 * @param key Key code
	 * @param scancode Scancode
	 * @param mods Key modifiers
	 * @param priority Event priority
	 */
	public KeyEvent(int key, int scancode, int mods, int priority)
	{
		super(priority);
		
		this.key = key;
		this.scancode = scancode;
		this.mods = mods;
	}
	
	/**
	 * Key code accessor
	 * 
	 * @return Key code
	 */
	public int getKey()
	{
		return this.key;
	}
	
	/**
	 * Scan code accessor
	 * 
	 * @return Scancode
	 */
	public int getScancode()
	{
		return this.scancode;
	}
	
	/**
	 * Key modifiers accessor
	 * 
	 * @return Key modifiers
	 */
	public int getMods()
	{
		return this.mods;
	}
}
