package fr.spriggans.game.level.levelObjects.entities;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.gfx.Bitmap;
import fr.spriggans.gfx.Screen;

public class Player extends AbstractLevelElement {
	Inputs inputs;

	// TODO : RM ME : utilisé juste pour le test.
	private boolean isFacingLeft = false;

	public Player(int x, int y, Inputs input) {
		super(x, y);
		color = 0xFFFF0000;
		this.inputs = input;
	}

	@Override
	public void tick() {
		final int speed = 12;

		if (inputs.left.isPressed()) {
			x -= speed;
			isFacingLeft = true;
		}
		if (inputs.right.isPressed()) {
			x += speed;
			isFacingLeft = false;
		}
		if (inputs.down.isPressed())
			y += speed;
		if (inputs.up.isPressed())
			y -= speed;

		// if (x < 0)
		// x = Level.LEVEL_WIDTH;
		// if (y < 0)
		// y = Level.LEVEL_HEIGHT;
		// if (x > Level.LEVEL_WIDTH)
		// x = 0;
		// if (y > Level.LEVEL_HEIGHT)
		// y = 0;
	}

	public int getXOffset(Screen screen, int lvlWidth) {
		if (lvlWidth < screen.getWidth())
			return (lvlWidth - screen.getWidth()) / 2;
		if (x <= screen.xOffs + screen.screenSlideDistance)
			return x - screen.screenSlideDistance;
		if (x >= screen.xOffs + screen.getWidth() - screen.screenSlideDistance)
			return x - screen.getWidth() + screen.screenSlideDistance;
		return screen.xOffs;
	}

	public int getYOffset(Screen screen, int lvlHeight) {
		if (lvlHeight < screen.getHeight())
			return (lvlHeight - screen.getHeight()) / 2;
		if (y <= screen.yOffs + screen.screenSlideDistance)
			return y - screen.screenSlideDistance;
		if (y >= screen.yOffs + screen.getHeight() - screen.screenSlideDistance)
			return y - screen.getHeight() + screen.screenSlideDistance;
		return screen.yOffs;
	}

	@Override
	public void render(Screen screen, int xOffs, int yOffs) {
		Bitmap.GASPARD.render(screen, x, y,
				isFacingLeft ? Screen.MIRROR_HORIZONTAL : 0);
	}
}
