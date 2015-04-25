package fr.spriggans.game.level.levelObjects.landscape;

import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.gfx.Screen;

public abstract class AbstractLandscapeCollidable extends AbstractLevelElement {
	/** BoundingBox de l'objet collidable. Englobe totalement l'objet. */
	protected Rectangle boundingBox;

	/** Geometrie de l'objet collidable. */
	protected GeneralPath geometry;

	public AbstractLandscapeCollidable(int x, int y) {
		super(x, y);
		color = 0xFF00FF00;
	}

	protected void initBoundingBox() {
		boundingBox = geometry.getBounds();
	}

	public void renderBoundingBox(Screen screen) {
		screen.renderRectangle(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height, 0xFFFF0000, true, 2);
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public GeneralPath getGeometry() {
		return geometry;
	}
}
