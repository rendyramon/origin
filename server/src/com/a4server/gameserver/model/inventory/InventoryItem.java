package com.a4server.gameserver.model.inventory;

import com.a4server.gameserver.model.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * вещь в инвентаре
 * Created by arksu on 23.02.15.
 */
public class InventoryItem extends AbstractItem
{
	private static final Logger _log = LoggerFactory.getLogger(InventoryItem.class.getName());

	/**
	 * инвентарь в котором хранится данная вещь
	 */
	private Inventory _parentInventory;

	public InventoryItem(Inventory parentInventory, ResultSet rset) throws SQLException
	{
		super(parentInventory.getObject(), rset);
		_parentInventory = parentInventory;
	}

	public InventoryItem(AbstractItem other)
	{
		super(other);
	}

	public InventoryItem(GameObject object, int typeId, int q, int amount, int stage, int ticks, int ticksTotal)
	{
		super(object, typeId, q, amount, stage, ticks, ticksTotal);
	}

	public InventoryItem(GameObject object, int typeId, int q)
	{
		super(object, typeId, q, 1, 0, 0, 0);
	}

	public Inventory getParentInventory()
	{
		return _parentInventory;
	}

	public void setParentInventory(Inventory parentInventory)
	{
		_parentInventory = parentInventory;
	}
}
