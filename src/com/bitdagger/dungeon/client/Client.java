package com.bitdagger.dungeon.client;

import com.bitdagger.dungeon.Dungeon;
import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.events.Event;
import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;
import com.bitdagger.dungeon.events.TestEvent;

import static org.lwjgl.glfw.GLFW.*;

public final class Client extends Dungeon implements EventHandler
{
	private Display display;
	private EventManager em;
	
	public Client()
	{
		this.em = EventManager.instance(); 
		this.em.register(this,TestEvent.class);
		this.display = Display.instance();
		
		this.em.raise(new TestEvent(1));
	}
	
	public void handleEvent(TestEvent event)
	{
		System.out.println(event.getData());
		this.em.raise(new TestEvent(event.getData() + 1));
	}
	
	public void run()
	{
		this.display.init();
		
		while (glfwWindowShouldClose(this.display.getWindowHandle()) == GLFW_FALSE) {
			this.em.handle();
			
			this.display.render();
		}
		
		this.display.destroy();
	}
}
