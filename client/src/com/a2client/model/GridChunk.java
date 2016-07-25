package com.a2client.model;

import com.a2client.MapCache;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.a2client.model.Grid.CHUNK_SIZE;
import static com.a2client.model.Tile.TILE_ATLAS_SIZE;

/**
 * кусочек грида для оптимизации рендера карты
 * Created by arksu on 22.02.15.
 */
public class GridChunk
{
	private static final Logger _log = LoggerFactory.getLogger(GridChunk.class.getName());

	/**
	 * меш
	 */
	private Mesh _mesh;

	/**
	 * массив вершин
	 */
	private float[] _vertex;

	/**
	 * массив индексов
	 */
	private short[] _index;

	/**
	 * границы чанка для определения видимости
	 */
	private BoundingBox _boundingBox;

	/**
	 * отступ данного грида в тайлах
	 */
	int _gx, _gy;

	/**
	 * отступ чанка внутри грида
	 */
	int _cx, _cy;

	/**
	 * грид в котором создаем чанк
	 */
	private Grid _grid;

	/**
	 * этот чанк на границе мира? есть тайлы с неопределенной высотой...
	 */
	private boolean _isBorder = false;

	private final NormalHeight[][] _heights = new NormalHeight[CHUNK_SIZE + 1][CHUNK_SIZE + 1];

	public GridChunk(Grid grid, int gx, int gy)
	{
		_grid = grid;
		_vertex = new float[CHUNK_SIZE * CHUNK_SIZE * 9 * 4];
		_index = new short[CHUNK_SIZE * CHUNK_SIZE * 6];
		_mesh = new Mesh(
				true,
				_vertex.length / 3,
				_index.length,
				new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
				new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE)
		);

		_cx = gx;
		_cy = gy;
		makeMesh();
	}

	protected NormalHeight getNormalHeight(int tx, int ty)
	{
		NormalHeight normalHeight = _heights[tx - _gx - _cx][ty - _gy - _cy];
		if (normalHeight != null)
		{
			return normalHeight;
		}
		normalHeight = new NormalHeight(tx, ty, this);
		_heights[tx - _gx - _cx][ty - _gy - _cy] = normalHeight;
		return normalHeight;
	}

	protected void makeMesh()
	{
		int idx = 0;
		int idv = 0;

		// отступ данного грида в тайлах
		_gx = _grid.getTc().x;
		_gy = _grid.getTc().y;
		_boundingBox = new BoundingBox(new Vector3(_gx + _cx, -1, _gy + _cy),
									   new Vector3(_gx + _cx + CHUNK_SIZE, 3, _gy + _cy + CHUNK_SIZE));

		short vertex_count = 0;
		NormalHeight nh;
		_isBorder = false;

		for (int x = _cx; x < _cx + CHUNK_SIZE; x++)
		{
			for (int y = _cy; y < _cy + CHUNK_SIZE; y++)
			{
				int tx;
				int ty;
				Vector2 uv;

				// абсолютные координаты тайла
				tx = _gx + x;
				ty = _gy + y;

				// 0 =====
				nh = getNormalHeight(tx, ty);
				_isBorder = _isBorder || nh.isBorder;
				_vertex[idx++] = tx;
				_vertex[idx++] = nh.h;
				_vertex[idx++] = ty;

				// normal
				_vertex[idx++] = nh.normal.x;
				_vertex[idx++] = nh.normal.y;
				_vertex[idx++] = nh.normal.z;

				idx += 1; // skip color

				uv = Tile.getTileUV(_grid._tiles[y][x]);
				_vertex[idx++] = uv.x;
				_vertex[idx++] = uv.y;

				// 1 =====
				nh = getNormalHeight(tx + 1, ty);
				_isBorder = _isBorder || nh.isBorder;
				_vertex[idx++] = tx + 1;
				_vertex[idx++] = nh.h;
				_vertex[idx++] = ty;

				// normal
				_vertex[idx++] = nh.normal.x;
				_vertex[idx++] = nh.normal.y;
				_vertex[idx++] = nh.normal.z;

				idx += 1; // skip color

				_vertex[idx++] = uv.x + TILE_ATLAS_SIZE;
				_vertex[idx++] = uv.y;

				// 2 =====
				nh = getNormalHeight(tx, ty + 1);
				_isBorder = _isBorder || nh.isBorder;
				_vertex[idx++] = tx;
				_vertex[idx++] = nh.h;
				_vertex[idx++] = ty + 1;

				// normal
				_vertex[idx++] = nh.normal.x;
				_vertex[idx++] = nh.normal.y;
				_vertex[idx++] = nh.normal.z;

				idx += 1; // skip color

				_vertex[idx++] = uv.x;
				_vertex[idx++] = uv.y + TILE_ATLAS_SIZE;

				// 3 =====
				nh = getNormalHeight(tx + 1, ty + 1);
				_isBorder = _isBorder || nh.isBorder;
				_vertex[idx++] = tx + 1;
				_vertex[idx++] = nh.h;
				_vertex[idx++] = ty + 1;

				// normal
				_vertex[idx++] = nh.normal.x;
				_vertex[idx++] = nh.normal.y;
				_vertex[idx++] = nh.normal.z;

				idx += 1; // skip color

				_vertex[idx++] = uv.x + TILE_ATLAS_SIZE;
				_vertex[idx++] = uv.y + TILE_ATLAS_SIZE;

				//index
				_index[idv++] = vertex_count;
				_index[idv++] = (short) (vertex_count + 3);
				_index[idv++] = (short) (vertex_count + 1);

				_index[idv++] = vertex_count;
				_index[idv++] = (short) (vertex_count + 2);
				_index[idv++] = (short) (vertex_count + 3);

				vertex_count += 4;

			}
		}
		_mesh.setVertices(_vertex);
		_mesh.setIndices(_index);
	}

	private class NormalHeight
	{
		public float h;
		public Vector3 normal;
		public boolean isBorder;

		public NormalHeight(int tx, int ty, GridChunk chunk)
		{
			// получаем высоты соседних тайлов
			float h1 = chunk.getHeight(tx, ty);
			float h2 = chunk.getHeight(tx - 1, ty - 1);
			float h3 = chunk.getHeight(tx - 1, ty);
			float h4 = chunk.getHeight(tx, ty - 1);

			// если хоть один из соседних тайлов не определен поставим признак того что это граница чанка
			isBorder = h1 <= MapCache.FAKE_HEIGHT || h2 <= MapCache.FAKE_HEIGHT || h3 <= MapCache.FAKE_HEIGHT || h4 <= MapCache.FAKE_HEIGHT;

			// выставим тайлам которых нет
			h1 = h1 <= MapCache.FAKE_HEIGHT ? 0f : h1;
			h2 = h2 <= MapCache.FAKE_HEIGHT ? 0f : h2;
			h3 = h3 <= MapCache.FAKE_HEIGHT ? 0f : h3;
			h4 = h4 <= MapCache.FAKE_HEIGHT ? 0f : h4;

			// посчитаем среднюю высоту вершины по четырем соседним тайлам
			h = (h1 + h2 + h3 + h4) / 4f;

			// посчитаем нормаль
			normal = new Vector3(h1 - h2, h3 - h4, 2.0f).nor();
		}
	}

	/**
	 * получить высоту указанного тайла
	 */
	public float getHeight(int tx, int ty)
	{
		int x = tx - _gx;
		int y = ty - _gy;
		if (x >= _gx && x < _gx + MapCache.GRID_SIZE && y >= _gy && y < _gy + MapCache.GRID_SIZE)
		{
			return _grid._heights[y][x];
		}
		return MapCache.getTileHeight(tx, ty);
	}

	public Mesh getMesh()
	{
		return _mesh;
	}

	public BoundingBox getBoundingBox()
	{
		return _boundingBox;
	}

	public void clear()
	{
		_mesh.dispose();
	}

	public boolean isBorder()
	{
		return _isBorder;
	}
}
