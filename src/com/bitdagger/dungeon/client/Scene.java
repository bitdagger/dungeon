package com.bitdagger.dungeon.client;

import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;
import com.bitdagger.dungeon.logging.Logger;

/**
 * Scene
 */
public abstract class Scene implements EventHandler
{
	/**
	 * Current state of the scene
	 */
	protected State state;
	
	/**
	 * Construct a new Scene object
	 */
	public Scene()
	{
		this.state = State.NEW;
	}
	
	/**
	 * Get the current state of the scene
	 * 
	 * @return Current state
	 */
	public State getState()
	{
		return this.state;
	}
	
	/**
	 * Initialize the scene
	 */
	public void init()
	{
		Logger.instance().debug("Initialized scene " + this.getClass().getSimpleName());
		this.state = State.RUNNING;
	}
	
	/**
	 * Prepare to be deleted
	 */
	public void cleanup()
	{
		Logger.instance().debug("Tearing down scene " + this.getClass().getSimpleName());
		EventManager.instance().unregister(this);
		this.state = State.DEAD;
	}
	
	/**
	 * Do things
	 * 
	 * @param delta Delta value
	 */
	public void update(float delta)
	{
		if (!this.state.equals(State.RUNNING)) {
			throw new RuntimeException(
				"Tried to update a scene that was not running!"
			);
		}
	}
	
	/**
	 * Pause the scene
	 */
	public void pause()
	{
		Logger.instance().debug("Paused scene " + this.getClass().getSimpleName());
		this.state = State.PAUSED;
	}
	
	/**
	 * Resume the scene
	 */
	public void resume()
	{
		Logger.instance().debug("Resumed scene " + this.getClass().getSimpleName());
		this.state = State.RUNNING;
	}
	
	/**
	 * Render the scene
	 * 
	 * @param alpha Alpha value
	 */
	public void draw(float alpha)
	{
		// Default implementation is to do nothing
	}
	
	/**
	 * State enumeration
	 */
	public enum State
	{
		NEW, RUNNING, PAUSED, DEAD
	}
}
