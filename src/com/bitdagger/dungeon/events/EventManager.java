package com.bitdagger.dungeon.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public final class EventManager
{
	private static final int ITERATION_LIMIT = 5;
	
	protected static EventManager instance;
	
	protected final ArrayList<Event> eventqueue;
	
	protected final HashMap<EventHandler, ArrayList<Class<?>>> handlers;
	
	private EventManager()
	{
		this.eventqueue = new ArrayList<Event>();
		this.handlers = new HashMap<EventHandler, ArrayList<Class<?>>>();
	}
	
	public static EventManager instance()
	{
		if (EventManager.instance == null) {
			EventManager.instance = new EventManager();
		}
		
		return EventManager.instance;
	}
	
	public void register(EventHandler handler, Class<?> type)
	{
		if (!Event.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException("Event type is not a valid event");
		}
		
		if (!this.handlers.containsKey(handler)) {
			this.handlers.put(handler, new ArrayList<Class<?>>());
		}
		
		ArrayList<Class<?>> types = this.handlers.get(handler);
		if (types.contains(type)) {
			return; // Already registered
		}
		
		types.add(type);
	}
	
	public void unregister(EventHandler handler, Class<?> type)
	{
		if (!this.handlers.containsKey(handler)) {
			return; // Not registered
		}
		
		ArrayList<Class<?>> types = this.handlers.get(handler);
		if (!types.contains(type)) {
			return; // Not registered
		}
		
		types.remove(type);
		if (types.isEmpty()) {
			this.handlers.remove(handler);
		}
	}
	
	public void raise(Event e)
	{	
		this.eventqueue.add(e);
	}
	
	public void handle()
	{
		if (this.eventqueue.isEmpty()) {
			return;
		}
		
		PriorityQueue<Event> queue = new PriorityQueue<Event>(10, new Comparator<Event>() {
			public int compare(Event a, Event b)
			{
				int priority = ((Integer) a.getPriority()).compareTo(b.getPriority());
				if (priority != 0) {
					return priority;
				}
				
				return ((Long) a.getTimestamp()).compareTo(b.getTimestamp());
			}
		});
		
		HashMap<EventHandler, ArrayList<Class<?>>> handlers = new HashMap<EventHandler, ArrayList<Class<?>>>();
		
		int iterations = 0;
		do {
			if (iterations++ > ITERATION_LIMIT) {
				break;
			}
			
			queue.clear();
			queue.addAll(this.eventqueue);
			this.eventqueue.clear();
			
			handlers.clear();
			handlers.putAll(this.handlers);
			
			
			while(queue.size() > 0) {
				Event event = queue.poll();
				for (EventHandler handler : handlers.keySet()) {
					for (Class<?> type : handlers.get(handler)) {
						if (!type.isInstance(event)) {
							continue;
						}
						
						try {
							Method handleMethod = handler.getClass().getMethod("handleEvent", type);
							handleMethod.invoke(handler, event);
						} catch (NoSuchMethodException e) {
							System.out.println("Tried to pass " + type.getName() + " to " + handler.getClass().getName() + ", but the handle method did not exist");
							continue;
						} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} while (!this.eventqueue.isEmpty());
	}
}
