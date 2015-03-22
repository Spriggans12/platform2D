package fr.spriggans.game.level.levelObjects;

import fr.spriggans.gfx.Screen;

public abstract class AbstractLevelElement {
	/** Coordonnée coin NW */
	protected int x;
	/** Coordonnée coin NW */
	protected int y;

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
