package com.bitdagger.dungeon.client;

import java.util.Stack;

/**
 * Scene manager
 * 
 * Responsible for managing the various active scenes
 */
public final class SceneManager
{
	/**
	 * Singleton instance
	 */
	protected static SceneManager instance;
	
	/**
	 * Stack of scenes
	 */
	protected Stack<Scene> scenes;
	
	/**
	 * Quick reference to the active scene 
	 * This is so we don't have to peek at the stack all the time
	 */
	protected Scene activeScene;
	
	/**
	 * Construct a new SceneManager object
	 */
	private SceneManager()
	{
		this.scenes = new Stack<Scene>();
		this.activeScene = null;
	}
	
	/**
	 * Singleton accessor
	 * 
	 * @return Singleton instance
	 */
	public static SceneManager instance()
	{
		if (SceneManager.instance == null) {
			SceneManager.instance = new SceneManager();
		}
		
		return SceneManager.instance;
	}
	
	/**
	 * Remove the current scene and switch to a new one
	 * 
	 * @param scene New active scene
	 */
	public void changeScene(Scene scene)
	{
		if (this.activeScene != null) {
			this.activeScene.cleanup();
			this.scenes.pop();
		}
		
		this.scenes.push(scene);
		this.activeScene = scene;
		this.activeScene.init();
	}
	
	/**
	 * Pause the active scene if it exists and add a new scene
	 * 
	 * @param scene New active scene
	 */
	public void pushScene(Scene scene)
	{
		if (this.activeScene != null) {
			this.activeScene.pause();
		}
		
		this.scenes.push(scene);
		this.activeScene = scene;
		this.activeScene.init();
	}
	
	/**
	 * Remove the active scene and resume the previous scene if it exists
	 */
	public void popScene()
	{
		if (this.activeScene != null) {
			this.activeScene.cleanup();
			this.scenes.pop();
			this.activeScene = null;
		}
		
		if (!this.scenes.empty()) {
			this.activeScene = this.scenes.peek();
			this.activeScene.resume();
		}
	}
	
	/**
	 * Update the active scene
	 */
	public void update()
	{
		if (this.activeScene != null) {
			this.activeScene.update();
		}
	}
	
	/**
	 * Accessor for the active scene
	 * 
	 * @return Active scene
	 */
	public Scene getActiveScene()
	{
		return this.activeScene;
	}
	
	/**
	 * Pop all scenes and cleanup
	 */
	public void destroy()
	{
		while(!this.scenes.isEmpty()) {
			this.scenes.pop().cleanup();
		}
	}
}
