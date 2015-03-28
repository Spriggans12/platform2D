package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Animation;
import fr.spriggans.gfx.Screen;

public class AbstractEntity extends AbstractLevelElement {
	/** La vitesse horizontale. */
	protected float speedX;
	/** La vitesse verticale. */
	protected float speedY;
	/** Taille de la boundingBox. */
	protected int entityWidth;
	/** Taille de la boundingBox. */
	protected int entityHeight;
	/** gravity/f */
	protected float gravityStrength = 0;
	/** friction/f */
	protected float groundFriction = 0;
	/** Accélération au sol/f. */
	protected float groundAcceleration = 0.1f;
	/** Jump speed. */
	protected float groundJumpSpeed = 0;
	/** Maximum horizontal velocity. */
	protected float maxSpeedX = 4;
	/** Maximum vertical velocity. */
	protected float maxSpeedY = 4;
	/** Is the entity jumping ? */
	protected boolean jumping = false;

	/** Les points de collisions de l'entite. 8 Points référencés par rapport aux x,y du AbstractLevelElement.<br/>
	 * Dans l'ordre et de gauche à droite : <br/>
	 * - 0/1 : Top of head <br/>
	 * - 2/3 : Bottom of feet <br/>
	 * - 4/5 : Left <br/>
	 * - 6/7 : Right */
	protected Point collisionsPoints[] = new Point[8];
	/** Points de collisions repris dans le sens horaire. */
	protected int[] xPointsShape;
	/** Points de collisions repris dans le sens horaire. */
	protected int[] yPointsShape;
	/** Octogone représentant le squelette de l'entité. */
	protected GeneralPath entityGeometry;

	/** L'animation actuelle jouee par l'entite. */
	protected Animation animation;

	/** Le Level auquel l'entité appartient. */
	protected Level level;

	/** Nombre max de boucles à faire pour chercher une collision. */
	protected final int MAX_ITERATIONS = 3;

	/** Objets collidables proches de l'entité. Seuls ces objets là seront testés pour les collisions pour éviter d'itérer sur tout. */
	List<AbstractLandscapeCollidable> collidablesInVicinity = new ArrayList<AbstractLandscapeCollidable>();

	public AbstractEntity(int x, int y, Level level, int eWidth, int eHeight) {
		super(x, y);
		this.level = level;
		this.entityWidth = eWidth;
		this.entityHeight = eHeight;
	}

	@Override
	public void tick() {
		// Flags pour savoir le type de collision.
		boolean contactX = true, contactYBottom = true, contactYTop = true;

		// On loope un max de MAX_ITERATIONS fois, jusqu'à ce qu'il y ait eu une collision.
		for (int iteration = 0; iteration < MAX_ITERATIONS && (contactX || contactYBottom || contactYTop); iteration++) {
			collidablesInVicinity.clear();
			contactX = contactYBottom = contactYTop = false;
			float nextMoveX = speedX;
			float nextMoveY = speedY;

			// On calcule le bounding box englobant le mouvement de l'entité.
			int a0 = (int) Math.min(x, x + nextMoveX);
			int b0 = (int) Math.min(y, y + nextMoveY);
			int a1 = (int) Math.max(x + entityWidth, x + entityWidth + nextMoveX);
			int b1 = (int) Math.max(y + entityHeight, y + entityHeight + nextMoveY);
			final Rectangle entityBounds = new Rectangle(a0, b0, a1 - a0, b1 - b0);

			// On checke tous les objets pour voir s'ils sont proches de l'entité.
			for (final AbstractLandscapeCollidable collidable : level.getCollisionLayer()) {
				// Pas besoin de regarder à la main chacun des 4 cotés du collidable.
				// En utilisant la ruse, on peut utiliser la BB combinée de l'entité et du collidable.
				// Si la BB est plus haute|grosse que la hauteur|grosseur de l'entité + collidable,
				// alors on est pas en collision; sinon, on l'est (faire un dessin, ça aide).
				a0 = Math.min(entityBounds.x, collidable.getBoundingBoxRENAME_ME().x);
				b0 = Math.min(entityBounds.y, collidable.getBoundingBoxRENAME_ME().y);
				a1 = (int) Math.max(entityBounds.getMaxX(), collidable.getBoundingBoxRENAME_ME().getMaxX());
				b1 = (int) Math.max(entityBounds.getMaxY(), collidable.getBoundingBoxRENAME_ME().getMaxY());
				final Rectangle bounds = new Rectangle(a0, b0, a1 - a0, b1 - b0);

				// Si la BB du collidable intersecte la BB de l'entité, on l'ajoute à la liste des objets proches.
				if (bounds.getWidth() < collidable.getBoundingBoxRENAME_ME().getWidth() + entityBounds.getWidth()
						&& bounds.getHeight() < collidable.getBoundingBoxRENAME_ME().getHeight() + entityBounds.getHeight())
					collidablesInVicinity.add(collidable);
			}
			float projectedMoveX, projectedMoveY;
			float originalMoveX, originalMoveY;
			float vectorLength;
			int segments;

			// Stocke le mouvement originel pour voir s'il a été modifié après par une collision.
			originalMoveX = nextMoveX;
			originalMoveY = nextMoveY;
			// Itération sur tous les collidables proches de l'entité jusqu'à atteindre une collision.
			for (int k = 0; k < collidablesInVicinity.size() && !contactX && !contactYBottom && !contactYTop; k++) {

				// ----------------------------
				// - Speculative contact part -
				// ----------------------------

				final Area specAABB = new Area(entityBounds);
				final Area specCollidable = new Area(collidablesInVicinity.get(k).getGeometry());
				specAABB.intersect(specCollidable);
				final Rectangle specIntBounds = specAABB.getBounds();
				if (specIntBounds.height >= 1 && specIntBounds.width >= 1) {
					// Test pour les 4 directions du mouvement. 0:haut, 1:bas, 2:gauche, 3:droite.
					for (int dir = 0; dir < 4; dir++) {
						if (dir == 0 && nextMoveY > 0)
							continue;
						if (dir == 1 && nextMoveY < 0)
							continue;
						if (dir == 2 && nextMoveX > 0)
							continue;
						if (dir == 3 && nextMoveX < 0)
							continue;

						final float safeMoveX = (float) Math.max(
								Math.max(specIntBounds.getMinX() - x - entityWidth, x - specIntBounds.getMaxX()), 0);
						final float safeMoveY = (float) Math.max(
								Math.max(specIntBounds.getMinY() - y - entityHeight, y - specIntBounds.getMaxY()), 0);
						final float safeVecLength = (float) Math.sqrt(safeMoveX * safeMoveX + safeMoveY * safeMoveY);
						vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
						projectedMoveX = nextMoveX * (safeVecLength / vectorLength);
						projectedMoveY = nextMoveY * (safeVecLength / vectorLength);
						vectorLength -= safeVecLength;
						segments = 0;

						// Avance le long de la droite du mouvement prévu jusqu'à collision avec le collidable.
						// On checke pour chaque direction la paire des points de collisions définie dans le constructeur de l'entité.
						while (!collidablesInVicinity.get(k).getGeometry().contains((collisionsPoints[dir * 2].x + x + projectedMoveX),
								(collisionsPoints[dir * 2].y + y + projectedMoveY))
								&& !collidablesInVicinity.get(k).getGeometry().contains(
										(collisionsPoints[dir * 2 + 1].x + x + projectedMoveX),
										(collisionsPoints[dir * 2 + 1].y + y + projectedMoveY)) && segments < vectorLength) {
							projectedMoveX += nextMoveX / vectorLength;
							projectedMoveY += nextMoveY / vectorLength;
							segments++;
						}

						// S'il y a eu une collision.
						if (segments < vectorLength) {
							// On corrige le mouvement en trop.
							if (segments > 0) {
								projectedMoveX -= nextMoveX / vectorLength;
								projectedMoveY -= nextMoveY / vectorLength;
							}
							// Ajuste le nextMove en fonction de la direction.
							if (dir >= 2 && dir <= 3)
								nextMoveX = projectedMoveX;
							if (dir >= 0 && dir <= 1)
								nextMoveY = projectedMoveY;
						}
					}
				}

				// --------------------------
				// - Penetration resolution -
				// --------------------------
				// ie : si collision à droite, décaler l'entité vers la gauche slightly.

				// Calcul de l'intersection de la geometry de l'entité et du collidable.
				final Area collidableArea = new Area(collidablesInVicinity.get(k).getGeometry());
				final Area entityArea = new Area(entityGeometry);
				entityArea.transform(AffineTransform.getTranslateInstance((int) (x + nextMoveX), (int) (y + nextMoveY)));
				entityArea.intersect(collidableArea);
				final Rectangle intersection = entityArea.getBounds();
				for (int dir = 0; dir < 4; dir++) {
					if (entityArea.contains((int) (collisionsPoints[dir * 2].x + x + nextMoveX),
							(int) (collisionsPoints[dir * 2].y + y + nextMoveY))
							|| entityArea.contains((int) (collisionsPoints[dir * 2 + 1].x + x + nextMoveX),
									(int) (collisionsPoints[dir * 2 + 1].y + y + nextMoveY))) {
						switch (dir) {
						case 0:
							nextMoveY += intersection.height;
							break;
						case 1:
							nextMoveY -= intersection.height;
							break;
						case 2:
							nextMoveX += intersection.width;
							break;
						case 3:
							nextMoveX -= intersection.width;
							break;
						}
					}
				}

				// On détecte quel type de contact s'est produit en comparant les anciens et nouveaux moves.
				if (nextMoveY > originalMoveY && originalMoveY < 0)
					contactYTop = true;
				if (nextMoveY < originalMoveY && originalMoveY > 0)
					contactYBottom = true;
				if (Math.abs(nextMoveX - originalMoveX) > 0.01f)
					contactX = true;
				// On arrête le saut du player s'il touche un mur horizontalement.
				if (contactX && contactYTop && speedY < 0)
					speedY = nextMoveY = 0;
			}

			// Si un contact a été détecté, on recalcule le mouvement et on empêche de bouger cette frame.
			if (contactYBottom || contactYTop) {
				y += nextMoveY;
				speedY = 0;
				if (contactYBottom)
					jumping = false;
			}
			if (contactX) {
				x += nextMoveX;
				speedX = 0;
			}
		}
		// Update la position de l'entité.
		x += speedX;
		y += speedY;

		final boolean movingSideways = applyInputs();
		applyGravity();
		applyFriction(movingSideways);
		limitSpeeds();

		animation.tick();
	}

	/** Méthode faisant bouger l'entité (faisant, par ex, changer ses speeds). */
	protected boolean applyInputs() {
		return false;
	}

	/** Ajout de la force de gravité. */
	protected void applyGravity() {
		speedY += gravityStrength;
	}

	/** Applique la friction si l'entité est au sol. */
	protected void applyFriction(boolean movingSideways) {
		if (movingSideways)
			return;
		if (speedX < 0)
			speedX += groundFriction;
		if (speedX > 0)
			speedX -= groundFriction;
		// Evite un bug de non-stoppage de l'entité.
		if ((speedX > 0 && speedX < groundFriction) || (speedX < 0 && speedX > -groundFriction))
			speedX = 0;
	}

	/** Speed limiter. */
	protected void limitSpeeds() {
		if (speedX > maxSpeedX)
			speedX = maxSpeedX;
		if (speedX < -maxSpeedX)
			speedX = -maxSpeedX;
		if (speedY > maxSpeedY)
			speedY = maxSpeedY;
		if (speedY < -maxSpeedY)
			speedY = -maxSpeedY;
	}

	@Override
	public void render(Screen screen) {
		// TODO : debug only : affichage des BB des voisins.
		for (final AbstractLandscapeCollidable collidable : collidablesInVicinity) {
			collidable.renderBoundingBox(screen);
		}
		animation.render(screen, (int) x, (int) y);
	}

}
