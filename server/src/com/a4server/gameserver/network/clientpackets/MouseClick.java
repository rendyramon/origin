package com.a4server.gameserver.network.clientpackets;

import com.a4server.gameserver.model.Player;
import com.a4server.gameserver.model.ai.player.MindMoveAction;
import com.a4server.gameserver.model.position.MoveToPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by arksu on 08.02.15.
 */
public class MouseClick extends GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(MouseClick.class.getName());

	private static final int BUTTON_LEFT = 0;
	private static final int BUTTON_RIGHT = 1;
	
	private int _button;
	private int _x;
	private int _y;
	private int _objectId;

	@Override
	public void readImpl()
	{
		_button = readC();
		_x = readD();
		_y = readD();
		_objectId = readD();
	}

	@Override
	public void run()
	{
		// нажатая кнопка < 10
		boolean isDown = _button < 10;
		_button = _button >= 10 ? _button - 10 : _button;
		Player player = client.getActiveChar();
		if (player != null)
		{
			if (isDown)
			{
				switch (_button)
				{
					case BUTTON_LEFT:
						// если кликнули не в объект
						if (_objectId == 0)
						{
							_log.debug("MoveToPoint to (" + _x + ", " + _y + ")");
							// для простого передвижения не требуется мозг) не надо ни о чем думать
							player.setMind(null);
							// запустим движение. создадим контроллер для этого
							player.StartMove(new MoveToPoint(_x, _y));
						}
						else
						{
							// клик по объекту. бежим к нему и делаем действие над ним
							player.setMind(new MindMoveAction(player, _objectId));
						}
				}
			}
		}
	}
}
