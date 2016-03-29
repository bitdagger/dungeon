package com.bitdagger.dungeon.client;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer
{
	private static Timer instance;
	
	private double lastLoopTime;
	
	private float timeCount;
	
	private int fps;
	
	private int fpsCount;
	
	private int ups;
	
	private int upsCount;
	
	public static Timer instance()
	{
		if (Timer.instance == null) {
			Timer.instance = new Timer();
			Timer.instance.init();
		}
		
		return Timer.instance;
	}
	
	public void init()
	{
		this.lastLoopTime = this.getTime();
	}
	
	public double getTime()
	{
		return glfwGetTime();
	}
	
	public float getDelta()
	{
		double time = getTime();
		float delta = (float) (time - this.lastLoopTime);
		this.lastLoopTime = time;
		this.timeCount += delta;
		
		return delta;
    }
	
	public void updateFPS()
	{
		this.fpsCount++;
	}
	
	public void updateUPS()
	{
		this.upsCount++;
	}
	
	public void update()
	{
		if (this.timeCount > 1f) {
			this.fps = this.fpsCount;
			this.fpsCount = 0;
			
			this.ups = this.upsCount;
			this.upsCount = 0;
			
			this.timeCount -= 1f;
		}
	}
	
	public int getFPS()
	{
		return this.fps > 0 ? this.fps : this.fpsCount;
	}
	
	public int getUPS()
	{
		return this.ups > 0 ? this.ups : this.upsCount;
	}
	
	public double getLastLoopTime()
	{
		return this.lastLoopTime;
	}
}
