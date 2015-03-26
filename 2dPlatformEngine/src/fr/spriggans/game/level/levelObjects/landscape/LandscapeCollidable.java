package fr.spriggans.game.level.levelObjects.landscape;

import fr.spriggans.game.physics.BoundingBox;

public class LandscapeCollidable extends AbstractLandscape {

	/** BoundingBox de l'objet collidable. Englobe totalement l'objet. */
	private final BoundingBox boundingBox;

	public LandscapeCollidable(int x, int y, int w, int h) {
		super(x, y, true, w, h);
		color = 0xFF00FF00;
		boundingBox = new BoundingBox(x, y, x + w, y + h);
	}
	// public int getX() {
	// return x;
	// }
	//
	// public int getY() {
	// return y;
	// }
	//
	// public int getWidth() {
	// return wi;
	// }
	//
	// public int getHeight() {
	// return he;
	// }

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
}
