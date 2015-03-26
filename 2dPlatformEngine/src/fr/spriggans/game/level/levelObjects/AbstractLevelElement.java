package fr.spriggans.game.level.levelObjects;

import fr.spriggans.gfx.Screen;

public abstract class AbstractLevelElement {
	/** Coordonnee coin NW pour l'affichage */
	protected float x;
	/** Coordonnee coin NW pour l'affichage */
	protected float y;

	public AbstractLevelElement(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// TODO : RM-ME
	protected int color;

	public void render(Screen screen) {
	}

	public void tick() {
	}
}
