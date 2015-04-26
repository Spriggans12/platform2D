package fr.spriggans.game.level;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.spriggans.game.level.levelObjects.AbstractLevelElement;
import fr.spriggans.game.level.levelObjects.background.Background;
import fr.spriggans.game.level.levelObjects.entities.AbstractEntity;
import fr.spriggans.game.level.levelObjects.entities.Player;
import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Screen;
import fr.spriggans.jaxbMapping.adapters.AbstractEntityListAdapter;
import fr.spriggans.jaxbMapping.adapters.AbstractLandscapeCollidableListAdapter;
import fr.spriggans.jaxbMapping.adapters.BackgroundListAdapter;

@XmlRootElement(name = "level")
public class Level {
	/** Vrai si le level est fini. */
	private boolean over;
	private int width;
	private int height;

	/** Liste contenant les entites mobiles ayant une IA. */
	private List<AbstractEntity> livingEntitiesLayer = new ArrayList<AbstractEntity>();

	/** Liste contenant les objets interactifs (items, projectiles, boutons...). */
	private List<AbstractLevelElement> interactiveLayer = new ArrayList<AbstractLevelElement>();

	/** Liste contenant les tiles (sols, murs) collidables du level. */
	private List<AbstractLandscapeCollidable> collisionLayer = new ArrayList<AbstractLandscapeCollidable>();

	/** Liste contenant les tiles non collidables du level. Utilisées pour décoration. */
	// private final List<LandscapeNotCollidable> decorativeLayer = new ArrayList<LandscapeNotCollidable>();

	/** Liste contenant les backgrounds du level. */
	private List<Background> backgroundLayer = new ArrayList<Background>();

	public Level() {
		System.out.println("jaxb lvl created");

		//
		// if (levelId == 0) {
		// this.width = 2000;
		// this.height = 1100;
		//
		// livingEntitiesLayer.add(new Player(200, 200, this, inputs));
		//
		// // backgroundLayer.add(new Background(0, 0));
		//
		// collisionLayer.add(new LandscapeCollidableRectangle(130, height - 150, 200, 30));
		// collisionLayer.add(new LandscapeCollidableRectangle(30, height - 70, 30, 30));
		// collisionLayer.add(new LandscapeCollidableRectangle(80, height - 120, 30, 30));
		// collisionLayer.add(new LandscapeCollidableRectangle(350, height - 320, 30, 250));
		// collisionLayer.add(new LandscapeCollidableRectangle(450, height - 90, 40, 3));
		// collisionLayer.add(new LandscapeCollidableRectangle(350, height - 100, 100, 30));
		//
		// collisionLayer.add(new LandscapeCollidableRectangle(250, height - 240, 100, 30));
		// collisionLayer.add(new LandscapeCollidableRectangle(50, height - 240, 100, 30));
		//
		// collisionLayer.add(new LandscapeCollidableEllipse(120, height - 520, 3000, 300));
		//
		// // Escaliers
		// for (int i = 0; i < 20; i++)
		// collisionLayer.add(new LandscapeCollidableRectangle(450 + i * 30, height - 30 - i * 10, 50, 30));
		// for (int i = 0; i < 20; i++)
		// collisionLayer.add(new LandscapeCollidableRectangle(350 + i * 30, height - 320 - 3 * i, 50, 30));
		// for (int i = 0; i < 20; i++)
		// collisionLayer.add(new LandscapeCollidableRectangle(350 + 20 * 30 + i * 30, height - 320 - 3 * 20 + 3 * i, 50, 30));
		//
		// for (int i = 0; i < 20; i++)
		// collisionLayer.add(new LandscapeCollidableRectangle(width - 150 - i * 30, i, 50, 30));
		//
		// final int s = 30;
		// collisionLayer.add(new LandscapeCollidableRectangle(0, 0, width, s));
		// collisionLayer.add(new LandscapeCollidableRectangle(0, height - s, width, s));
		// collisionLayer.add(new LandscapeCollidableRectangle(0, 0, s, height));
		// collisionLayer.add(new LandscapeCollidableRectangle(width - s, 0, s, height));
		// }

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

		// for (final Background bg : backgroundLayer) {
		// bg.render(screen);
		// }
		// for (final LandscapeNotCollidable deco : decorativeLayer) {
		// deco.render(screen);
		// }
		for (final AbstractLandscapeCollidable collidable : getCollisionLayer()) {
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

	@XmlAttribute(name = "width")
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@XmlAttribute(name = "height")
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@XmlJavaTypeAdapter(AbstractEntityListAdapter.class)
	@XmlElement(name = "entityList")
	public List<AbstractEntity> getLivingEntitiesLayer() {
		return livingEntitiesLayer;
	}

	public void setLivingEntitiesLayer(List<AbstractEntity> livingEntitiesLayer) {
		this.livingEntitiesLayer = livingEntitiesLayer;
	}

	// TODO
	// @XmlJavaTypeAdapter(AbstractLandscapeCollidableListAdapter.class)
	// @XmlElement(name = "interactionsList")
	public List<AbstractLevelElement> getInteractiveLayer() {
		return interactiveLayer;
	}

	public void setInteractiveLayer(List<AbstractLevelElement> interactiveLayer) {
		this.interactiveLayer = interactiveLayer;
	}

	@XmlJavaTypeAdapter(AbstractLandscapeCollidableListAdapter.class)
	@XmlElement(name = "collidablesList")
	public List<AbstractLandscapeCollidable> getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(List<AbstractLandscapeCollidable> collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	@XmlJavaTypeAdapter(BackgroundListAdapter.class)
	@XmlElement(name = "backgroundList")
	public List<Background> getBackgroundLayer() {
		return backgroundLayer;
	}

	public void setBackgroundLayer(List<Background> backgroundLayer) {
		this.backgroundLayer = backgroundLayer;
	}

}
