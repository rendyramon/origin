package com.a4server.gameserver.model.event;

import com.a4server.gameserver.model.GameObject;
import com.a4server.gameserver.network.serverpackets.GameServerPacket;

/**
 * базовое игровое событие
 * рассылается объектами или гридом всем живым объектам внутри грида
 * к событию может быть прикреплен пакет. тогда он будет разослан всем клиентам которые обработали событие
 * а также дополнительная информация _extraInfo описывающая событие
 * Created by arksu on 11.02.15.
 */
public class Event
{
	public enum EventType
	{
		/**
		 * тип события по умолчанию. прикрепленный пакет шлется всем у кого он в known списке
		 */
		DEFAULT,
		MOVE,
		STOP_MOVE,
		CHAT_GENERAL_MESSAGE,
		INTERACT
	}

	/**
	 * объект который сгенерировал событие
	 * к которому оно относится
	 */
	protected final GameObject _object;

	/**
	 * тип сообщения
	 */
	protected final EventType _type;

	/**
	 * дополнительная информация о событии
	 */
	protected Object _extraInfo = null;

	/**
	 * пакет прикрепленный к событию. если другой объект обработал это событие
	 * то пакет будет отослан ему
	 */
	protected GameServerPacket _packet = null;

	public Event(GameObject object, EventType type)
	{
		_object = object;
		_type = type;
	}

	public Event(GameObject object, EventType type, GameServerPacket pkt)
	{
		_object = object;
		_type = type;
		_packet = pkt;
	}

	public Event(GameObject object, EventType type, Object extraInfo)
	{
		_object = object;
		_type = type;
		_extraInfo = extraInfo;
	}

	public GameObject getObject()
	{
		return _object;
	}

	public EventType getType()
	{
		return _type;
	}

	public GameServerPacket getPacket()
	{
		return _packet;
	}

	public void setPacket(GameServerPacket pkt)
	{
		_packet = pkt;
	}

	public Object getExtraInfo()
	{
		return _extraInfo;
	}

	@Override
	public String toString()
	{
		return "(event " + _object +
			   " type=" + _type +
			   (_extraInfo != null ? " extra=" + _extraInfo : "") +
			   ")";
	}
}
