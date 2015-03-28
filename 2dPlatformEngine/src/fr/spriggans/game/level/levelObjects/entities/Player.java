package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
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

		collisionsPoints = new Point[] { new Point(4, 0), new Point(14, 0), new Point(4, 39), new Point(14, 39), new Point(0, 9), new Point(
				0, 29), new Point(19, 9), new Point(19, 29), };
		xPointsShape = new int[] { 5, 15, 20, 20, 15, 5, 0, 0 };
		yPointsShape = new int[] { 0, 0, 10, 30, 40, 40, 30, 10 };
		entityGeometry = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPointsShape.length);
		entityGeometry.moveTo(xPointsShape[0], yPointsShape[0]);
		for (int i = 0; i < xPointsShape.length; i++)
			entityGeometry.lineTo(xPointsShape[i], yPointsShape[i]);
		entityGeometry.closePath();

		// TODO : les placer dans le constructeur d'entités.

		final int S = 1;

		this.groundFriction = 0.3f * S;
		this.groundAcceleration = 0.2f * S;
		this.gravityStrength = 0.5f * S;
		// this.gravityStrength = 0.05f * S;
		this.groundJumpSpeed = 10 * S;
		// this.groundJumpSpeed = 2f * S;
		this.maxSpeedX = 5 * S;
		this.maxSpeedY = 10 * S;
		// TODO : Change it
		animation = Animation.TEST_ANIM_IDDLE;

		this.inputs = input;
	}

	@Override
	protected boolean applyInputs() {
		boolean horizontalMovement = false;

		if (inputs.left.isPressed()) {
			speedX -= groundAcceleration;
			isFacingLeft = true;
			horizontalMovement = true;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}
		if (inputs.right.isPressed()) {
			speedX += groundAcceleration;
			isFacingLeft = false;
			horizontalMovement = true;
			if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
				animation.raz();
				animation = Animation.TEST_ANIM_WALK;
			}
		}

		if (inputs.up.isPressed() && !jumping) {
			jumping = true;
			speedY -= groundJumpSpeed;
		}

		// Fin de l'annimaition de marche.
		if (!inputs.left.isPressed() && !inputs.right.isPressed()) {
			if (animation.equals(Animation.TEST_ANIM_WALK)) {
				animation.raz();
				animation = Animation.TEST_ANIM_IDDLE;
			}
		}

		return horizontalMovement;
	}

	public int getXOffset(Screen screen, int lvlWidth) {
		if (lvlWidth < screen.getWidth())
			return (lvlWidth - screen.getWidth()) / 2;
		if (x <= screen.xOffs + screen.screenSlideDistance)
			return (int) (x - screen.screenSlideDistance);
		if (x >= screen.xOffs + screen.getWidth() - screen.screenSlideDistance)
			return (int) (x - screen.getWidth() + screen.screenSlideDistance);
		return screen.xOffs;
	}

	public int getYOffset(Screen screen, int lvlHeight) {
		if (lvlHeight < screen.getHeight())
			return (lvlHeight - screen.getHeight()) / 2;
		if (y <= screen.yOffs + screen.screenSlideDistance)
			return (int) (y - screen.screenSlideDistance);
		if (y >= screen.yOffs + screen.getHeight() - screen.screenSlideDistance)
			return (int) (y - screen.getHeight() + screen.screenSlideDistance);
		return screen.yOffs;
	}

	@Override
	public void render(Screen screen) {
		// TODO : debug only : affichage des BB des voisins.
		for (final AbstractLandscapeCollidable collidable : collidablesInVicinity) {
			collidable.renderBoundingBox(screen);
		}
		animation.render(screen, (int) x, (int) y, !isFacingLeft ? Screen.MIRROR_HORIZONTAL : 0);

		// TODO : DEBUG ONLY
		final Rectangle a = entityGeometry.getBounds();
		screen.renderRectangle((int) x, (int) y, a.width, a.height, 0xFFFFFF00, true, 1);
		for (final Point p : collisionsPoints)
			screen.renderPixel(0xFFFF0000, (int) x + p.x, (int) y + p.y);
	}
}
