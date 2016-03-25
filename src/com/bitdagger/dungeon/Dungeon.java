package com.bitdagger.dungeon;

import com.bitdagger.dungeon.client.Client;
import com.bitdagger.dungeon.logging.Logger;

/**
 * Main launcher class
 * 
 * This is our entry point to the application. By default it will launch a new 
 * instance of a client.
 */
public abstract class Dungeon
{	
	/**
	 * Version number
	 */
	private static final String VERSION = "0.0.1";
	
	/**
	 * Construct a new Dungeon object
	 */
	public Dungeon()
	{
		Logger log = Logger.instance();
		log.enableDebugging();
		log.info(String.format("Dungeon version %s", VERSION));
	}
	
	/**
	 * Application entry point
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args)
	{
		(new Client()).run();
	}
}
