package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;

import fr.spriggans.game.Launcher;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Screen;

public class BasicEnnemy extends AbstractEntity {
	
	// TODO créer des classes IA.
	private static final double JUMP_ODDS = 0.05;
	private boolean movingRight;
	
	private int count;
	private static final int cols[] = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFF00FFFF, 0xFFFF00FF, 0xFFFFFFFF};
	private static final int frequence = 1;
	

	public BasicEnnemy(int x, int y) {
		super(x, y, EntityAttributes.att_basic_ennemy);
		this.movingRight = false;
		int index = (int)(Math.random() * 6);
		color = cols[index];
		
		count = (int)(Math.random() * frequence);
	}
	
	@Override
	public void render(Screen screen) {
		if (Launcher.DEBUG_SHOW_COLLISION_BOUNDING_BOX)
			for (final AbstractLandscapeCollidable collidable : collidablesInVicinity) {
				collidable.renderBoundingBox(screen);
			}
		
		if(count++ >= frequence) {
			int index = (int)(Math.random() * 6);
			color = cols[index];
			count = 0;
		}
		
		if (Launcher.DEBUG_SHOW_ENTITY_BB)
			screen.renderRectangle(x, y, entityGeometry.getBounds().width, entityGeometry.getBounds().height, color, false, 1);
		 if (Launcher.DEBUG_SHOW_COLLISION_BOUNDING_BOX)
			 for (final Point p : collisionsPoints)
				 screen.renderPixel(0xFFFF0000, x + p.x, y + p.y, false);
	}
	
	@Override
	protected boolean applyInputs() {
		// Reverse si speed nulle.
		if(speedX == 0) {
			movingRight = !movingRight;	
		}
		if(movingRight)
			speedX += groundAcceleration;
		else
			speedX -= groundAcceleration;
		
		// Saute aléatoirement.
		if(Math.random() < JUMP_ODDS && !jumping) {
			jumping = true;
			speedY -= groundJumpSpeed;
		}
			
		
		return true;
	}
}
