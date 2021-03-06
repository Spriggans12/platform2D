package fr.spriggans.game.level.levelObjects.landscape;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import fr.spriggans.gfx.Screen;

public class LandscapeCollidableRectangle extends AbstractLandscapeCollidable {

	private boolean isSkewed;

	private int dh;

	private int initialHeight;

	public LandscapeCollidableRectangle(int x0, int y0, int w, int h) {
		super(x0, y0);
		color = 0xffff8800;
		geometry = new GeneralPath(new Rectangle2D.Float(x0, y0, w, h));
		initBoundingBox();
		isSkewed = false;
	}

	public LandscapeCollidableRectangle(int x0, int y0, int w, int h, int deltaH) {
		super(x0, y0);
		this.dh = deltaH;
		this.initialHeight = h;
		geometry = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
		geometry.moveTo(x0, y0);
		geometry.lineTo(x0 + w, y0 + deltaH);
		geometry.lineTo(x0 + w, y0 + deltaH + h);
		geometry.lineTo(x0, y0 + h);
		geometry.closePath();
		color = 0xff888800;
		initBoundingBox();
		isSkewed = true;
	}

	@Override
	public void render(Screen screen) {
		if (!isSkewed)
			screen.renderRectangle(x, y, boundingBox.width, boundingBox.height, color, true, 1);
		else
			screen.renderParallelogram(x, y, boundingBox.width, initialHeight, dh, color, false);
	}

	public boolean getIsSkewed() {
		return isSkewed;
	}

	public void setSkewed(boolean isSkewed) {
		this.isSkewed = isSkewed;
	}

	public int getDh() {
		return dh;
	}

	public void setDh(int dh) {
		this.dh = dh;
	}
}
