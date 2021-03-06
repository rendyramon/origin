package com.a2client.screens;

import com.a2client.Lang;
import com.a2client.Main;
import com.a2client.gui.GUI;
import com.a2client.gui.GUI_Button;
import com.a2client.gui.GUI_Edit;
import com.a2client.gui.GUI_Label;
import com.a2client.util.Align;
import com.badlogic.gdx.Gdx;

public class CharacterCreate extends BaseScreen
{
	GUI_Button btnCreate, btnExit;
	GUI_Edit editNickname;
	GUI_Label lblNickname;

	public CharacterCreate()
	{
		GUI.reCreate();

		lblNickname = new GUI_Label(GUI.rootNormal());
		lblNickname.caption = Lang.getTranslate("Game.character.nickname");
		lblNickname.align = Align.Align_Center;
		lblNickname.setSize(150, 25);
		lblNickname.setPos(0, 100);
		lblNickname.centerX();

		editNickname = new GUI_Edit(GUI.rootNormal());
		editNickname.setSize(150, 27);
		editNickname.setPos(lblNickname.pos.add(0, 30));
		editNickname.centerX();

		btnCreate = new GUI_Button(GUI.rootNormal())
		{
			@Override
			public void DoClick()
			{
				if (!editNickname.text.isEmpty())
				{
					doCreateCharacter();
				}
			}
		};
		btnCreate.caption = Lang.getTranslate("Game.character.create");
		btnCreate.setSize(150, 25);
		btnCreate.setPos(editNickname.pos.add(0, 45));
		btnCreate.centerX();

		btnExit = new GUI_Button(GUI.rootNormal())
		{
			@Override
			public void DoClick()
			{
				CharacterSelect.Show();
			}
		};
		btnExit.caption = Lang.getTranslate("Game.cancel");
		btnExit.setSize(100, 25);
		btnExit.setPos(Gdx.graphics.getWidth() - 110, Gdx.graphics.getHeight() - 35);

	}

	public void doCreateCharacter()
	{
		new com.a2client.network.game.clientpackets.CharacterCreate(editNickname.text, 0, 0, 0, 0).send();
	}

	static public void Show()
	{
		Main.freeScreen();
		Main.getInstance().setScreen(new CharacterCreate());
	}
}
