package fr.spriggans.game.level.levelObjects.landscape;

public class LandscapeNotCollidable extends AbstractLandscape {
	public LandscapeNotCollidable(int x, int y, int w, int h) {
		super(x, y, false, w, h);
		color = 0xFF007700;
	}
}
