package fr.spriggans.game.level.levelObjects;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import fr.spriggans.game.level.levelObjects.landscape.AbstractLandscapeCollidable;
import fr.spriggans.gfx.Screen;

@XmlTransient
@XmlSeeAlso({ AbstractLandscapeCollidable.class })
public abstract class AbstractLevelElement {
	/** Coordonnee coin NW pour l'affichage */
	protected int x;
	/** Coordonnee coin NW pour l'affichage */
	protected int y;

	public AbstractLevelElement(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// TODO : RM-ME
	protected int color;

	public void render(Screen screen) {
	}

	public void tick() {
	}
}
