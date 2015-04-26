package fr.spriggans.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import fr.spriggans.game.level.Level;
import fr.spriggans.gfx.Screen;

public class Game {
	private final List<Level> levelList = new ArrayList<Level>();
	private int currentLevelIndex;

	public Game(Inputs inputs, Screen screen) {
		try {
			currentLevelIndex = 0;

			// Unmarshalling of the level.
			final JAXBContext jabxbContext = JAXBContext.newInstance(Level.class);
			final Unmarshaller unmarshaller = jabxbContext.createUnmarshaller();
			final Level lvl = (Level) unmarshaller.unmarshal(new File("C:\\Users\\Spriggans\\Desktop\\level.xml"));

			System.out.println(lvl);

			levelList.add(lvl);

			screen.calibrateScreenToLevel(levelList.get(currentLevelIndex).getWidth(), levelList.get(currentLevelIndex).getHeight());
		} catch (final JAXBException e) {
			e.printStackTrace();
			System.err.println("Error unmarshalling xml file.");
		}
	}

	public void tick(Screen screen) {
		levelList.get(currentLevelIndex).tick();
		if (levelList.get(currentLevelIndex).isOver()) {
			currentLevelIndex++;
			if (currentLevelIndex == levelList.size())
				currentLevelIndex = 0;
			screen.calibrateScreenToLevel(levelList.get(currentLevelIndex).getWidth(), levelList.get(currentLevelIndex).getHeight());
		}
	}

	public void render(Screen screen) {
		levelList.get(currentLevelIndex).render(screen);
	}
}
