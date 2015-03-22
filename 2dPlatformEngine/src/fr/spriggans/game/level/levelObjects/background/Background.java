package fr.spriggans.game.level.levelObjects.background;

import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.gfx.Bitmap;
import fr.spriggans.gfx.Screen;

public class Background extends AbstractLevelElement {

	public Background(int x, int y) {
		super(x, y);
		color = 0xFF777777;
	}

	@Override
	public void render(Screen screen, int xOffs, int yOffs) {
		Bitmap.BG_TEST.render(screen, x, y, 0);
	}
}
