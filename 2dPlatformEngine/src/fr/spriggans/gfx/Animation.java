package fr.spriggans.gfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** DEPRECATED ! DO NOT USE NO MORE.
 *
 * Une animation contient une liste de Frames. */
public class Animation {
	// TODO : At some point, exporter ailleurs les animations.
	public static final Animation[] animations = new Animation[128];

	public static final Animation TEST_ANIM_IDDLE = new Animation(
			0,
			new Frame[] { new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 12, 0, 10), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 14, 0, 10), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 15, 0, 10), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 16, 0, 10), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 17, 0, 10), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 18, 0, 10), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 19, 0, 10), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 20, 0, 10), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 21, 0, 10), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 22, 0, 10), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 23, 0, 10) }, true);
	static final int S = 5;
	public static final Animation TEST_ANIM_WALK = new Animation(
			1,
			new Frame[] { new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 0, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 1, 0, S), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 2, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 3, 0, S), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 4, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 5, 0, S), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 6, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 7, 0, S), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 8, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 9, 0, S), new Frame(
					SpriteSheet.METALSLUG_TEST_SHEET, 10, 0, S), new Frame(SpriteSheet.METALSLUG_TEST_SHEET, 11, 0, S) }, true);

	private final int id;
	private List<Frame> frames = new ArrayList<Frame>();
	private int idCurrentFrame;
	/** Compte le nombre de ticks que la frame a deja passé. Atteint frame.duration - 1, on change de frame. */
	private int currentFrameCount;
	// TODO : loop au niveau du dessus ? ou faire un truc ici.
	private boolean loop;

	public Animation(int id, Frame[] frames, boolean loop) {
		this.id = id;
		this.setFrames(Arrays.asList(frames));
		this.setLoop(loop);
		idCurrentFrame = 0;
		idCurrentFrame = 0;
		if (animations[this.id] != null)
			throw new RuntimeException("Animation id " + id + " duplicated.");
		animations[this.id] = this;
	}

	/** @return Vrai si l'animation a atteint sa fin. */
	public boolean tick() {
		// La frame actuelle est finie.
		if (currentFrameCount++ == frames.get(idCurrentFrame).getDuration() - 1) {
			currentFrameCount = 0;
			// Chargement de la prochaine frame.
			if (idCurrentFrame++ == frames.size() - 1) {
				// On a fini la derniere frame de l'animation.
				idCurrentFrame = 0;
				return true;
			}
		}
		return false;
	}

	public void render(Screen screen, int x, int y) {
		render(screen, x, y, 0);
	}

	public void render(Screen screen, int x, int y, int mirrorBits) {
		final Frame f = frames.get(idCurrentFrame);
		screen.renderPixels(f.getPixels(), f.getWidth(), f.getHeight(), x, y, mirrorBits);
	}

	public void raz() {
		idCurrentFrame = 0;
		currentFrameCount = 0;
	}

	public List<Frame> getFrames() {
		return frames;
	}

	public void setFrames(List<Frame> frames) {
		this.frames = frames;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

}
