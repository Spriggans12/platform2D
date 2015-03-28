package fr.spriggans.game;

import java.util.ArrayList;
import java.util.List;

import fr.spriggans.game.level.Level;
import fr.spriggans.gfx.Screen;

public class Game {
	private final List<Level> levelList = new ArrayList<Level>();
	private int currentLevelIndex;

	public Game(Inputs inputs, Screen screen) {
		currentLevelIndex = 0;
		levelList.add(new Level(inputs));
		screen.calibrateScreenToLevel(levelList.get(currentLevelIndex).getWidth(), levelList.get(currentLevelIndex).getHeight());
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
