package com.bitdagger.dungeon.client;

import java.util.ArrayList;

import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;

public abstract class Scene implements EventHandler
{
	protected ArrayList<Class<?>> eventhandles;
	protected EventManager em;
	
	public Scene()
	{
		this.eventhandles = new ArrayList<Class<?>>();
		this.em = EventManager.instance();
	}
	
	protected void registerEvent(Class<?> handle)
	{
		this.em.register(this, handle);
		this.eventhandles.add(handle);
	}
	
	protected void unregisterEvent(Class<?> handle)
	{
		this.em.unregister(this, handle);
		this.eventhandles.remove(handle);
	}
	
	protected void unregisterAllEvents()
	{
		for (Class<?> handle : this.eventhandles) {
			this.em.unregister(this, handle);
		}
		
		this.eventhandles.clear();
	}
	
	public void init()
	{
		
	}
	
	public void cleanup()
	{
		this.unregisterAllEvents();
	}
	
	public void update()
	{
		
	}
	
	public void pause()
	{
		for (Class<?> handle : this.eventhandles) {
			this.em.unregister(this, handle);
		}
	}
	
	public void resume()
	{
		for (Class<?> handle : this.eventhandles) {
			this.em.register(this, handle);
		}
	}
	
	public void draw()
	{
		
	}
}
