package com.bitdagger.dungeon.client;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.client.events.KeyReleaseEvent;
import com.bitdagger.dungeon.client.events.KeyRepeatEvent;
import com.bitdagger.dungeon.events.EventManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Display manager
 * 
 * Handles all the specifics of managing the window and drawing
 */
public final class Display
{   
    /**
     * Singleton instance
     */
    private static Display instance;
    
    /**
	 * Handle for the window we create
	 */
    private long windowHandle;
    
    /**
     * Singleton accessor
     * 
     * @return Singleton instance
     */
    public static Display instance()
    {
    	if (Display.instance == null) {
    		Display.instance = new Display();
    		Display.instance.init();
    	}
    	
    	return Display.instance;
    }
    
    /**
     * Initialize the display manager
     */
    private void init()
    {
    	// Set up the error callback to print to System.err
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
 
        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        final int WIDTH = 1600;
        final int HEIGHT = 900;
        
        // Create the window
        // -- long monitorHandle = glfwGetPrimaryMonitor(); // For fullscreen
        this.windowHandle = glfwCreateWindow(
    		WIDTH, 			// Width
    		HEIGHT,			// Height
    		"Hello World!",	// Title 
    		NULL, 			// Monitor handle for fullscreen
    		NULL			// Monitor handle for resource sharing
        );
        if (this.windowHandle == NULL) {
        	throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // Center the window on the screen
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(this.windowHandle, 
    		(vidmode.width() - WIDTH) / 2, 
    		(vidmode.height() - HEIGHT) / 2
    	);
        
        // Set up a key callback (pressed, repeat, released)
        glfwSetKeyCallback(this.windowHandle, new GLFWKeyCallback()
        {
            @Override
            public void invoke(long window, int key, int scancode, 
            				   int action, int mods) {
            	
            	// DEBUG - Temporary kill-switch on ESC
            	// Allows us to quit the application quickly
            	if (key == GLFW_KEY_ESCAPE) {
            		if (action == GLFW_RELEASE) {
            			// Only close when we release ESC
            			glfwSetWindowShouldClose(window, GLFW_TRUE);
            		}
            		
            		// Don't send key events for ESC
            		return;
            	}
            	
            	// Send the key event to the event manager for handling
            	EventManager em = EventManager.instance();
            	if (action == GLFW_PRESS) {
            		em.raise(new KeyPressEvent(key, scancode, mods));
            	} else if (action == GLFW_RELEASE) {
            		em.raise(new KeyReleaseEvent(key, scancode, mods));
            	} else if (action == GLFW_REPEAT) {
            		em.raise(new KeyRepeatEvent(key, scancode, mods));
            	} else {
            		// Unknown action, ignore for now
            	}
            }
        });
        
        // Finalize the window
        glfwMakeContextCurrent(this.windowHandle);	// OpenGL context
        glfwSwapInterval(1);						// Enable V-Sync
        glfwShowWindow(this.windowHandle);			// Show the window
        
        // Make the OpenGL bindings available for use
        GL.createCapabilities();
        
        // Set the default clear color
        // -------- * NOTE * --------
        // This is set to red for clarity in debugging
        // It should be set to black or white eventually
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    }
    
    /**
     * Render the frame
     */
    public void render()
    {
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    glfwSwapBuffers(this.windowHandle);
	    glfwPollEvents();
    }
    
    /**
     * Destroy the current instance
     */
    public void destroy()
    {
    	glfwDestroyWindow(this.windowHandle);
    	glfwTerminate();
    	
    	Display.instance = null;
    }
    
    /**
     * Should the window close
     * 
     * @return True if the window should close, false otherwise
     */
    public boolean shouldClose()
    {
    	return glfwWindowShouldClose(this.windowHandle) == GLFW_TRUE;
    }
}
