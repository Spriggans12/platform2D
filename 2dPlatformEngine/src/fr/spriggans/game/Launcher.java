package fr.spriggans.game;

public class Launcher {
	public static final boolean DEBUG_SHOW_FPS = false;
	public static final boolean DEBUG_SHOW_COLLISION_BOUNDING_BOX = true;
	public static final boolean DEBUG_SHOW_ENTITY_BB = true;
	public static final boolean DEBUG_SHOW_ENTITY_SPRITE = false;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = WIDTH * 9 / 16;

	public static void main(String[] args) {
		final MainComponent g = new MainComponent();
		g.init(WIDTH, HEIGHT);
		g.start();

		try {
			g.join();
		} catch (final InterruptedException e) {
			System.exit(0);
		}

	}
}
