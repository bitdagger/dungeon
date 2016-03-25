package com.bitdagger.dungeon.logging;

/**
 * Logging level enumeration
 */
public enum LogLevel
{
	/**
	 * Enumeration definition
	 */
	DEBUG("DEBUG"), 
	INFO("INFO"), 
	NOTICE("NOTICE"), 
	WARN("WARN"), 
	ERROR("ERROR"), 
	FATAL("FATAL");
	
	/**
	 * String value
	 */
	private String s;
	
	/**
	 * Construct a new LogLevel enumeration
	 * 
	 * @param s String value
	 */
	private LogLevel(String s)
	{
		this.s = s;
	}
	
	/**
	 * String value accessor
	 * 
	 * @return
	 */
	public String getValue()
	{
		return this.s;
	}
}
