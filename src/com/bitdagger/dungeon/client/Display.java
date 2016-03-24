package com.bitdagger.dungeon.client;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.client.events.KeyReleaseEvent;
import com.bitdagger.dungeon.client.events.KeyRepeatEvent;
import com.bitdagger.dungeon.events.EventManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class Display
{
    private GLFWErrorCallback errorCallback;
    
    private long windowHandle;
    
    private static Display instance;
    
    private boolean initialized;
    private boolean destroyed;
    
    private Display()
    {
    	this.initialized = false;
    	this.destroyed = false;
    }
    
    public static Display instance()
    {
    	if (Display.instance == null) {
    		Display.instance = new Display();
    	}
    	
    	return Display.instance;
    }
    
    public void init()
    {
    	if (this.initialized) {
    		return;
    	}
    	
    	// Set up the error callback to print to System.err
        glfwSetErrorCallback(this.errorCallback = GLFWErrorCallback.createPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
 
        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        
        // Create the window
        long monitorHandle = glfwGetPrimaryMonitor();
        this.windowHandle = glfwCreateWindow(1600, 900, "Hello World!", NULL, NULL);
        if (this.windowHandle == NULL) {
        	throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // Set up a key callback (pressed, repeat, released)
        glfwSetKeyCallback(this.windowHandle, new GLFWKeyCallback()
        {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods)
            {
            	// Temporary kill-switch on ESC
            	if (key == GLFW_KEY_ESCAPE) {
            		if (action == GLFW_RELEASE) {
            			glfwSetWindowShouldClose(window, GLFW_TRUE);
            		}
            		
            		return;
            	}
            	
            	EventManager em = EventManager.instance();
            	if (action == GLFW_PRESS) {
            		em.raise(new KeyPressEvent(key, scancode, mods));
            	} else if (action == GLFW_RELEASE) {
            		em.raise(new KeyReleaseEvent(key, scancode, mods));
            	} else if (action == GLFW_REPEAT) {
            		em.raise(new KeyRepeatEvent(key, scancode, mods));
            	} else {
            		// Unknown
            	}
                
            	//EventManager.instance().raise();
            }
        });
        
        // Finalize the window
        glfwMakeContextCurrent(this.windowHandle);	// Make the OpenGL context current
        glfwSwapInterval(1);						// Enable V-Sync
        glfwShowWindow(this.windowHandle);			// Show the window
        
        // Make the OpenGL bindings available for use
        GL.createCapabilities();
        
        // Set the default clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        
        this.initialized = true;
        this.destroyed = false;
    }
    
    public void render()
    {
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    glfwSwapBuffers(this.windowHandle);
	    glfwPollEvents();
    }
    
    public void destroy()
    {
    	if (this.destroyed) {
    		return;
    	}
    	
    	glfwDestroyWindow(this.windowHandle);
    	glfwTerminate();
        this.errorCallback.free();
        
        this.initialized = false;
        this.destroyed = true;
    }
    
    public long getWindowHandle()
    {
    	return this.windowHandle;
    }
}
