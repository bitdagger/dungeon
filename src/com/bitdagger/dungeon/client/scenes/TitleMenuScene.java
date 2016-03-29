package com.bitdagger.dungeon.client.scenes;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.bitdagger.dungeon.client.Display;
import com.bitdagger.dungeon.client.Scene;
import com.bitdagger.dungeon.client.SceneManager;
import com.bitdagger.dungeon.client.events.KeyPressEvent;
import com.bitdagger.dungeon.client.math.Matrix4f;
import com.bitdagger.dungeon.events.EventHandler;
import com.bitdagger.dungeon.events.EventManager;

/**
 * Title menu scene
 * 
 * Main menu for the game, gives options like New Game, Load Game, Quit, etc
 */
public class TitleMenuScene extends Scene implements EventHandler
{
	private float previousAngle = 0f;
	private float angle = 0f;
	private float anglePerSecond = 50f;
	
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
	
	public void update(float delta)
	{
		super.update(delta);
		
		previousAngle = angle;
		angle += delta * anglePerSecond;
	}
	
	
	/**
	 * Render the scene
	 * 
	 * @param alpha Alpha value
	 */
	public void draw(float alpha)
	{
		glClear(GL_COLOR_BUFFER_BIT);
		
		//
		
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3 * 6);
		vertices.put(-0.6f).put(-0.4f).put(0f).put(1f).put(0f).put(0f);
		vertices.put(0.6f).put(-0.4f).put(0f).put(0f).put(1f).put(0f);
		vertices.put(0f).put(0.6f).put(0f).put(0f).put(0f).put(1f);
		vertices.flip();
		
		int vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		//
		
		int floatSize = 4;
		int shaderProgram = Display.instance().getShaderProgram();

		int posAttrib = glGetAttribLocation(shaderProgram, "position");
		glEnableVertexAttribArray(posAttrib);
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 6 * floatSize, 0);

		int colAttrib = glGetAttribLocation(shaderProgram, "color");
		glEnableVertexAttribArray(colAttrib);
		glVertexAttribPointer(colAttrib, 3, GL_FLOAT, false, 6 * floatSize, 3 * floatSize);

		//
		
		int uniModel = glGetUniformLocation(shaderProgram, "model");
		float lerpAngle = (1f - alpha) * previousAngle + alpha * angle;
		Matrix4f model = Matrix4f.rotate(lerpAngle, 0f, 0f, 1f);
		glUniformMatrix4fv(uniModel, false, model.getBuffer());

		int uniView = glGetUniformLocation(shaderProgram, "view");
		Matrix4f view = new Matrix4f();
		glUniformMatrix4fv(uniView, false, view.getBuffer());

		int uniProjection = glGetUniformLocation(shaderProgram, "projection");
		float ratio = 640f / 480f;
		Matrix4f projection = Matrix4f.orthographic(-ratio, ratio, -1f, 1f, -1f, 1f);
		glUniformMatrix4fv(uniProjection, false, projection.getBuffer());
		
		
		
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
	}
}
