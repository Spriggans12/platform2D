package fr.spriggans.game.level.levelObjects.entities;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.gfx.Animation;
import fr.spriggans.gfx.Screen;

public class Player extends AbstractLevelElement {
	Inputs inputs;

	// TODO : Remonter d'un niveau.
	/** L'animation actuelle jouée par le player. */
	Animation animation;

	// TODO : RM ME : utilisé juste pour le test.
	private boolean isFacingLeft = false;

	public Player(int x, int y, Inputs input) {
		super(x, y);

		animation = Animation.TEST_ANIM_IDDLE;

		this.inputs = input;
	}

	@Override
	public void tick() {
		final int speed = 6;
		if (inputs.left.isPressed()) {
			x -= speed;
			isFacingLeft = true;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}
		if (inputs.right.isPressed()) {
			x += speed;
			isFacingLeft = false;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}
		if (inputs.down.isPressed())
			y += speed;
		if (inputs.up.isPressed())
			y -= speed;

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
		animation.render(screen, x, y, isFacingLeft ? Screen.MIRROR_HORIZONTAL
				: 0);
	}
}
