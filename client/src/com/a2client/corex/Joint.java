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

package com.a2client.corex;

import java.io.IOException;

public class Joint
{
    public int parent;
    public DualQuat bind;
    public DualQuat frame;

    public Joint(MyInputStream in)
    {
        try
        {
            this.parent = in.readInt();
            this.bind = new DualQuat(in);
            this.frame = new DualQuat(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String toString()
    {
        return "(" + parent + " " + bind.toString() + " " + frame.toString();
    }
}