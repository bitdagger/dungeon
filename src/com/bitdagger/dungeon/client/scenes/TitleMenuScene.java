package com.bitdagger.dungeon.client.scenes;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import com.bitdagger.dungeon.client.Scene;
import com.bitdagger.dungeon.client.SceneManager;
import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;

/**
 * Title menu scene
 * 
 * Main menu for the game, gives options like New Game, Load Game, Quit, etc
 */
public class TitleMenuScene extends Scene implements EventHandler
{
	/**
	 * Construct a new TitleMenuScene object
	 */
	public TitleMenuScene()
	{
		super();
		
		EventManager.instance().register(this, KeyPressEvent.class);
	}
	
	// DEBUG - load a new scene
	public void handleEvent(KeyPressEvent event)
	{
		SceneManager.instance().pushScene(new TestScene());
	}
	
	
	/**
	 * Render the scene
	 */
	public void draw()
	{
		glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}
