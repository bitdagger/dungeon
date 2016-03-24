package com.bitdagger.dungeon.client;

import java.util.Stack;

public final class SceneManager
{
	protected static SceneManager instance;
	
	protected Stack<Scene> scenes;
	
	protected Scene activeScene;
	
	private SceneManager()
	{
		this.scenes = new Stack<Scene>();
		this.activeScene = null;
	}
	
	public static SceneManager instance()
	{
		if (SceneManager.instance == null) {
			SceneManager.instance = new SceneManager();
		}
		
		return SceneManager.instance;
	}
	
	public void changeScene(Scene scene)
	{
		this.popScene();
		this.pushScene(scene);
	}
	
	public void pushScene(Scene scene)
	{
		if (this.activeScene != null) {
			this.activeScene.pause();
		}
		this.scenes.push(scene);
		this.activeScene = scene;
		this.activeScene.init();
	}
	
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
	
	public void update()
	{
		this.activeScene.update();
	}
	
	public Scene getActiveScene()
	{
		return this.activeScene;
	}
}
