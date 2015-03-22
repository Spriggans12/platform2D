package fr.spriggans.game.level;

import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.Inputs;
import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.game.level.levelObjects.background.Background;
import fr.spriggans.game.level.levelObjects.entities.Player;
import fr.spriggans.game.level.levelObjects.landscape.LandscapeNotCollidable;
import fr.spriggans.gfx.Screen;

public class Level {
	private boolean over;
	private int width;
	private int height;

	/**
	 * Liste contenant les entit�s mobiles ayant une IA.
	 */
	private final List<AbstractLevelElement> livingEntitiesLayer = new ArrayList<AbstractLevelElement>();

	/**
	 * Liste contenant les objets interactifs (items, projectiles, boutons...).
	 */
	private final List<AbstractLevelElement> interactiveLayer = new ArrayList<AbstractLevelElement>();

	/**
	 * Liste contenant les tiles (sols, murs) collidables ou non du level.
	 */
	private final List<AbstractLevelElement> landscapeLayer = new ArrayList<AbstractLevelElement>();

	/**
	 * Liste contenant les backgrounds du level.
	 */
	private final List<AbstractLevelElement> backgroundLayer = new ArrayList<AbstractLevelElement>();

	public Level(Inputs inputs) {
		// TODO LOAD THIS DIFFERENTLY.
		this.width = 2000;
		this.height = 1100;

		livingEntitiesLayer.add(new Player(200, 200, inputs));

		backgroundLayer.add(new Background(0, 0));

		// TODO UN-TEST
		final int s = 50;

		landscapeLayer.add(new LandscapeNotCollidable(0, 0, width, s));
		landscapeLayer.add(new LandscapeNotCollidable(0, height - s, width, s));

		landscapeLayer.add(new LandscapeNotCollidable(0, 0, s, height));
		landscapeLayer.add(new LandscapeNotCollidable(width - s, 0, s, height));

	}

	public void tick() {
		for (final AbstractLevelElement entity : livingEntitiesLayer) {
			entity.tick();
		}
	}

	public void render(Screen screen) {
		int xOffs = 0;
		int yOffs = 0;

		// Calcul des offsets en fonction du player.
		// TODO : Faire le calcul en fonction d'une cam�ra ? :D
		for (final AbstractLevelElement entity : livingEntitiesLayer) {
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

		screen.blackOut();

		for (final AbstractLevelElement bg : backgroundLayer) {
			bg.render(screen, xOffs, yOffs);
		}
		for (final AbstractLevelElement land : landscapeLayer) {
			land.render(screen, xOffs, yOffs);
		}
		for (final AbstractLevelElement interactive : interactiveLayer) {
			interactive.render(screen, xOffs, yOffs);
		}
		for (final AbstractLevelElement entity : livingEntitiesLayer) {
			entity.render(screen, xOffs, yOffs);
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
}
