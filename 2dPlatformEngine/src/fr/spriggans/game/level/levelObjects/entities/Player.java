package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.Level;
import fr.spriggans.gfx.Animation;
import fr.spriggans.gfx.Screen;

public class Player extends AbstractEntity {
	Inputs inputs;

	// TODO : RM ME : utilisé juste pour le test.
	private boolean isFacingLeft = false;

	// TODO : TMP for test...
	private static final int PL_W = 20;
	private static final int PL_H = 40;

	public Player(int x, int y, Level level, Inputs input) {
		super(x, y, level, PL_W, PL_H);

		collisionsPoints = new Point[] { new Point(5, 0), new Point(15, 0), new Point(5, 40), new Point(15, 40), new Point(0, 10), new Point(
				0, 30), new Point(20, 10), new Point(20, 30), };

		// TODO : Change it
		animation = Animation.TEST_ANIM_IDDLE;

		this.inputs = input;
	}

	@Override
	public void tick() {
		super.tick();

		final int speed = 4;
		if (inputs.left.isPressed()) {
			x = (x - speed);
			isFacingLeft = true;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}
		if (inputs.right.isPressed()) {
			x = (x + speed);
			isFacingLeft = false;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}
		if (inputs.down.isPressed())
			y = (y + speed);
		if (inputs.up.isPressed())
			y = (y - speed);

		if (!inputs.left.isPressed() && !inputs.right.isPressed()) {
			if (animation.equals(Animation.TEST_ANIM_WALK)) {
				animation.raz();
				animation = Animation.TEST_ANIM_IDDLE;
			}
		}
		animation.tick();
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
	public void render(Screen screen) {
		animation.render(screen, x, y, !isFacingLeft ? Screen.MIRROR_HORIZONTAL : 0);
	}
}
