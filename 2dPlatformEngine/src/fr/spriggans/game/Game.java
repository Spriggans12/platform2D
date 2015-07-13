package fr.spriggans.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.spriggans.game.level.Level;
import fr.spriggans.game.level.chat.Chat;
import fr.spriggans.game.level.levelObjects.entities.AbstractEntity;
import fr.spriggans.game.level.levelObjects.entities.Player;
import fr.spriggans.gfx.Screen;

public class Game {
	private final List<Level> levelList = new ArrayList<Level>();
	private int currentLevelIndex;

	public Game(KeyboardInput keyboard, Screen screen) {
		try {
			currentLevelIndex = 0;

			final JAXBContext jabxbContext = JAXBContext.newInstance(Level.class);
			final Unmarshaller unmarshaller = jabxbContext.createUnmarshaller();
			final Level lvl = (Level) unmarshaller.unmarshal(new File("res/levels/level.xml"));

			// Et ajout du chat pour le level, TODO : si celui-ci est activé.
			lvl.setChat(new Chat(keyboard));

			// Ajout de l'attribut level pour les entités.
			for (final AbstractEntity entity : lvl.getLivingEntitiesLayer()) {
				entity.setLevel(lvl);
				// Ajout des inputs du clavier pour le player.
				if (entity instanceof Player)
					((Player) entity).setKeyboardInput(keyboard);
			}
			levelList.add(lvl);

			screen.calibrateScreenToLevel(levelList.get(currentLevelIndex).getWidth(), levelList.get(currentLevelIndex).getHeight());
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

	public boolean tick(Screen screen) {
		levelList.get(currentLevelIndex).tick();
		if (levelList.get(currentLevelIndex).isOver()) {
			currentLevelIndex++;
			if (currentLevelIndex == levelList.size())
				return false;
			screen.calibrateScreenToLevel(levelList.get(currentLevelIndex).getWidth(), levelList.get(currentLevelIndex).getHeight());
		}
		return true;
	}

	public void render(Screen screen) {
		levelList.get(currentLevelIndex).render(screen);
	}
}
