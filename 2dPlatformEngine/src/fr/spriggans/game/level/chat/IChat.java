package fr.spriggans.game.level.chat;

import fr.spriggans.gfx.Screen;

public interface IChat {
	public void render(Screen screen);

	/** Renvoie une String contenant la séquence écrire et submitée par le chat. */
	public String tick(String name);

	public void addMessage(String autor, String msg);
}
