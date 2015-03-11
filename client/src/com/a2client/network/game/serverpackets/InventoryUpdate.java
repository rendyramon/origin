package com.a2client.network.game.serverpackets;

import com.a2client.InventoryCache;
import com.a2client.model.Inventory;
import com.a2client.model.InventoryItem;
import com.a2client.network.game.GamePacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * пришло обновление инвентаря
 * Created by arksu on 26.02.15.
 */
public class InventoryUpdate extends GameServerPacket
{
    static
    {
        GamePacketHandler.AddPacketType(0x18, InventoryUpdate.class);
    }

    private static final Logger _log = LoggerFactory.getLogger(InventoryUpdate.class.getName());

    int _parentObjectId;
    int _inventoryId;
    Inventory _inventory;
    List<InventoryItem> _items = new ArrayList<>();

    @Override
    public void readImpl()
    {
        _parentObjectId = readD();
        _inventoryId = readD();

        int size = readH();
        while (size > 0)
        {
            size--;

            int objectId = readD();
            int typeId = readD();
            int q = readD();
            int x = readC();
            int y = readC();
            int amount = readH();
            int stage = readC();
            int ticks = readH();
            int ticksTotal = readH();

            _items.add(new InventoryItem(objectId, typeId, q, x, y, stage, amount, ticks, ticksTotal));
        }
    }

    @Override
    public void run()
    {
        _inventory = InventoryCache.getInstance().get(_inventoryId);
        // если такого инвентаря еще нет на клиенте - создадим
        if (_inventory == null)
        {
            _inventory = new Inventory(_parentObjectId, _inventoryId);
            // и добавим в кэш
            InventoryCache.getInstance().add(_inventory);
        }
        // предварительно очистим
        _inventory.clear();
        // и запишем заново все итемы которые пришли от сервера
        for (InventoryItem item : _items)
        {
            _inventory.addItem(item);
        }
        // под конец откроем инвентарь если он еще не был открыт
        InventoryCache.getInstance().openInventory(_inventoryId);
    }
}