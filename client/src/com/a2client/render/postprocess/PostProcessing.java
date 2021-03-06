package com.a2client.render.postprocess;

import com.a2client.render.Render;
import com.a2client.render.framebuffer.DepthFrameBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;

/**
 * пост процессинг. эффекты накладываем на финальный кадр
 * Created by arksu on 31.07.16.
 */
public class PostProcessing
{
	private static final Logger _log = LoggerFactory.getLogger(PostProcessing.class.getName());

	/**
	 * квад для вывода на экран
	 */
	private final Mesh _fullScreenQuad;

	/**
	 * цепочка эффектов
	 */
	private final List<Effect> _effects = new ArrayList<>();

	public PostProcessing()
	{
		_fullScreenQuad = Render.createFullScreenQuad();
	}

	public void doPostProcessing(DepthFrameBuffer initialFBO)
	{
		// текущий буфер который обрабатываем
		DepthFrameBuffer frameBuffer = initialFBO;

		// пройдем по всем эффектам
		for (Effect effect : _effects)
		{
			// включим буфер эффекта если надо
			if (!effect.isFinal())
			{
				effect.getFrameBuffer().begin();
			}

			// включим шейдер
			effect.getShaderProgram().begin();

			effect.bindTextures(frameBuffer);

			// укажем размер экрана в шейдере
			effect.getShaderProgram().setUniformf("u_size", new Vector2(frameBuffer.getWidth(), frameBuffer.getHeight()));

			// выведем квад
			_fullScreenQuad.render(effect.getShaderProgram(), GL20.GL_TRIANGLE_STRIP);

			// выключим шейдер
			effect.getShaderProgram().end();

			gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);

			if (!effect.isFinal())
			{
				effect.getFrameBuffer().end();
				frameBuffer = effect.getFrameBuffer();
			}
			else
			{
				// последний эффект прерывает цикл
				break;
			}
		}
		Gdx.gl.glActiveTexture(GL13.GL_TEXTURE0);

	}

	/**
	 * добавить эффект в цепочку
	 */
	public void addEffect(Effect effect)
	{
		_effects.add(effect);
	}

}
