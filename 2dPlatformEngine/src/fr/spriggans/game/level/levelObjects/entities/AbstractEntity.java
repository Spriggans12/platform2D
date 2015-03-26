package fr.spriggans.game.level.levelObjects.entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidable;
import fr.spriggans.game.physics.BoundingBox;
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
	 * Dans l'ordre : <br/>
	 * - 0/1 : Top of head <br/>
	 * - 2/3 : Bottom of feet <br/>
	 * - 4/5 : Left <br/>
	 * - 6/7 : Right */
	// TODO : Créer my own Point Class ?
	protected Point collisionsPoints[] = new Point[8];

	/** L'animation actuelle jouee par l'entite. */
	protected Animation animation;

	/** Le Level auquel l'entité appartient. */
	protected Level level;

	/** Nombre max de boucles à faire pour chercher une collision. */
	protected final int MAX_ITERATIONS = 3;

	/** Objets collidables proches de l'entité. Seuls ces objets là seront testés pour les collisions pour éviter d'itérer sur tout. */
	List<LandscapeCollidable> collidablesInVicinity = new ArrayList<LandscapeCollidable>();

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
			int a = (int) Math.min(x, x + nextMoveX);
			int b = (int) Math.min(y, y + nextMoveY);
			int c = (int) Math.max(x + entityWidth, x + entityWidth + nextMoveX);
			int d = (int) Math.max(y + entityHeight, y + entityHeight + nextMoveY);
			final BoundingBox entityBounds = new BoundingBox(a, b, c, d);

			// On checke tous les objets pour voir s'ils sont proches de l'entité.
			for (final LandscapeCollidable collidable : level.getCollisionLayer()) {
				// Pas besoin de regarder à la main chacun des 4 cotés du collidable.
				// En utilisant la ruse, on peut utiliser la BB combinée de l'entité et du collidable.
				// Si la BB est plus haute|grosse que la hauteur|grosseur de l'entité + collidable,
				// alors on est pas en collision; sinon, on l'est (faire un dessin, ça aide).
				a = Math.min(entityBounds.getX0(), collidable.getBoundingBox().getX0());
				b = Math.min(entityBounds.getY0(), collidable.getBoundingBox().getY0());
				c = Math.max(entityBounds.getX1(), collidable.getBoundingBox().getX1());
				d = Math.max(entityBounds.getY1(), collidable.getBoundingBox().getY1());
				final BoundingBox bounds = new BoundingBox(a, b, c, d);

				// Si la BB du collidable est en collision avec la BB de l'entité, on l'ajoute à la liste des objets proches.
				if (bounds.getW() < collidable.getBoundingBox().getW() + entityBounds.getW()
						&& bounds.getH() < collidable.getBoundingBox().getH() + entityBounds.getH())
					collidablesInVicinity.add(collidable);
			}

			float projectedMoveX, projectedMoveY;
			float originalMoveX, originalMoveY;
			float vectorLength;
			int segments;

			// Store the original final expected movement of the entity so we can
			// see if it has been modified due to a collision or potential collision later.
			originalMoveX = nextMoveX;
			originalMoveY = nextMoveY;

			// Iteration sur tous les collidables proches de l'entité jusqu'à atteindre une collision.
			for (int k = 0; k < collidablesInVicinity.size() && !contactX && !contactYBottom && !contactYTop; k++) {

				// ----------------------------
				// - Speculative contact part -
				// ----------------------------

				// TODO : ICI Ajouter un improvement.

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
					// Positions actuelles sur la droite du mouvement prévu.
					projectedMoveX = projectedMoveY = 0;
					// Taille de la droite du mouvement prévu.
					vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
					segments = 0;

					// Avance le long de la droite du mouvement prévu jusqu'à collision avec le collidable.
					// TODO : AMELIORER ICI EN NE FAISANT PAS STEP BY STEP...
					// TODO : Créer un objet Geometry pour les collisions plus compliquées.
					// On checke pour chaque direction la paire des points de collisions définie dans le constructeur de l'entité.
					while (!collidablesInVicinity.get(k).getBoundingBox().containsPoint(
							this.collisionsPoints[dir * 2].x + (int) this.x + (int) projectedMoveX,
							this.collisionsPoints[dir * 2].y + (int) this.y + (int) projectedMoveY)
							&& !collidablesInVicinity.get(k).getBoundingBox().containsPoint(
									this.collisionsPoints[dir * 2 + 1].x + (int) this.x + (int) projectedMoveX,
									this.collisionsPoints[dir * 2 + 1].y + (int) this.y + (int) projectedMoveY) && segments < vectorLength) {
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

				// --------------------------
				// - Penetration resolution -
				// --------------------------
				// Ex : si collision à droite, décaler l'entité vers la gauche slightly.
				final BoundingBox entityNextBounds = new BoundingBox((int) x + (int) nextMoveX, (int) y + (int) nextMoveY, (int) x
						+ entityWidth + (int) nextMoveX, (int) y + entityHeight + (int) nextMoveY);

				final BoundingBox intersection = collidablesInVicinity.get(k).getBoundingBox();

				// TODO : get BoundingBox of the intersection of Entity skeleton and collidable.

				// On détecte quel type de contact s'est produit en comparant les anciens et nouveaux moves.
				if (nextMoveY > originalMoveY && originalMoveY < 0)
					contactYTop = true;
				if (nextMoveY < originalMoveY && originalMoveY > 0)
					contactYBottom = true;
				if (Math.abs(nextMoveX - originalMoveX) > 0.01f)
					contactX = true;

				// TODO : tester ce comportement
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
		for (final LandscapeCollidable collidable : collidablesInVicinity) {
			collidable.getBoundingBox().render(screen);
		}
		animation.render(screen, (int) x, (int) y);
	}

}
