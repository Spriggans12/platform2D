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

	// TODO bitmap
	protected int color;

	public void render(Screen screen, int xOffs, int yOffs) {
		screen.renderSquare(x, y, 20, color);
	}

	public void tick() {
	}
}
