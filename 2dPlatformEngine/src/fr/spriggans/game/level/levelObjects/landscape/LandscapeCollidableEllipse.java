package fr.spriggans.game.level.levelObjects.landscape;

import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import fr.spriggans.gfx.Screen;

public class LandscapeCollidableEllipse extends AbstractLandscapeCollidable {

	public LandscapeCollidableEllipse(int xNW, int yNW, int totalW, int totalH) {
		super(xNW, yNW);
		this.color = 0xFF00FFFF;
		geometry = new GeneralPath(new Ellipse2D.Float(xNW, yNW, totalW, totalH));
		initBoundingBox();
	}

	@Override
	public void render(Screen screen) {
		screen.renderEllipse(x, y, boundingBox.width, boundingBox.height, color);
	}
}
