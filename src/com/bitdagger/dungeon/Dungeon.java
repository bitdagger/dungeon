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
	 * Application entry point
	 * 
	 * @param args Program arguments
	 */
	public static void main(String[] args)
	{
		Logger.instance().enableDebugging();
		Logger.instance().debug("Launching Client...");
		(new Client()).run();
	}
}
