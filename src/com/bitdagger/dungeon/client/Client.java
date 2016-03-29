package com.bitdagger.dungeon.client;

import com.bitdagger.dungeon.Dungeon;
import com.bitdagger.dungeon.client.scenes.TitleMenuScene;
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
	 * Reference to the scene manager
	 */
	private SceneManager sm;
	
	/**
	 * Target frames per second
	 */
	private static final int TARGET_FPS = 60;
	
	/**
	 * Target updates per second
	 */
	private static final int TARGET_UPS = 5;
	
	/**
	 * Timer
	 */
	private Timer timer;

	/**
	 * Construct a new Client object
	 * 
	 * Instanciate the display and event managers and set operating defaults.
	 */
	public Client() {
		super();
		
		Logger.instance().debug("Launching Client...");
		this.em = EventManager.instance();
		this.sm = SceneManager.instance();
		this.timer = Timer.instance();
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
		this.sm.pushScene(new TitleMenuScene());
		this.display.showWindow();
		
		float delta;
		float alpha;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;
		
		Logger.instance().debug("Starting client loop...");
		while (!this.display.shouldClose()) {
			delta = timer.getDelta();
			accumulator += delta;
			
			while(accumulator >= interval) {
				this.update(interval);
				this.timer.updateUPS();
				accumulator -= interval;
			}
			
			alpha = accumulator / interval;
			this.display.render(this.sm.getActiveScene(), alpha);
			this.timer.updateFPS();
		}

		this.sm.destroy();
		this.display.destroy();
		Logger.instance().debug("Completing client loop...");
	}
	
	/**
	 * Update the game state
	 * 
	 * @param delta Delta value
	 */
	private void update(float delta)
	{
		this.em.process();
		this.sm.getActiveScene().update(delta);
	}
}
