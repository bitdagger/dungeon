package com.bitdagger.dungeon.client;

import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;

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
		this.state = State.RUNNING;
	}
	
	/**
	 * Prepare to be deleted
	 */
	public void cleanup()
	{
		EventManager.instance().unregister(this);
		this.state = State.DEAD;
	}
	
	/**
	 * Do things
	 */
	public void update()
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
		this.state = State.PAUSED;
	}
	
	/**
	 * Resume the scene
	 */
	public void resume()
	{
		this.state = State.RUNNING;
	}
	
	/**
	 * Render the scene
	 */
	public void draw()
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
