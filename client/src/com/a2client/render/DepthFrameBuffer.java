package com.a2client.render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.gl;

/**
 * Created by arksu on 27.07.16.
 */
public class DepthFrameBuffer extends FrameBuffer
{
	private static final Logger _log = LoggerFactory.getLogger(DepthFrameBuffer.class.getName());

	private int _depthTexture;

	public DepthFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth)
	{
		super(format, width, height, hasDepth);
	}

	public DepthFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil)
	{
		super(format, width, height, hasDepth, hasStencil);
	}

	public void createDepthTextre()
	{
//		Texture

		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, getFramebufferHandle());


		_depthTexture = gl.glGenTexture();
		gl.glBindTexture(GL11.GL_TEXTURE_2D, _depthTexture);
		gl.glTexImage2D(
				GL11.GL_TEXTURE_2D, 0,
				GL14.GL_DEPTH_COMPONENT16, width, height,
				0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null);
		gl.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		gl.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		gl.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
									  GL20.GL_TEXTURE_2D,
									  _depthTexture, 0);

		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}

	public int getDepthTexture()
	{
		return _depthTexture;
	}
}