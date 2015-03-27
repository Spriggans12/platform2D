package fr.spriggans.game.level;

import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.game.level.levelObjects.background.Background;
import fr.spriggans.game.level.levelObjects.entities.Player;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeCollidable;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeNotCollidable;
import fr.spriggans.gfx.Screen;

public class Level {
	/** Vrai si le level est fini. */
	private boolean over;
	private int width;
	private int height;

	// TODO : Ajouter la couche player ?
	/** Liste contenant les entites mobiles ayant une IA. */
	private final List<AbstractLevelElement> livingEntitiesLayer = new ArrayList<AbstractLevelElement>();

	/** Liste contenant les objets interactifs (items, projectiles, boutons...). */
	private final List<AbstractLevelElement> interactiveLayer = new ArrayList<AbstractLevelElement>();

	/** Liste contenant les tiles (sols, murs) collidables du level. */
	private final List<LandscapeCollidable> collisionLayer = new ArrayList<LandscapeCollidable>();

	/** Liste contenant les tiles non collidables du level. Utilisées pour décoration. */
	private final List<LandscapeNotCollidable> decorativeLayer = new ArrayList<LandscapeNotCollidable>();

	/** Liste contenant les backgrounds du level. */
	private final List<Background> backgroundLayer = new ArrayList<Background>();

	public Level(Inputs inputs) {
		// TODO LOAD THIS DIFFERENTLY.
		this.width = 2000;
		this.height = 1100;

		getLivingEntitiesLayer().add(new Player(200, 200, this, inputs));

		backgroundLayer.add(new Background(0, 0));

		collisionLayer.add(new LandscapeCollidable(130, height - 150, 200, 30));
		collisionLayer.add(new LandscapeCollidable(30, height - 80, 30, 30));
		collisionLayer.add(new LandscapeCollidable(80, height - 120, 30, 30));
		collisionLayer.add(new LandscapeCollidable(350, height - 350, 30, 280));
		collisionLayer.add(new LandscapeCollidable(450, height - 90, 40, 3));
		collisionLayer.add(new LandscapeCollidable(350, height - 100, 100, 30));

		collisionLayer.add(new LandscapeCollidable(250, height - 240, 100, 30));
		collisionLayer.add(new LandscapeCollidable(50, height - 240, 100, 30));

		// Escaliers
		for (int i = 0; i < 20; i++)
			collisionLayer.add(new LandscapeCollidable(450 + i * 30, height - 30 - i, 50, 30));

		for (int i = 0; i < 20; i++)
			collisionLayer.add(new LandscapeCollidable(width - 150 - i * 30, i, 50, 30));

		final int s = 30;
		collisionLayer.add(new LandscapeCollidable(0, 0, width, s));
		collisionLayer.add(new LandscapeCollidable(0, height - s, width, s));
		collisionLayer.add(new LandscapeCollidable(0, 0, s, height));
		collisionLayer.add(new LandscapeCollidable(width - s, 0, s, height));
	}

	public void tick() {
		for (final AbstractLevelElement entity : getLivingEntitiesLayer()) {
			entity.tick();
		}
		for (final AbstractLevelElement interactive : getInteractiveLayer()) {
			interactive.tick();
		}
	}

	public void render(Screen screen) {
		int xOffs = 0;
		int yOffs = 0;

		// Calcul des offsets en fonction du player.
		// TODO : Faire le calcul en fonction d'une camera ? :D
		for (final AbstractLevelElement entity : getLivingEntitiesLayer()) {
			if (entity instanceof Player) {
				final Player player = (Player) entity;
				xOffs = player.getXOffset(screen, width);
				yOffs = player.getYOffset(screen, height);
			}
		}

		if (width > screen.getWidth()) {
			if (xOffs < 0)
				xOffs = 0;
			if (xOffs > width - screen.getWidth())
				xOffs = width - screen.getWidth();
		}
		if (height > screen.getHeight()) {
			if (yOffs < 0)
				yOffs = 0;
			if (yOffs > height - screen.getHeight())
				yOffs = height - screen.getHeight();
		}
		screen.setOffsets(xOffs, yOffs);
		screen.blackOut();

		for (final Background bg : backgroundLayer) {
			bg.render(screen);
		}
		for (final LandscapeNotCollidable deco : decorativeLayer) {
			deco.render(screen);
		}
		for (final LandscapeCollidable collidable : getCollisionLayer()) {
			collidable.render(screen);
		}
		for (final AbstractLevelElement interactive : getInteractiveLayer()) {
			interactive.render(screen);
		}
		for (final AbstractLevelElement entity : getLivingEntitiesLayer()) {
			entity.render(screen);
		}
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<AbstractLevelElement> getLivingEntitiesLayer() {
		return livingEntitiesLayer;
	}

	public List<AbstractLevelElement> getInteractiveLayer() {
		return interactiveLayer;
	}

	public List<LandscapeCollidable> getCollisionLayer() {
		return collisionLayer;
	}
}
