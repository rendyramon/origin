package com.a4server.gameserver.network.clientpackets;

import com.a4server.gameserver.model.GameLock;
import com.a4server.gameserver.model.GameObject;
import com.a4server.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by arksu on 20.08.16.
 */
public class ContextSelect extends GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ContextSelect.class.getName());

	private int _objectId;
	private String _item;

	@Override
	public void readImpl()
	{
		_objectId = readD();
		_item = readS();
	}

	@Override
	public void run()
	{
		_log.debug("context select: " + _item);
		Player player = client.getPlayer();
		if (player != null)
		{
			try (GameLock ignored = player.tryLock())
			{
				GameObject object = player.isKnownObject(_objectId);
				if (object.getContextMenu(player).contains(_item))
				{
					object.contextSelected(player, _item);
				}
			}
			catch (Exception e)
			{
				_log.error("ContextSelect error:" + e.getMessage(), e);
			}
		}
	}
}
