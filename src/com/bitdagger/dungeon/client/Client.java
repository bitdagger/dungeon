package com.bitdagger.dungeon.client;

import com.bitdagger.dungeon.Dungeon;
import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;
import com.bitdagger.dungeon.logging.Logger;

/**
 * Main client class
 * 
 * Encompasses everything the client needs to run successfully.
 */
public final class Client extends Dungeon implements EventHandler
{
	/**
	 * Reference to the display manager
	 */
	private Display display;

	/**
	 * Reference to the event manager
	 */
	private EventManager em;

	/**
	 * Construct a new Client object
	 * 
	 * Instanciate the display and event managers and set operating defaults.
	 */
	public Client() {
		super();
		
		Logger.instance().debug("Launching Client...");
		this.em = EventManager.instance();
		this.display = Display.instance();
		if (this.display == null) {
			System.exit(1);
		}
	}

	/**
	 * Run the game
	 * 
	 * This acts as our main game loop, though the actual logic ends up being
	 * split into other areas such as the event manager and display manager.
	 */
	public void run() {
		Logger.instance().debug("Starting client loop...");
		while (!this.display.shouldClose()) {
			this.em.process();

			this.display.render();
		}

		this.display.destroy();
		Logger.instance().debug("Completing client loop...");
	}
}
