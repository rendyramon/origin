package com.a4server.gameserver.network.serverpackets;

import com.a4server.gameserver.model.inventory.Inventory;
import com.a4server.gameserver.model.inventory.InventoryItem;
import javolution.util.FastList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * шлем апдейт инвентаря. если не открыт - надо создать окошко и показать
 * Created by arksu on 26.02.15.
 */
public class InventoryUpdate extends GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(InventoryUpdate.class.getName());

	Inventory _inventory;

	public InventoryUpdate(Inventory inventory)
	{
		if (inventory == null)
		{
			throw new RuntimeException("null inventory");
		}
		_inventory = inventory;
	}

	@Override
	protected void write()
	{
		writeC(0x18);
		writeD(_inventory.getObject().getObjectId());
		writeD(_inventory.getInvenroyId());
		writeH(_inventory.getWidth());
		writeH(_inventory.getHeight());
		FastList<InventoryItem> items = _inventory.getItems();
		writeH(items.size());
		if (items.size() > 0)
		{
			for (InventoryItem item : items)
			{
				writeD(item.getObjectId());
				writeD(item.getTemplate().getItemId());
				writeS(item.getTemplate().getIconName());
				writeD(item.getQ());
				writeC(item.getX());
				writeC(item.getY());
				writeC(item.getWidth());
				writeC(item.getHeight());
				writeH(item.getAmount());
				writeC(item.getStage());
				writeH(item.getTicks());
				writeH(item.getTicksTotal());
			}
		}
	}
}
