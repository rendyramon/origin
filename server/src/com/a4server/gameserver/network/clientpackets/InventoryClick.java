package com.a4server.gameserver.network.clientpackets;

import com.a4server.gameserver.model.GameObject;
import com.a4server.gameserver.model.Hand;
import com.a4server.gameserver.model.Player;
import com.a4server.gameserver.model.inventory.Inventory;
import com.a4server.gameserver.model.inventory.InventoryItem;
import com.a4server.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * клик по объекту в инвентаре
 * Created by arksu on 26.02.15.
 */
public class InventoryClick extends GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(InventoryClick.class.getName());

	public static final int WAIT_LOCK = 300;

	private int _inventoryId;
	private int _objectId;
	private int _btn;
	private int _mod;
	private int _offsetX;
	private int _offsetY;
	private int _x;
	private int _y;

	@Override
	public void readImpl()
	{
		_inventoryId = readD();
		_objectId = readD();
		_btn = readC();
		_mod = readC();
		_offsetX = readC();
		_offsetY = readC();

	}

	@Override
	public void run()
	{
		Player player = client.getActiveChar();
		if (player != null && _btn == 0)
		{
			_log.debug("InventoryClick: obj=" + _objectId + " inv=" + _inventoryId + " offset=" + _offsetX + ", " + _offsetY);

			// держим в руке что-то?
			if (player.getHand() == null)
			{
				// в руке ничего нет. возьмем из инвентаря
				InventoryItem item = null;
				if (player.getInventory() != null)
				{
					item = player.getInventory().findItem(_objectId);
				}
				// не нашли эту вещь у себя в инвентаре
				// попробуем найти в объекте с которым взаимодействуем
				if (item == null && player.isInteractive())
				{
					for (GameObject object : player.getInteractWith())
					{
						item = object.getInventory() != null ? object.getInventory().findItem(_objectId) : null;
						if (item != null)
						{
							break;
						}
					}
				}

				// пробуем взять вещь из инвентаря
				if (item != null)
				{
					if (item.getParentInventory().getObject().tryLock(WAIT_LOCK))
					{
						try
						{
							InventoryItem taked = item.getParentInventory().takeItem(item) ? item : null;

							// взяли вещь из инвентаря
							if (taked != null)
							{
								// какая кнопка была зажата
								switch (_mod)
								{
									case 0:
										// ничего не нажато. пихаем в руку
										player.setHand(new Hand(player, taked));
										break;

									case Utils.MOD_CONTROL:
										// сразу перекинем вещь в инвентарь
										putItem(player, taked, -1, -1);
										break;
								}
							}
						}
						finally
						{
							item.getParentInventory().getObject().unlock();
						}
					}
				}
				else
				{
					// возможно какой то баг или ошибка. привлечем внимание
					_log.error("InventoryClick: item=null");
				}
			}
			else
			{
				// положим в инвентарь то что держим в руке
				putItem(player, player.getHand().getItem(), _x, _y);
			}
		}
	}

	/**
	 * положить вещь в инвентарь
	 * @param item вещь которую кладем
	 */
	public void putItem(Player player, InventoryItem item, int x, int y)
	{
		Inventory to = null;
		if (player.getInventory() != null)
		{
			to = player.getInventory().findInventory(_inventoryId);
		}
		if (to == null && player.isInteractive())
		{
			for (GameObject object : player.getInteractWith())
			{
				to = object.getInventory() != null ? object.getInventory().findInventory(_inventoryId) : null;
				if (to != null)
				{
					break;
				}
			}
		}

		// положим в инвентарь
		if (to != null)
		{
			to.putItem(item, x, y);
		}
	}
}
