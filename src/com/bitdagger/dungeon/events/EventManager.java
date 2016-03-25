package com.bitdagger.dungeon.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Event manager
 *
 * Manages events via priority queue and passes them off to their handlers
 */
public final class EventManager
{
	/**
	 * Maximum number of times to iterate through the active queue per frame
	 */
	private static final int DEPTH_LIMIT = 10;
	
	/**
	 * Singleton instance
	 */
	private static EventManager instance;
	
	/**
	 * Active event queue
	 */
	private final ArrayList<Event> eventqueue;
	
	/**
	 * Hashmap binding event types to a list of their handlers
	 */
	private final HashMap<Class<?>, ArrayList<EventHandler>> registry;
	
	/**
	 * Construct a new EventManager object
	 */
	private EventManager()
	{
		this.eventqueue = new ArrayList<Event>();
		this.registry = new HashMap<Class<?>, ArrayList<EventHandler>>();
	}
	
	/**
	 * Singleton accessor
	 * 
	 * @return Singleton instance
	 */
	public static EventManager instance()
	{
		if (EventManager.instance == null) {
			EventManager.instance = new EventManager();
		}
		
		return EventManager.instance;
	}
	
	/**
	 * Register a new event type with a handler
	 * 
	 * @param handler Object that will handle events of the given type
	 * @param type Event type to handle
	 */
	public void register(EventHandler handler, Class<?> type)
	{
		if (!Event.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException("Event type is not a valid event");
		}
		
		if (!this.registry.containsKey(type)) {
			this.registry.put(type, new ArrayList<EventHandler>());
		}
		
		ArrayList<EventHandler> handlers = this.registry.get(type);
		if (handlers.contains(handler)) {
			return; // Already registered
		}
		
		handlers.add(handler);
	}
	
	/**
	 * Unregister an event type from a handler
	 * 
	 * @param handler Handler the event type is registered to 
	 * @param type Event type to unregister
	 */
	public void unregister(EventHandler handler, Class<?> type)
	{
		if (!this.registry.containsKey(type)) {
			return; // Not registered
		}
		
		ArrayList<EventHandler> handlers = this.registry.get(type);
		if (!handlers.contains(handler)) {
			return; // Not registered
		}
		
		handlers.remove(handler);
		if (handlers.isEmpty()) {
			this.registry.remove(type);
		}
	}
	
	/**
	 * Unregister all events registered to a given handler
	 * 
	 * @param handler Handler to purge
	 */
	public void unregister(EventHandler handler)
	{
		for (Class<?> type : this.registry.keySet()) {
			ArrayList<EventHandler> handlers = this.registry.get(type);
			if (handlers.contains(handler)) {
				handlers.remove(handler);
			}
			
			if (handlers.isEmpty()) {
				this.registry.remove(type);
			}
		}
	}
	
	/**
	 * Add an event to the active queue
	 * 
	 * @param event Event to raise
	 */
	public void raise(Event event)
	{	
		this.eventqueue.add(event);
	}
	
	/**
	 * Handle events in the active queue
	 */
	public void process()
	{
		// Do nothing if there are no events 
		if (this.eventqueue.isEmpty()) {
			return;
		}
		
		// Priority queue to order events properly
		PriorityQueue<Event> queue = new PriorityQueue<Event>(10, 
			new Comparator<Event>() {
			public int compare(Event a, Event b)
			{
				int priority = ((Integer) a.getPriority()).compareTo(
					b.getPriority()
				);
				if (priority != 0) {
					return priority;
				}
				
				return ((Long) a.getTimestamp()).compareTo(b.getTimestamp());
			}
		});
		
		// Copy of our registry so we don't have to worry about concurrent 
		// modification from other threads
		HashMap<Class<?>, ArrayList<EventHandler>> regcopy = new HashMap<Class<?>, ArrayList<EventHandler>>();
		
		int depth = 0;
		do {
			// Prevent infinite loops, but allow chaining in the same frame
			// Up to a certain depth 
			if (depth++ > DEPTH_LIMIT) {
				break;
			}
			
			// Clear the copies and load them up
			queue.clear();
			queue.addAll(this.eventqueue);
			this.eventqueue.clear();
			
			regcopy.clear();
			regcopy.putAll(this.registry);
			
			// Process events
			while(queue.size() > 0) {
				this.handleEvent(regcopy, queue.poll());
			}
		} while (!this.eventqueue.isEmpty()); // Only not empty if chaining
	}
	
	/**
	 * Process a single event
	 * 
	 * @param event Event to process
	 */
	private void handleEvent(HashMap<Class<?>, ArrayList<EventHandler>> registry, Event event)
	{		
		for (Class<?> type : registry.keySet()) {
			// Skip events that aren't relevant
			if (!type.isInstance(event)) {
				continue;
			}
			for (EventHandler handler : registry.get(type)) {
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
	
}
