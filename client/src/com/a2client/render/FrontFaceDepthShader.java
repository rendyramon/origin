package com.a2client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

/**
 * Created by arksu on 24.09.15.
 */
public class FrontFaceDepthShader extends DefaultShader
{
	public static class Config extends DefaultShader.Config
	{
		public boolean depthBufferOnly = false;
		public float defaultAlphaTest = 0.5f;

		public Config()
		{
			super();
//			defaultCullFace = GL20.GL_FRONT;
//			defaultCullFace = GL20.GL_BACK;
		}

		public Config(String vertexShader, String fragmentShader)
		{
			super(vertexShader, fragmentShader);
		}
	}

	private static String defaultVertexShader = null;

	public final static String getDefaultVertexShader()
	{
		if (defaultVertexShader == null)
		{
			defaultVertexShader = Gdx.files.internal("assets/depth_vertex.glsl").readString();
		}
		return defaultVertexShader;
	}

	private static String defaultFragmentShader = null;

	public final static String getDefaultFragmentShader()
	{
		if (defaultFragmentShader == null)
		{
			defaultFragmentShader = Gdx.files.internal("assets/depth_frag.glsl").readString();
		}
		return defaultFragmentShader;
	}

	public static String createPrefix(final Renderable renderable, final Config config)
	{
		String prefix = DefaultShader.createPrefix(renderable, config);
		if (!config.depthBufferOnly) prefix += "#define PackedDepthFlag\n";
		return prefix;
	}

	public FrontFaceDepthShader(final Renderable renderable, final Config config)
	{
		this(renderable, config, createPrefix(renderable, config));
	}

	public FrontFaceDepthShader(final Renderable renderable, final Config config, final String prefix)
	{
		super(renderable, config, prefix, config.vertexShader != null ? config.vertexShader : getDefaultVertexShader(),
			  config.fragmentShader != null ? config.fragmentShader : getDefaultFragmentShader());
	}
}
