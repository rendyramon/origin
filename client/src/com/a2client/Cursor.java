/*
 * This file is part of the Origin-World game client.
 * Copyright (C) 2013 Arkadiy Fattakhov <ark@ark.su>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.a2client;

import com.a2client.util.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class Cursor
{
	static private Cursor _instance;

	private static final String CURSORS_DIR = "assets/cursors/";

	static public Cursor getInstance()
	{
		if (_instance == null)
		{
			_instance = new Cursor();
		}
		return _instance;
	}

	public void setCursor(String name)
	{
		if (Utils.isEmpty(name)) name = "arrow";

		Pixmap pm = new Pixmap(Gdx.files.internal(CURSORS_DIR + name + ".png"));
		com.badlogic.gdx.graphics.Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
		Gdx.graphics.setCursor(cursor);

		pm.dispose();
	}

	public void render()
	{

	}
}
