package com.a2client;

import com.a2client.model.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * описывает класс моего персонажа и всего что с ним связано
 * параметры, статы, скиллы и прочее
 */
public class Player
{
	private static final Logger _log = LoggerFactory.getLogger(Player.class.getName());
	private static Player _instance;

	private int _objectId;
	private String _name;

	private Hand _hand;

	public void setObjectId(int myObjectId)
	{
		_objectId = myObjectId;
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public void setName(String myName)
	{
		_name = myName;
	}

	public String getName()
	{
		return _name;
	}

	public static void init()
	{
		if (_instance != null)
		{
			_log.error("Player init: instance is not NULL!");
		}
		_instance = new Player();
	}

	public void dispose()
	{
		_instance = null;
	}

	public static Player getInstance()
	{
		return _instance;
	}

	public Hand getHand()
	{
		return _hand;
	}

	public void setHand(Hand hand)
	{
		if (_hand != hand)
		{
			if (hand != null)
			{
				hand.makeControl();
			}
			if (_hand != null)
			{
				_hand.dispose();
			}
			_hand = hand;
		}
	}

}
