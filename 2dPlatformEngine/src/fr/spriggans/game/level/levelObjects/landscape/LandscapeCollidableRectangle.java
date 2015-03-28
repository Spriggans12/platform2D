package fr.spriggans.game.level.levelObjects.landscape;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import fr.spriggans.gfx.Screen;

public class LandscapeCollidableRectangle extends AbstractLandscapeCollidable {

	// TODO : Add skew et tout...
	public LandscapeCollidableRectangle(int x0, int y0, int w, int h) {
		super(x0, y0, w, h);
		geometry = new GeneralPath(new Rectangle2D.Float(x0, y0, w, h));
		initBoundingBox();
	}

	@Override
	public void render(Screen screen) {
		screen.renderRectangle((int) x, (int) y, boundingBox.width, boundingBox.height, color);
	}
}
