package com.a4server.gameserver.model.objects;

import com.a4server.gameserver.model.GameObject;

/**
 * шаблон объекта по которому создаются конечные экземпляры
 * Created by arksu on 15.02.15.
 */
public interface ObjectTemplate
{
	/**
	 * уникальный идентификатор типа объекта
	 */
	int getTypeId();

	/**
	 * ширина объекта
	 */
	int getWidth();

	/**
	 * высота объекта
	 */
	int getHeight();

	/**
	 * имя шаблона
	 */
	String getName();

	Class<? extends GameObject> getClassName();

	CollisionTemplate getCollision();

	InventoryTemplate getInventory();

	ItemTemplate getItem();
}