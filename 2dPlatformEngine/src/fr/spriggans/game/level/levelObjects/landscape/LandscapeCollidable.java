package fr.spriggans.game.level.levelObjects.landscape;

import java.awt.Rectangle;

import fr.spriggans.gfx.Screen;

public class LandscapeCollidable extends AbstractLandscape {

	/** BoundingBox de l'objet collidable. Englobe totalement l'objet. */
	private final Rectangle boundingBox;

	public LandscapeCollidable(int x, int y, int w, int h) {
		super(x, y, true, w, h);
		color = 0xFF00FF00;
		boundingBox = new Rectangle(x, y, w, h);
	}

	public void renderBoundingBox(Screen screen) {
		screen.renderRectangle(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height, 0xFFFF0000, true, 2);
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

}
