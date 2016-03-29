package com.bitdagger.dungeon.client;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.client.events.KeyReleaseEvent;
import com.bitdagger.dungeon.client.events.KeyRepeatEvent;
import com.bitdagger.dungeon.client.math.Matrix4f;
import com.bitdagger.dungeon.events.EventManager;
import com.bitdagger.dungeon.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

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
     * Debugging output for key events
     */
    private final boolean keydebug = false;
    
    /**
     * Error callback for GLFW
     */
    private GLFWErrorCallback errCB;
    
    /**
     * Key callback for GLFW
     */
    private GLFWKeyCallback  keyCB;
    
    /**
     * Handle for the vertex shader
     */
    private int vertexShader;
    
    /**
     * Handle for the fragment shader
     */
    private int fragmentShader;
    
    /**
     * Handle for the shader program
     */
    private int shaderProgram;
    
    /**
     * Singleton accessor
     * 
     * @return Singleton instance
     */
    public static Display instance()
    {
    	if (Display.instance == null) {
    		Display.instance = new Display();
    		try {
    			Display.instance.init();
    		} catch (Exception e) {
    			Logger.instance().fatal(e.getMessage());
    			Display.instance = null;
    		}
    	}
    	
    	return Display.instance;
    }
    
    /**
     * Initialize the display manager
     */
    private void init()
    {
    	// Set up the error callback to print to System.err
        glfwSetErrorCallback(this.errCB = GLFWErrorCallback.createPrint(Logger.instance().getErrorStream()));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
 
        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
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
        final Display self = this;
        glfwSetKeyCallback(this.windowHandle, this.keyCB = new GLFWKeyCallback()
        {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
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
            		if (self.keydebug)
            			Logger.instance().debug("Key pressed [" + key + ", " + scancode + ", " + mods + "]");
            		em.raise(new KeyPressEvent(key, scancode, mods));
            	} else if (action == GLFW_RELEASE) {
            		if (self.keydebug)
            			Logger.instance().debug("Key released [" + key + ", " + scancode + ", " + mods + "]");
            		em.raise(new KeyReleaseEvent(key, scancode, mods));
            	} else if (action == GLFW_REPEAT) {
            		if (self.keydebug)
            			Logger.instance().debug("Key repeated [" + key + ", " + scancode + ", " + mods + "]");
            		em.raise(new KeyRepeatEvent(key, scancode, mods));
            	} else {
            		Logger.instance().notice("Unknown key action: " + action);
            	}
            }
        });
        
        // Finalize the window
        glfwMakeContextCurrent(this.windowHandle);	// OpenGL context
        glfwSwapInterval(1);						// Enable V-Sync
        
        // Make the OpenGL bindings available for use
        GL.createCapabilities();
        
        // Set the default clear color
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        
        // Create vertex shader
		try {
			String vertexSource = String.join("\n", 
				Files.readAllLines(Paths.get("resources/shaders/default.vert"))
			);
			this.vertexShader = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(this.vertexShader, vertexSource);
			glCompileShader(this.vertexShader);
			if (glGetShaderi(this.vertexShader, GL_COMPILE_STATUS) != GL_TRUE) {
			    throw new RuntimeException(glGetShaderInfoLog(this.vertexShader));
			}
		} catch (IOException|RuntimeException e) {
			Logger.instance().fatal("Failed to create vertex shader: " + e.getMessage());
			System.exit(1);
		}
		
		// Create fragment shader
		try {
			String vertexSource = String.join("\n", 
				Files.readAllLines(Paths.get("resources/shaders/default.frag"))
			);
			this.fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(this.fragmentShader, vertexSource);
			glCompileShader(this.fragmentShader);
			if (glGetShaderi(this.fragmentShader, GL_COMPILE_STATUS) != GL_TRUE) {
			    throw new RuntimeException(glGetShaderInfoLog(this.fragmentShader));
			}
		} catch (IOException|RuntimeException e) {
			Logger.instance().fatal("Failed to create fragment shader: " + e.getMessage());
			System.exit(1);
		}
		
		// Create the shader program and attach the shader
		this.shaderProgram = glCreateProgram();
		glAttachShader(this.shaderProgram, this.vertexShader);
		glAttachShader(this.shaderProgram, this.fragmentShader);
		GL30.glBindFragDataLocation(this.shaderProgram, 0, "fragColor");
		glLinkProgram(this.shaderProgram);
		if (glGetProgrami(this.shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
			Logger.instance().fatal("Failed to link shaders: " + glGetProgramInfoLog(this.shaderProgram));
			System.exit(1);
		}
		glUseProgram(this.shaderProgram);
    }
    
    /**
     * Render the frame
     * 
     * @param activescene The active scene we should render
     * @param alpha Interpolation value
     */
    public void render(Scene activescene, float alpha)
    {
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	    if (activescene != null) {	    	
	    	activescene.draw(alpha);
	    }
	    glfwSwapBuffers(this.windowHandle);
	    glfwPollEvents();
    }
    
    /**
     * Destroy the current instance
     */
    public void destroy()
    {
    	Logger.instance().debug("Tearing down display...");
    	glfwDestroyWindow(this.windowHandle);
    	this.keyCB.free();
    	glfwTerminate();
    	this.errCB.free();
    	
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
    
    /**
     * Show the window
     */
    public void showWindow()
    {
    	glfwShowWindow(this.windowHandle);
    }
    
    /**
     * Hide the window
     */
    public void hideWindow()
    {
    	glfwHideWindow(this.windowHandle);
    }
    
    /**
     * Shader program accessor
     * 
     * @return
     */
    public int getShaderProgram()
    {
    	return this.shaderProgram;
    }
}
