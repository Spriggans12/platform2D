package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.Launcher;
import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Screen;

public class Player extends AbstractEntity {
	Inputs inputs;

	// TODO : TMP for test...
	private static final int PL_W = 20;
	private static final int PL_H = 40;

	public Player(int x, int y, Level level, Inputs input) {
		super(x, y, level, PL_W, PL_H);

		collisionsPoints = new Point[] { new Point(4, 0), new Point(14, 0), new Point(4, 39), new Point(14, 39), new Point(0, 9), new Point(
				0, 29), new Point(19, 9), new Point(19, 29) };

		// collisionsPoints = new Point[] { new Point(5, 0), new Point(15, 0), new Point(5, 40), new Point(15, 40), new Point(0, 10), new Point(
		// 0, 30), new Point(20, 10), new Point(20, 30) };

		initGeometryFromCollisionPoints();

		final float S = 0.2f;
		// TODO : les placer dans le constructeur d'entités.

		this.groundFriction = (int) (30 * S);
		this.groundAcceleration = (int) (10 * S);
		this.gravityStrength = (int) (5 * S);
		this.groundJumpSpeed = (int) (100 * S);
		this.maxSpeedX = (int) (50 * S);
		this.maxSpeedY = (int) (100 * S);

		// TODO : Change it
		// animation = Animation.TEST_ANIM_IDDLE;

		this.inputs = input;
	}

	@Override
	protected boolean applyInputs() {
		boolean horizontalMovement = false;
		if (inputs.left.isPressed()) {
			speedX -= groundAcceleration;
			horizontalMovement = true;
			// if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
			// animation.raz();
			// animation = Animation.TEST_ANIM_WALK;
			// }
		}
		if (inputs.right.isPressed()) {
			speedX += groundAcceleration;
			horizontalMovement = true;
			// if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
			// animation.raz();
			// animation = Animation.TEST_ANIM_WALK;
			// }
		}
		if ((inputs.up.isPressed()) && !jumping) {
			jumping = true;
			speedY -= groundJumpSpeed;
		}

		// Fin de l'annimaition de marche.
		if (!inputs.left.isPressed() && !inputs.right.isPressed()) {
			// if (animation.equals(Animation.TEST_ANIM_WALK)) {
			// animation.raz();
			// animation = Animation.TEST_ANIM_IDDLE;
			// }
		}

		return horizontalMovement;
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
		if (Launcher.DEBUG_SHOW_COLLISION_BOUNDING_BOX)
			for (final AbstractLandscapeCollidable collidable : collidablesInVicinity) {
				collidable.renderBoundingBox(screen);
			}
		if (Launcher.DEBUG_SHOW_ENTITY_SPRITE)
			;
		// animation.render(screen, (int) x, (int) y, !isFacingLeft ? Screen.MIRROR_HORIZONTAL : 0);

		if (Launcher.DEBUG_SHOW_ENTITY_BB)
			screen.renderRectangle(x, y, entityGeometry.getBounds().width, entityGeometry.getBounds().height, 0xFFFFFF00, true, 1);
		if (Launcher.DEBUG_SHOW_COLLISION_BOUNDING_BOX)
			for (final Point p : collisionsPoints)
				screen.renderPixel(0xFFFF0000, x + p.x, y + p.y);
	}
}
