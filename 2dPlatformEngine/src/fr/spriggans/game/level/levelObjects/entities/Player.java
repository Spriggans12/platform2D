package fr.spriggans.game.level.levelObjects.entities;

import java.awt.event.KeyEvent;

import fr.spriggans.game.KeyboardInput;
import fr.spriggans.game.Launcher;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Screen;

public class Player extends AbstractEntity {
	private KeyboardInput keyboard;

	public Player(int x, int y) {
		super(x, y, EntityAttributes.att_player);
	}

	@Override
	protected boolean applyInputs() {
		boolean horizontalMovement = false;

		keyboard.poll();

		if (keyboard.keyDown(KeyEvent.VK_LEFT) || keyboard.keyDown(KeyEvent.VK_Q)) {
			speedX -= groundAcceleration;
			horizontalMovement = true;
			// if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
			// animation.raz();
			// animation = Animation.TEST_ANIM_WALK;
			// }
		}
		if (keyboard.keyDown(KeyEvent.VK_RIGHT) || keyboard.keyDown(KeyEvent.VK_D)) {
			speedX += groundAcceleration;
			horizontalMovement = true;
			// if (animation.equals(Animation.TEST_ANIM_IDDLE)) {
			// animation.raz();
			// animation = Animation.TEST_ANIM_WALK;
			// }
		}
		if ((keyboard.keyDown(KeyEvent.VK_UP) || keyboard.keyDown(KeyEvent.VK_Z)) && !jumping) {
			jumping = true;
			speedY -= groundJumpSpeed;
		}

		// Fin de l'annimaition de marche.
		if (!(keyboard.keyDown(KeyEvent.VK_LEFT) || keyboard.keyDown(KeyEvent.VK_Q))
				&& !(keyboard.keyDown(KeyEvent.VK_RIGHT) || keyboard.keyDown(KeyEvent.VK_D))) {
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
		// animation.render(screen, (int) x, (int) y, !isFacingLeft ?
		// Screen.MIRROR_HORIZONTAL : 0);

		if (Launcher.DEBUG_SHOW_ENTITY_BB)
			screen.renderRectangle(x, y, entityGeometry.getBounds().width, entityGeometry.getBounds().height, 0x44FFFF00, false, 1);
		// if (Launcher.DEBUG_SHOW_COLLISION_BOUNDING_BOX)
		// for (final Point p : collisionsPoints)
		// screen.renderPixel(0xFFFF0000, x + p.x, y + p.y, false);
	}

	public void setKeyboardInput(KeyboardInput keyboard) {
		this.keyboard = keyboard;
	}

	public KeyboardInput getKeyboardInput() {
		return keyboard;
	}
}
