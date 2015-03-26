package fr.spriggans.game.level.levelObjects.landscape;

import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.gfx.Screen;

public abstract class AbstractLandscape extends AbstractLevelElement {
	protected boolean collidable;

	// TODO => MOVE ME TO COLLIDABLE ONLY.
	protected int wi;
	protected int he;

	public AbstractLandscape(int x, int y, boolean collidable, int w, int h) {
		super(x, y);
		this.collidable = collidable;
		wi = w;
		he = h;
	}

	@Override
	public void render(Screen screen) {
		screen.renderRectangle((int) x, (int) y, wi, he, color);
	}
}
