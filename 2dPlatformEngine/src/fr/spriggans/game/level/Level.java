package fr.spriggans.game.level;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.spriggans.game.level.chat.IChat;
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

	/** Chat du level. Si null, le chat n'est pas actif pour ce level. */
	private IChat chat;
	
	public Level() {
		System.out.println("jaxb lvl created");
	}

	public void tick() {
		for (final AbstractLevelElement entity : getLivingEntitiesLayer()) {
			entity.tick();
		}
		for (final AbstractLevelElement interactive : getInteractiveLayer()) {
			interactive.tick();
		}
		if(chat != null) {
			// TODO utiliser un pseudo.
			chat.tick("Spriggans");
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
		if(chat != null) {
			chat.render(screen);
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
	@XmlElement(name = "entities")
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
	@XmlElement(name = "collidables")
	public List<AbstractLandscapeCollidable> getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(List<AbstractLandscapeCollidable> collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	@XmlJavaTypeAdapter(BackgroundListAdapter.class)
	@XmlElement(name = "backgrounds")
	public List<Background> getBackgroundLayer() {
		return backgroundLayer;
	}

	public void setBackgroundLayer(List<Background> backgroundLayer) {
		this.backgroundLayer = backgroundLayer;
	}

	public void setChat(IChat chat) {
		this.chat = chat;
	}
}
