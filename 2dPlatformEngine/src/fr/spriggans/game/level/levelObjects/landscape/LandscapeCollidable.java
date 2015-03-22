package fr.spriggans.game.level.levelObjects.landscape;

public class LandscapeCollidable extends AbstractLandscape {
	public LandscapeCollidable(int x, int y, int w, int h) {
		super(x, y, true, w, h);
		color = 0xFF00FF00;
	}
}
