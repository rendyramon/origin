package com.a4server.gameserver.model.objects.impl;

import com.a4server.gameserver.model.GameObject;
import com.a4server.gameserver.model.Grid;
import com.a4server.gameserver.model.Player;
import com.a4server.gameserver.model.objects.ObjectTemplate;
import com.a4server.gameserver.network.serverpackets.ContextMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arksu on 22.08.16.
 */
public class Tree extends GameObject
{
	private static final Logger _log = LoggerFactory.getLogger(Tree.class.getName());

	public Tree(Grid grid, ResultSet rset) throws SQLException
	{
		super(grid, rset);
	}

	public Tree(int objectId, ObjectTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void actionClick(Player player)
	{
		List<String> list = new ArrayList<>();
		list.add("take_branch");
		list.add("chop");

		player.getClient().sendPacket(new ContextMenu(_objectId, list));
	}
}
